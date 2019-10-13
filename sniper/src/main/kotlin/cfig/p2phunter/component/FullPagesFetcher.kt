package cfig.p2phunter.component

import cfig.Jargon
import cfig.component.MinaSessionManager
import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.SendListingIdReq2
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.sdk2.Pacer
import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.PpdClient
import cfig.p2psniper.ppdsdk.PpdServiceBase
import cfig.p2psniper.ppdsdk.params.PpdResp.LoanListResp.SingleListingResp
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class FullPagesFetcher {
    private val sniperStore: SniperStore
    private val sniperProps: SniperProps

    //recent Id history
    private var recentIds = LinkedList<Long>()
    private val recentIdMaxSize = 30000
    //initial run, ignore first fetch
    private var bFirstFetch = true

    //check loans XX ms ago
    private val loanTimePeriod = 5000L

    //sleep to match pacer
    val tooFastTime: Int

    @Autowired
    private val jdc: JedisClient? = null

    constructor(sS: SniperStore, sP: SniperProps) {
        sniperProps = sP
        sniperStore = sS
        tooFastTime = sniperProps.threads * 98 // nearly = (threads * 100ms)
        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()

        if (!sniperProps.fetchEnable) {
            log.warn("Loan Fetcher disabled, running in dry-run mode ...")
            return
        }

        val fetcherA = Thread(Runnable {
            val pacer = Pacer(10 * 1000, 94, 50)
            log.info("Start fetcher with ${sniperProps.threads} threads")
            for (i in 1..sniperProps.threads) {
                TimeUnit.SECONDS.sleep(1)
                Thread(Runnable {
                    while (true) {
                        fetchOnce(sniperStore.clientA!!, pacer, i)
                    }
                }).start()
            }
        })

        val fetcherB = Thread(Runnable {
            val pacer = Pacer(10 * 1000, 94, 50)
            log.info("Start fetcher with ${sniperProps.threads} threads")
            for (i in 1..sniperProps.threads) {
                TimeUnit.SECONDS.sleep(1)
                Thread(Runnable {
                    while (true) {
                        fetchOnce(sniperStore.clientB!!, pacer, i)
                    }
                }).start()
            }
        })

        if (sniperStore.clientA!!.ppdApp.appId == sniperStore.clientB!!.ppdApp.appId) {
            fetcherA.start()
        } else {
            fetcherA.start()
            fetcherB.start()
        }
    }

    private fun consumeOnce(newList: List<SingleListingResp>, tsAfterQuery: Long) {
        if (newList.isEmpty()) return
        //1. bid AA loan
        safeLoanConsumer!!.consumeNB(newList)

        //2. check database and consume
        val diggerList: MutableList<SingleListingResp> = mutableListOf()
        newList.forEach {
            //FIXME: disable database checking, we dont have prefetch for now
//            val aLoan = loanRepoExt!!.findByListingId(it.ListingId)
//            if (aLoan != null) {
//                if (sniperProps.isStandAlone()) {
//                    //do nothing as standalone server
//                } else {
//                    //consume it directly
//                    log.debug("Consume directly: ${it.ListingId}")
//                    creditLoanConsumer!!.consumeNB(aLoan, tsAfterQuery)
//                }
//            } else {
//                diggerList.add(it)
//            }
            diggerList.add(it)
        }

        //3. send to digger
        loanDigger!!.digListings(diggerList, tsAfterQuery)
    }

    private fun fetchOnce(oneTimeClient: PpdClient, inPacer: Pacer, index: Int = 1) {
        Thread.currentThread().name = oneTimeClient.ppdApp.nickName + "-" + index
        try {
            var pageNo = 1
            val tsRoundStart = System.currentTimeMillis()
            while (true) {
                while (inPacer.overSpeed()) {
                    log.debug("overSpeed")
                    TimeUnit.MILLISECONDS.sleep(inPacer.napTime)
                }
                val tsBeforeQuery = System.currentTimeMillis()
                inPacer.update(tsBeforeQuery)
                val loans = oneTimeClient.loanService.loanList(Timestamp(tsBeforeQuery - loanTimePeriod).toLocalDateTime(), pageNo)
                val tsAfterQuery = System.currentTimeMillis()
                //update recent ID cache
                val newList = updateCacheLocked(loans)

                if (newList.isNotEmpty()) {
                    //log
                    val ids: MutableList<Long> = mutableListOf()
                    newList.forEach { ids.add(it.ListingId) }
                    val timeSec = (tsAfterQuery - tsRoundStart) / 1000
                    val timeMil = (tsAfterQuery - tsRoundStart) % 1000
                    log.info("${loans.size}-${newList.size}: $ids, ${tsAfterQuery - tsBeforeQuery}ms, round ${timeSec}s${timeMil}ms, page[$pageNo]")

                    if (bFirstFetch) {//ignore
                        log.warn("Ignore first fetch: " + newList.size)
                    } else {//consume
                        //2. inform redis
                        Thread(Runnable {
                            jdc!!.publish(newList)
                        }).start()
                        //1. inform peer
                        if (!sniperProps.mina_peer.isBlank()) {
                            Thread(Runnable {
                                try {
                                    sendListingId4(sniperProps.mina_peer, newList)
                                } catch (e: Exception) {
                                    log.error("Fail to sendListingId2 to ${sniperProps.mina_peer}")
                                }
                            }).start()
                        }
                        consumeOnce(newList, tsAfterQuery)
                    }
                }//newList.isNotEmpty

                //break if page end
                if (loans.size < 200) {
                    if (tsAfterQuery - tsRoundStart < tooFastTime) {
                        log.debug("Sleep ${tooFastTime - (tsAfterQuery - tsRoundStart)}ms")
                        TimeUnit.MILLISECONDS.sleep(tooFastTime - (tsAfterQuery - tsRoundStart))
                    }
                    break
                }
                pageNo++
            }
            if (bFirstFetch) bFirstFetch = false
        } catch (e: com.fasterxml.jackson.core.JsonParseException) {
            log.error("JsonParseException: ${e.message}")
        } catch (e: Exception) {
            LogHelper.dump("FullPagesFetcher.updater", e)
            log.error("Unexpected Exception: ${e.message}")
        }
    }

    private fun updateCacheLocked(inNewIds: List<SingleListingResp>):
            List<SingleListingResp> {
        val newList: MutableList<SingleListingResp> = mutableListOf()
        synchronized(this) {
            inNewIds
                    .filter { !recentIds.contains(it.ListingId) }
                    .forEach {
                        recentIds.addLast(it.ListingId)
                        newList.add(it)
                    }

            //reduce
            for (i in 1..(recentIds.size - recentIdMaxSize)) {
                val origSize = recentIds.size
                if (recentIds.isEmpty()) {
                    log.error("xx: recentIds is empty")
                } else {
                    try {
                        recentIds.removeFirst()
                    } catch (e: java.util.NoSuchElementException) {
                        log.error("NoSuchElementException: size= ${recentIds.size}")
                        val try2 = recentIds.pollFirst()
                        if (try2 == null) {
                            log.error("pollFirst also got null")
                        } else {
                            log.error("pollFirst success")
                        }
                    }
                }
                val newSize = recentIds.size
                log.debug("recentIds size: $origSize -> $newSize")
            }
        }

        return newList
    }

    //for mina slave / redis
    fun onListingReceived4(loans: List<SingleListingResp>, host: String, src: NewListingSrc = NewListingSrc.MINA) {
        val tsOnReceive = System.currentTimeMillis()
        //update recent ID cache
        val newList = updateCacheLocked(loans)
        //log
        val ids: MutableList<Long> = mutableListOf()
        newList.forEach { ids.add(it.ListingId) }
        if (newList.isNotEmpty()) {
            log.info("${loans.size}-${newList.size}: $ids, <- $src [$host]")
            consumeOnce(newList, tsOnReceive)
        }
    }

    private fun sendListingId4(url: String, inIds: List<SingleListingResp>) {
        val j1 = Jargon()
        j1.theSrcPort = 4571
        j1.theDstPort = 4570
        j1.theMsgType = Jargon.MSG_TYPE.PPD_LISTING
        j1.theDstIP = InetAddress.getByName(url)
        val session = msm!!.getSession(j1)
        if ((session != null) && session.isConnected()) {
            val ts = System.currentTimeMillis()
            j1.theTime = Timestamp(ts)
            val reqObj = SendListingIdReq2(inIds)
            val jsonStr = mapper2.writeValueAsString(reqObj)
            j1.theCmd = jsonStr
            session.write(j1)
        } else {
            log.error("Can not get session again")
        }
    }

    @Autowired
    private val loanDigger: LoanDigger? = null

    @Autowired
    private val msm: MinaSessionManager? = null

    @Autowired
    private val loanRepoExt: LoanRepoExt? = null

    @Autowired
    private val safeLoanConsumer: SafeLoanConsumer? = null

    @Autowired
    private val creditLoanConsumer: CreditLoanConsumer? = null

    val mapper2 = ObjectMapper()

    companion object {
        private val log = LoggerFactory.getLogger(FullPagesFetcher::class.java)
    }

    enum class NewListingSrc {
        SELF,
        MINA,
        REDIS
    }
}
