package cfig.p2phunter.component

import cfig.Jargon
import cfig.component.MinaSessionManager
import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.SendListingIdReq
import cfig.p2phunter.th.SniperResp
import cfig.p2psniper.common.entity.Loan
import cfig.p2psniper.common.entity.PendingLoan
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.repository.PendingLoanRepo
import cfig.p2psniper.common.repository.PendingLoanRepoExt
import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.PpdServiceBase
import cfig.rabbit.DigReceiver
import cfig.rabbit.DigWorker
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Component
class DetailFetcher {
    private val sniperProps: SniperProps
    private val pendingRepoExt: PendingLoanRepoExt
    private val msm: MinaSessionManager
    private val discoverThreads: Int
    private var listingHead = 100000000L
    private val WORKERS = 5
    private val mapper2 = ObjectMapper()
    private val bottomReviewCnt = AtomicLong(0)
    private val topReviewCnt = AtomicLong(0)

    constructor(plre: PendingLoanRepoExt, minaSessionMgr: MinaSessionManager, sP: SniperProps, loanDigger: LoanDigger) {
        sniperProps = sP
        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()
        msm = minaSessionMgr
        pendingRepoExt = plre
        //$WORKERS times of client count
        discoverThreads = sniperProps.preFetchClients.size * WORKERS

        if (sP.rabbitMode != 0) {
            log.warn("Rabbitmq client running in " + if (sP.rabbitMode == 1) "dev" else "productin" + " mode")
            //rabbitmq Loan receiver
            val digReceiver = DigReceiver(sP.rabbitMode)
            digReceiver.prep()
            digReceiver.recvResult(this)

            //rabbitmq worker
            Thread(Runnable {
                TimeUnit.SECONDS.sleep(10)
                val digWorker = DigWorker(sP.rabbitMode)
                digWorker.prep()
                digWorker.fetchTasks(loanDigger)
            }).start()
        } else {
            log.warn("Rabbitmq disabled")
        }

        if (!sniperProps.preFetchEnable) {
            log.info("Pre-Fetch disabled")
            return
        } else {
            log.info("Pre-Fetch enabled")
        }

        Thread(Runnable {
            TimeUnit.SECONDS.sleep(5)
            listingHead = pendingRepoExt.findMaxListingId()
            log.info("Initial listing HEAD is $listingHead")
            requestAnotherPreDig()
        }).start()

        Thread(Runnable {
            requestAnotherReview(10) //from top
            requestAnotherReview(10, true) //from bottom
        }).start()
    }

    //MSG_TYPE.PPD_LISTING_DETAIL
    fun eatListings(resp: SniperResp.PreBidResp) {
        resp.loanList.forEach {
            maybeSaveListing(it)
        }
    }

    fun maybeSaveListing(loan: Loan): Boolean {
        return if (null == loanRepoExt!!.findByListingId(loan.ListingId)) {
            try { //XXX: if sth abnormal
                loanRepo!!.save(loan)
            } catch (e: org.hibernate.exception.ConstraintViolationException) {
                log.error("$e: maybeSaveListing($loan)")
            } catch (e: java.sql.SQLIntegrityConstraintViolationException) {
                log.error("$e: maybeSaveListing($loan)")
            } catch (e: java.sql.SQLException) {
                log.error("$e: maybeSaveListing($loan)")
            } catch (e: Exception) {
                log.error("Unexpected exception: $e: maybeSaveListing($loan)")
            }
            true
        } else {
            false
        }
    }

    fun maybeDeletePending(loan: Loan) {
        val searchItem = pendingRepoExt.findById(loan.ListingId)
        if (searchItem != null) {
            pendingRepo!!.delete(searchItem)
        }
    }

    fun historyReviewListing() {
        val roundNum = bottomReviewCnt.incrementAndGet()
        log.info("Reviewing history, round $roundNum...")
        val todoList = pendingRepoExt.findOldIds(300000)
        var lap = 0
        if (todoList.isNotEmpty()) {
            log.info("Reviewing history, min=" + todoList[0] + ", max="
                    + todoList[if (todoList.size == 1) 0 else (todoList.size - 1)])
            var pointer = 0
            while (true) {
                val startId = pointer
                val endId = minOf(todoList.size, startId + 10)
                if (startId >= endId) break
                lap++
                Thread(Runnable {
                    try {
                        val currentLap = lap
                        log.debug("review sublist: $startId, $endId, lap $currentLap")
                        val req = SendListingIdReq(ids = todoList.subList(startId, endId))
                        val resp = loanDetail(req, (pointer / 10), false)
                        if (resp!!.loanList.isNotEmpty()) {
                            val idList: MutableList<Long> = mutableListOf()
                            resp.loanList.mapTo(idList) { it.ListingId }
                            log.info("[$roundNum]bottom - lap $currentLap: got ${resp.loanList.size}: $idList")
                            resp.loanList.forEach {
                                maybeSaveListing(it)
                                maybeDeletePending(it)
                            }
                            //XXX: do not broadcast for history reviews
//                            broadcastListingDetailNB(resp)
                        } else {
                            log.debug("review lap $currentLap: empty")
                        }
                    } catch (e: SocketTimeoutException) {
                        log.error("$e")
                    }
                }).start()

                if (pointer >= todoList.size) break
                pointer += 10
                TimeUnit.MILLISECONDS.sleep(500)
            }
            requestAnotherReview(5, true)
        } else {
            log.info("Review history done")
            requestAnotherReview(30, true)
        }
    }

