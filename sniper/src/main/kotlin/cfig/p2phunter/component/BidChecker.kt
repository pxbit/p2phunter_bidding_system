package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2psniper.common.repository.PpdBid2Repo
import cfig.p2psniper.common.repository.PpdBid2RepoExt
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import cfig.p2psniper.common.entity.PpdUser
import cfig.p2psniper.common.repository.PpdUserRepo
import cfig.p2psniper.common.repository.PpdUserRepoExt
import cfig.p2psniper.ppdsdk.params.PpdResp
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException

@Component
class BidChecker {
    private val log = LoggerFactory.getLogger(BidChecker::class.java)

    @Autowired
    private val ppdBid2Repo: PpdBid2Repo? = null

    @Autowired
    private val ppdBid2RepoExt: PpdBid2RepoExt? = null

    @Autowired
    private val sniperStore: SniperStore? = null

    @Autowired
    private val hU: HunterUtils? = null

    private val sniperProps: SniperProps
    private val ppdUserRepo: PpdUserRepo
    private val ppdUserRepoExt: PpdUserRepoExt

    constructor(sP: SniperProps, pr: PpdUserRepo, prt: PpdUserRepoExt) {
        sniperProps = sP
        ppdUserRepo = pr
        ppdUserRepoExt = prt
        Thread(Runnable {
            while (true) {
                TimeUnit.SECONDS.sleep(30)
                try {
                    queryOnce()
                } catch (e: Throwable) {
                    LogHelper.dump(log.name, e)
                }
            }
        }).start()
    }

    private fun queryOnce() {
        val ids = ppdBid2RepoExt!!.findPendingOrders()
        log.info(ids.toString())
        for (item in ids) {
            val bid = ppdBid2Repo!!.findById(item).get()
            val u: PpdUser? = ppdUserRepoExt.findByUserId(bid.userId)
            if (u == null) {
                log.warn("[db]user ${bid.userId} is null")
                continue
            }
            if (bid.orderId.isNullOrBlank() || u.AccessToken.isNullOrBlank()) {
                log.error("order id or accessToken empty")
                continue
            }
            val qRet: PpdResp.BidQueryResp2?
            //if instanceOf BidQueryResp2, go on; else continue
            val internalRet = sniperStore!!.bidClient!!.bidService
                    .queryBiddingResult(u.AccessToken!!, bid.listingId, bid.orderId!!)
                    as? PpdResp.BidQueryResp2 ?: continue
            qRet = internalRet
            log.info(qRet.toString())

            bid.message = qRet!!.resultMessage
            when (qRet.result) {
                1 -> {
                    bid.checked = 1
                    bid.code = qRet.result
                    bid.message = qRet.resultMessage
                    bid.amount = qRet.resultContent.bidAmount
                    bid.eAmount = qRet.resultContent.participationAmount
                    bid.couponAmount = qRet.resultContent.succCouponAmount
                    bid.bidId = qRet.resultContent.bidId

                    val bidRecords = mutableListOf(
                            HunterUtils.BidRecordItem(u.UserId, bid.ruleId!!, bid.listingId, bid.eAmount, bid.appIndex)
                    )
                    log.info("BidSuccess: $bidRecords")
                    if (!sniperProps.isStandAlone()) {
                        hU!!.sendBidRecord(bidRecords)
                    }
                    ppdBid2Repo.save(bid)
                }
                2 -> {
                    log.info("Will check u=${bid.userId}/listing=${bid.listingId} later")
                }
                else -> {
                    bid.checked = 1
                    bid.code = qRet.result
                    bid.message = qRet.resultMessage
                    bid.amount = qRet.resultContent.bidAmount
                    bid.eAmount = qRet.resultContent.participationAmount
                    bid.couponAmount = qRet.resultContent.succCouponAmount
                    bid.bidId = qRet.resultContent.bidId
                    ppdBid2Repo.save(bid)
                }
            }
        }
    }
}
