package cfig.p2phunter.component

import cfig.Jargon
import cfig.component.MinaSessionManager
import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.SniperReq
import cfig.p2psniper.common.entity.PendingLoan
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.repository.PendingLoanRepo
import cfig.p2psniper.common.repository.PendingLoanRepoExt
import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.PpdServiceBase
import cfig.p2psniper.ppdsdk.params.PpdResp
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.util.concurrent.TimeUnit

@Component
class HistoryLoanWorker {
    constructor(lre: LoanRepoExt,
                pre: PendingLoanRepoExt,
                pr: PendingLoanRepo,
                lr: LoanRepo,
                sS: SniperStore,
                gC: SniperConfig,
                mSm: MinaSessionManager,
                sP: SniperProps) {
        loanRepoExt = lre
        pendingRepoExt = pre
        pendingRepo = pr
        sniperStore = sS
        loanRepo = lr
        sniperConfig = gC
        msm = mSm
        sniperProps = sP

        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()

        if (!sniperProps.preFetchEnable) {
            log.info("Pre-Fetch disabled")
            return
        } else {
            log.info("Pre-Fetch enabled")
        }

        Thread(Runnable {
            TimeUnit.SECONDS.sleep(5)
            while (true) {
                try {
                    loanStatusCheck()
                    TimeUnit.SECONDS.sleep(10)
                } catch (e: Exception) {
                    log.error("$e")
                }
            }
        }).start()
    }

    @Deprecated("move such logic to standalone program")
    private fun check() {
        log.info("check start")
        var cnt = 0
        //80 million - 105 million
        for (i in 101240000L until 102_000_000L) {
            try {
                if (i.toInt() % 10000 == 0) log.info("$i: added $cnt")
                val loan = loanRepoExt.findByListingId(i)
                val pendingLoan = pendingRepoExt.findById(i)
                if (loan == null && pendingLoan == null) {
                    cnt++
                    pendingRepo.save(PendingLoan(i, false))
                }
            } catch (e: Exception) {
                log.error("Unexpected exception: $e")
                LogHelper.dump(HistoryLoanWorker::class.java.canonicalName, e)
            }
            TimeUnit.MILLISECONDS.sleep(50)//sleep 50ms
        }
        log.info("check finished, total $cnt")
    }

    private fun loanStatusCheck() {
        val loans = loanRepoExt.findLoansWithNullStatus(sniperProps.preFetchClients.size * 20)
        if (loans.size > 0) {
            for (i in 0 until loans.size step 20) {
                val sublist = loans.subList(i, minOf(i + 19, loans.size - 1))
                log.info("status check: " + i + " - " + minOf(i + 19, loans.size - 1))
                val req = SniperReq.LoanIdList()
                sublist.mapTo(req.ids) { it.ListingId }

                val j1 = Jargon()
                j1.theSrcPort = 4570
                j1.theDstPort = 4570
                j1.theMsgType = Jargon.MSG_TYPE.PPD_REQ_LOAN_STATUS
                j1.theSrcIP = InetAddress.getByName("cfig.me")
                j1.theDstIP = InetAddress.getByName(sniperProps.preFetchClients[i / 20])
                j1.theCmd = ObjectMapper().writeValueAsString(req)
                val session = msm.getSession(j1)
                if ((session != null) && session.isConnected) {
                    log.info("Req Status: ${j1.theDstIP}: ${sublist.size}")
                    session.write(j1)
                } else {
                    log.error("Can not get session again for ${j1.theDstIP}:${j1.theDstPort}")
                }
            }
        }
    }

    //called by ObjHandler
    fun requestLoanStatus(j: Jargon) {
        val msgData = ObjectMapper().readValue(j.theCmd, SniperReq.LoanIdList::class.java)
        while (sniperConfig.statusPacer.overSpeed()) {
            TimeUnit.MILLISECONDS.sleep(sniperConfig.statusPacer.napTime)
        }
        sniperConfig.statusPacer.update(System.currentTimeMillis())
        val ret = sniperStore.clientA!!.loanService.listingStatus(msgData.ids)
        if (ret != null) {
            val j1 = j.ackJargon()
            j1.theMsgType = Jargon.MSG_TYPE.PPD_RESP_LOAN_STATUS
            j1.theCmd = mapper2.writeValueAsString(ret)
            val session = msm.getSession(j1)
            if ((session != null) && session.isConnected) {
                session.write(j1)
            } else {
                log.error("Can not get session again for ${j1.theDstIP}:${j1.theDstPort}")
            }
        }
    }

    //called by ObjHandler
    fun responseLoanStatus(resp: PpdResp.ListingStatusResp) {
        val ids: MutableList<Long> = mutableListOf()
        resp.Infos.mapTo(ids) { it.ListingId }
        log.info("Recv XStatus: $ids")
        resp.Infos.forEach { info ->
            loanRepoExt.findByListingId(info.ListingId).let { loan ->
                loan.XStatus = info.Status.toShort()
                loanRepo.save(loan)
            }
        }
    }

    private val mapper2 = ObjectMapper()
    private val sniperStore: SniperStore
    private val sniperProps: SniperProps
    private val loanRepoExt: LoanRepoExt
    private val pendingRepoExt: PendingLoanRepoExt
    private val pendingRepo: PendingLoanRepo
    private val loanRepo: LoanRepo
    private val sniperConfig: SniperConfig
    private val msm: MinaSessionManager
    private val log = LoggerFactory.getLogger(HistoryLoanWorker::class.java)
}