    fun reviewListing() {
        val roundNum = topReviewCnt.incrementAndGet()
        log.info("Reviewing, round $roundNum...")
        val todoList = pendingRepoExt.findRecentIds(10000)
        var lap = 0
        if (todoList.isNotEmpty()) {
            log.info("Reviewing, min=" + todoList[0] + ", " +
                    "max=" + todoList[if (todoList.size == 1) 0 else (todoList.size - 1)])
            var pointer = 0
            while (true) {
                val startId = pointer
                val endId = minOf(todoList.size, startId + 10)
                if (startId >= endId) break
                lap++
                Thread(Runnable {
                    try {
                        val currentLap = lap
                        log.debug("review sublist: $startId, $endId, lap $currentLap")
                        val req = SendListingIdReq(ids = todoList.subList(startId, endId))
                        val resp = loanDetail(req, (pointer / 10), false)
                        if (resp!!.loanList.isNotEmpty()) {
                            val idList: MutableList<Long> = mutableListOf()
                            resp.loanList.mapTo(idList) { it.ListingId }
                            log.info("[$roundNum]top - lap $currentLap: got ${resp.loanList.size}: $idList")
                            resp.loanList.forEach {
                                maybeSaveListing(it)
                                maybeDeletePending(it)
                            }
                            broadcastListingDetailNB(resp)
                        } else {
                            log.debug("++lap $currentLap: empty")
                        }
                    } catch (e: SocketTimeoutException) {
                        log.error("$e")
                    }
                }).start()

                if (pointer >= todoList.size) break
                pointer += 10
                TimeUnit.MILLISECONDS.sleep(0L + 500 / sniperProps.preFetchClients.size)
            }
            requestAnotherReview(5)
        } else {
            log.info("Review done")
            requestAnotherReview(30)
        }
    }

    @Synchronized
    fun discoverListing() {
        val shadowList: MutableList<Long> = mutableListOf()
        (1..(10 * discoverThreads)).mapTo(shadowList) { i -> listingHead + i }
        log.info("Discover: $listingHead +${10 * discoverThreads}")
        val sw = StopWatch("discover")
        sw.start()

        try {
            val respArray = arrayOfNulls<SniperResp.PreBidResp>(discoverThreads)
            val threadArray = arrayOfNulls<Thread>(discoverThreads)
            val reqArray = arrayOfNulls<SendListingIdReq>(discoverThreads)
            val retSave = arrayOfNulls<Int>(discoverThreads)

            var roundSize = 0
            for (i in 0 until discoverThreads) {
                threadArray[i] = Thread(Runnable {
                    //log.info("from " + (i * 10) + " to " + (i * 10 + 10))
                    reqArray[i] = SendListingIdReq(ids = shadowList.subList(i * 10, i * 10 + 10))
                    respArray[i] = loanDetail(reqArray[i]!!, i, true)
                    var cnt = 0
                    respArray[i]!!.loanList.forEach {
                        if (maybeSaveListing(it)) cnt++
                    }
                    retSave[i] = cnt
                    if (respArray[i]!!.loanList.isNotEmpty()) {
                        broadcastListingDetailNB(respArray[i]!!)
                    }
                })
            }
            for (i in 0 until discoverThreads) {
                threadArray[i]!!.start()
            }

            for (i in 0 until discoverThreads) {
                threadArray[i]!!.join()
            }

            for (i in 0 until discoverThreads) {
                val listOfIds: MutableList<Long> = mutableListOf()
                respArray[i]!!.loanList.mapTo(listOfIds) { it.ListingId }
                log.debug("$i Recv [${listOfIds.size}] -> ${retSave[i]}: $listOfIds")
                reqArray[i]!!.ids.forEach {
                    if (listOfIds.contains(it)) {
                        //found
                    } else {
                        pendingRepo!!.save(PendingLoan(it))
                    }
                }
                roundSize += respArray[i]!!.loanList.size
            }
            if (sw.isRunning) sw.stop()
            if (roundSize > 0) {
                //send next request
                listingHead += (10 * discoverThreads)
                log.info("+Round size: $roundSize, $sw")
                requestAnotherPreDig()
            } else {
                log.info("-Round size: $roundSize, pause, $sw")
                requestAnotherPreDig(10)
            }
        } catch (e: SocketTimeoutException) {
            if (sw.isRunning) sw.stop()
            log.error("$e")
        } finally {
            if (sw.isRunning) sw.stop()
        }
    }

