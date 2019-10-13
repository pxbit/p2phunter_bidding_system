package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2psniper.common.entity.PpdBid2
import cfig.p2psniper.common.entity.PpdBid2.ConsumeSrc
import cfig.p2psniper.common.entity.PpdUser
import cfig.p2psniper.common.repository.PpdBid2Repo
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BidWorker {
    companion object {
        private val log = LoggerFactory.getLogger("BidWorker")
    }

    @Autowired
    private val sniperProps: SniperProps? = null

    fun bidNB(u: PpdUser,
              listingId: Long,
              bidAmount: Int,
              strategyId: Long,
              ts2: Long,
              consumeSrc: ConsumeSrc) {
        Thread(Runnable {
            Thread.currentThread().name = "bid"
            try {
                bidInternal2(u, listingId, bidAmount, strategyId, ts2, consumeSrc)
            } catch (e: Exception) {
                LogHelper.dump("BidWorker", e)
            }
        }).start()
    }

    private fun bidInternal2(u: PpdUser,
                             listingId: Long,
                             bidAmount: Int,
                             strategyId: Long,
                             ts2: Long,
                             consumeSrc: ConsumeSrc = ConsumeSrc.DIGGER) {
        try {
            val tsBeforeBid = System.currentTimeMillis()
            //use coupon for all
            val ret = sniperStore!!.bidClient!!.bidService.bidding2(
                    u.AccessToken!!, listingId, bidAmount, true)
            if (ret != null) {
                log.info(ret.toString())
                if (!ret.OrderId.isNullOrBlank()) {
                    val bidRet2 = PpdBid2(0, ret.ListingId, ret.OrderId, ret.Amount, 0f, 0f,
                            ret.ResultMessage, 0, strategyId, u.UserId, consumeSrc.toShort(), sniperProps!!.appIndex, 0)
                    ppdBid2Repo!!.save(bidRet2)
                } else {
                    when (ret.Result) {
                        4001 -> {
                            userService!!.rmUser(u.UserId)
                            log.warn("Remove user ${u.UserId} from list due to insufficient balance")
                            HunterUserService.poorList.add(u.UserId)
                        }
                        5001 -> {
                            userService!!.rmUser(u.UserId)
                            log.warn("Remove user ${u.UserId} from list due to risk tests")
                            HunterUserService.poorList.add(u.UserId)
                        }
                    }

                    val bidRet2 = PpdBid2(0, ret.ListingId, null, ret.Amount, 0f, 0f,
                            ret.ResultMessage, 0, strategyId, u.UserId, consumeSrc.toShort(), sniperProps!!.appIndex, 1)
                    ppdBid2Repo!!.save(bidRet2)
                }
            }
            val tsAfterBid = System.currentTimeMillis()
        } catch (e: Exception) {
            LogHelper.dump(log.name, e)
        }
    }

    fun ConsumeSrc.toShort(): Short {
        return when (this) {
            ConsumeSrc.FETCHER -> 0
            ConsumeSrc.DATABASE -> 1
            ConsumeSrc.DIGGER -> 2
            ConsumeSrc.AI -> 3
        }
    }

    @Autowired
    private val sniperStore: SniperStore? = null

    @Autowired
    private val ppdBid2Repo: PpdBid2Repo? = null

    @Autowired
    private val userService: HunterUserService? = null
}
