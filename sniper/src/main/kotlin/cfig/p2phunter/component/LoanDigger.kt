package cfig.p2phunter.component

import cfig.Jargon
import cfig.Jargon.MSG_TYPE
import cfig.component.MinaSessionManager
import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.utils.HunterMatcher
import cfig.p2psniper.common.entity.Loan
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.sdk2.Pacer
import cfig.p2psniper.ppdsdk.PpdClient
import cfig.p2psniper.ppdsdk.params.PpdResp.LoanListResp.SingleListingResp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@Component
class LoanDigger {
    private val currentDigClient = AtomicBoolean()
    private val DIG_SIZE = 2
    //500 times/1min -> 83 times / 10 sec
    private val pacer = Pacer(10 * 1000, 79, 50)
    private val sniperStore: SniperStore
    private val sniperProps: SniperProps
    private val jedisClient: JedisClient

    constructor(sS: SniperStore, sP: SniperProps, jC: JedisClient) {
        sniperStore = sS
        sniperProps = sP
        jedisClient = jC
    }

    private fun sendDigRequestDelayed(remainCount: Int = 0) {
        val j = Jargon()
        j.theDstPort = 4570
        j.theMsgType = MSG_TYPE.PPD_DIG_REQUEST
        j.theParam = 150 //delay 500ms
        val session = msm!!.getSession(j)
        if (session != null && session.isConnected) {
            session.write(j)
        }
        log.info("$remainCount loans in queue for digging")
    }

    private fun getOneTimeDigClient(): PpdClient {
        var oneTimeClient: PpdClient? = null
        synchronized(currentDigClient) {
            if (currentDigClient.get()) {
                oneTimeClient = sniperStore.clientB
                currentDigClient.set(false)
            } else {
                oneTimeClient = sniperStore.clientA
                currentDigClient.set(true)
            }
        }
        return oneTimeClient!!
    }

    fun digListings(listings: List<SingleListingResp>, tsAfterQuery: Long) {
        if (listings.isNotEmpty()) {
            val loansToDig: MutableList<Long> = mutableListOf()
            listings.forEach { loansToDig.add(it.ListingId) }
            for (i in 0..(loansToDig.size - 1) step DIG_SIZE) {//split list
                val shadowList: MutableList<Long> = mutableListOf()
                shadowList.addAll(loansToDig.subList(i, minOf(i + DIG_SIZE, loansToDig.size)))

                Thread(Runnable { digAnt(getOneTimeDigClient(), shadowList, tsAfterQuery) }).start()
            }
        }
    }

    private fun digAnt(oneTimeClient: PpdClient, shadowList: MutableList<Long>, tsAfterQuery: Long) {
        Thread.currentThread().name = "dig_" + oneTimeClient.ppdApp.nickName

        while (pacer.overSpeed()) {
            log.debug("Digger overSpeed")
            TimeUnit.MILLISECONDS.sleep(pacer.napTime)
        }

        val ts1 = System.currentTimeMillis()
        pacer.update(ts1)

        try {
            val detailResp = oneTimeClient.loanService.listingDetail(shadowList)
            val tsAfterDig = System.currentTimeMillis()

            if (sniperProps.isStandAlone()) {
                //do nothing in standalone mode
            } else {
                //start bidding immediately
                creditLoanConsumer!!.consumeNB(detailResp, tsAfterDig)
            }

            //publish to AI strategy generator
            jedisClient.publishLoanDetail(detailResp)

            //log later
            log.info("Dig $shadowList, prepare ${ts1 - tsAfterQuery}ms, dig ${tsAfterDig - ts1}ms, total ${tsAfterDig - tsAfterQuery}ms")

            //save in db at last
            detailResp.LoanInfos?.let { infos ->
                infos.forEach {
                    maybeSaveListing(HunterMatcher.loanDetailItem2Loan(it))
                }
            }
        } catch (e: java.net.SocketTimeoutException) {
            log.error("SocketTimeoutException: dig details: $shadowList")
        }
    }

    //answering pre-dig request
    fun preDig(shadowList: List<Long>): List<Loan> {
        val ret: MutableList<Loan> = mutableListOf()
        val oneTimeClient = getOneTimeDigClient()
        var retry = 0

        while (pacer.overSpeed()) {
            log.info("Digger overSpeed")
            TimeUnit.MILLISECONDS.sleep(pacer.napTime)
        }

        while (true) {
            if (retry > 0) log.info("Retry " + retry)
            try {
                pacer.update(System.currentTimeMillis())
                val detailResp = oneTimeClient.loanService.listingDetail(shadowList)
                if (null != detailResp.LoanInfos) {
                    detailResp.LoanInfos!!.mapTo(ret) { HunterMatcher.loanDetailItem2Loan(it) }
                } else {
                    log.error("detailResp.LoanInfos() is null")
                }
                return ret
            } catch (e: java.net.SocketTimeoutException) {
                log.error("$e: dig details")
            } catch (e: com.fasterxml.jackson.core.JsonParseException) {
                log.error("$e: dig details")
            }
            if (retry++ > 2) break
        }
        return ret
    }

    private fun maybeSaveListing(loan: Loan): Boolean {
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

    @Autowired
    private val loanRepo: LoanRepo? = null

    @Autowired
    private val loanRepoExt: LoanRepoExt? = null

    @Autowired
    private val msm: MinaSessionManager? = null

    @Autowired
    private val creditLoanConsumer: CreditLoanConsumer? = null

    companion object {
        private val log = LoggerFactory.getLogger(LoanDigger::class.java)
    }
}