    private fun loanDetail(sendListingIdReq: SendListingIdReq, index: Int, discover: Boolean = true): SniperResp.PreBidResp? {
        val clientIP = sniperProps.preFetchClients[index % sniperProps.preFetchClients.size]
        val client = OkHttpClient()
        val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = RequestBody.create(JSON_TYPE, ObjectMapper().writeValueAsString(sendListingIdReq))
        val req = Request.Builder()
                .url("http://$clientIP:4580/dig")
                .post(jsonBody)
                .build()

        var ret = SniperResp.PreBidResp()
        try {
            val resp = client.newCall(req).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                ret = ObjectMapper().readValue(respJson, SniperResp.PreBidResp::class.java)
            } else {
                log.error("loanDetail() got null body")
            }
        } catch (e: java.net.SocketTimeoutException) {
            log.error("$clientIP: loanDetail(): $e")
        } catch (e: com.fasterxml.jackson.core.JsonParseException) {
            log.error("$clientIP: loanDetail(): $e")
        } catch (e: JsonMappingException) {
            log.error("$clientIP: loanDetail(): $e")
        } catch (e: java.io.IOException) {
            log.error("$clientIP: loanDetail(): $e")
            LogHelper.dump(log.name, e)
        }
        return ret
    }

    private fun broadcastListingDetailNB(resp: SniperResp.PreBidResp, delaySeconds: Long = 0) {
        Thread(Runnable {
            if (delaySeconds > 0) {
                log.info("broadcastListingDetailNB() after $delaySeconds seconds ...")
                TimeUnit.SECONDS.sleep(delaySeconds)
            } else {
                log.debug("broadcastListingDetailNB() immediately ...")
            }
            sniperProps.preFetchClients.forEach {
                val j1 = Jargon()
                j1.theSrcPort = 4571
                j1.theDstPort = 4570
                j1.theMsgType = Jargon.MSG_TYPE.PPD_LISTING_DETAIL
                j1.theDstIP = InetAddress.getByName(it)
                j1.theCmd = ObjectMapper().writeValueAsString(resp)
                val session = msm.getSession(j1)
                if ((session != null) && session.isConnected) {
                    session.write(j1)
                } else {
                    log.error("Can not get session again for $it")
                }
                TimeUnit.SECONDS.sleep(1)
            }
        }).start()
    }

    private fun requestAnotherPreDig(delaySeconds: Long = 0) {
        Thread(Runnable {
            if (delaySeconds > 0) {
                log.info("requestAnotherPreDig() after $delaySeconds seconds ...")
                TimeUnit.SECONDS.sleep(delaySeconds)
            } else {
                log.debug("requestAnotherPreDig() immediately ...")
            }
            val j1 = Jargon()
            j1.theSrcPort = 4571
            j1.theDstPort = 4570
            j1.theMsgType = Jargon.MSG_TYPE.PPD_PRE_DIG
            val session = msm.getSession(j1)
            if ((session != null) && session.isConnected) {
                session.write(j1)
            } else {
                log.error("Can not get session again for localhost")
            }
        }).start()
    }

    private fun requestAnotherReview(delaySeconds: Long = 0, fromBottom: Boolean = false) {
        Thread(Runnable {
            val msg = if (fromBottom) "bottom" else "top"
            if (delaySeconds > 0) {
                log.info("requestAnotherReview() from $msg after $delaySeconds seconds ...")
                TimeUnit.SECONDS.sleep(delaySeconds)
            } else {
                log.info("requestAnotherReview() from $msg immediately ...")
            }
            val j1 = Jargon()
            j1.theSrcPort = 4571
            j1.theDstPort = 4570
            j1.theMsgType = Jargon.MSG_TYPE.PPD_REVIEW_PENDING
            j1.theCmd = if (fromBottom) "bottom" else ""
            val session = msm.getSession(j1)
            if ((session != null) && session.isConnected) {
                session.write(j1)
            } else {
                log.error("Can not get session again")
            }
        }).start()
    }

    @Autowired
    private val loanRepo: LoanRepo? = null

    @Autowired
    private val loanRepoExt: LoanRepoExt? = null

    @Autowired
    private val pendingRepo: PendingLoanRepo? = null

    companion object {
        private val log = LoggerFactory.getLogger(DetailFetcher::class.java)
    }
}
