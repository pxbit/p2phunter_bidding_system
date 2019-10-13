package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2psniper.common.entity.CustomAARule
import cfig.p2psniper.common.entity.PpdBid2.ConsumeSrc
import cfig.p2psniper.common.entity.PpdUser
import cfig.p2psniper.common.repository.CustomAARuleRepo
import cfig.p2psniper.common.repository.PpdUserRepo
import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.params.PpdResp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class SafeLoanConsumer {
    private val userService: HunterUserService
    private val genericMaxCount = 15f

    @Autowired
    private val ppdUserRepo: PpdUserRepo? = null

    @Autowired
    private val customAARuleRepo: CustomAARuleRepo? = null

    constructor(uS: HunterUserService) {
        userService = uS
    }

    fun consumeNB(listings: List<PpdResp.LoanListResp.SingleListingResp>) {
        if (!sniperProps!!.bidEnable) {
            return
        }
        Thread(Runnable {
            try {
                if (sniperProps.isStandAlone()) {
                    consumeStandalone(listings)
                } else {
                    consumeInternal(listings)
                }
            } catch (e: Exception) {
                LogHelper.dump("SafeLoanConsumer", e)
            }
        }).start()
    }

    private fun consumeStandalone(listings: List<PpdResp.LoanListResp.SingleListingResp>) {
        listings.filter { it.CreditCode == "AA" }
                .forEach { listing ->
                    ppdUserRepo!!.findAll().forEach { user ->
                        customAARuleRepo!!.findByPpdUser(user)!!.forEach {
                            if (listing.matchCustomAARule(it)) {
                                bidWorker!!.bidNB(user, listing.ListingId, it.amount, it.id,
                                        System.currentTimeMillis(), ConsumeSrc.FETCHER)
                                log.info("Bid AA loan: ${listing.ListingId} for user ${user.UserName}")
                            }
                        }
                    }
                }
    }

    private fun consumeInternal(listings: List<PpdResp.LoanListResp.SingleListingResp>) {
        //start bidding now
        val users = userService.getUsersCheap()
        val userAARules = userService.getUserSysAARulesCheap()

        //kotlin 1.2
        val userList: LinkedList<Int> = LinkedList()
        userList += users.keys
        userList.shuffle()
        //VIP CHANNEL
        userService.getVIPListCheap().forEach {
            if (userList.contains(it)) {
                userList.remove(it)
                userList.addFirst(it)
            }
        }

        listings
                .filter { it.CreditCode == "AA" && it.RemainFunding > 0 }
                .forEach { loanRespItem ->
                    var thisMaxCount = loanRespItem.RemainFunding / 50
                    if (thisMaxCount < 1) { //low remain funding
                        thisMaxCount = 2.0f
                    }
                    thisMaxCount = minOf(thisMaxCount, genericMaxCount)
                    var bidCount = 0
                    for (uid in userList) {
                        if (bidCount > thisMaxCount) { //next loan
                            log.warn("Loan " + loanRespItem.ListingId + " has been tried, $bidCount > min($genericMaxCount, $thisMaxCount)")
                            break
                        }
                        val u = users[uid]
                        if (u != null) {
                            //current user rules
                            if (userAARules[uid] != null) {
                                for (ruleItem in userAARules[uid]!!) {
                                    if (matchGlobalSettingAA(u, loanRespItem, ruleItem.amount)) {
                                        //global setting match
                                        if (loanRespItem.matchCustomAARule(ruleItem)) {
                                            //bid
                                            log.info("Bid AA for user $uid rule ${ruleItem.id}, loan ${loanRespItem.ListingId}")
                                            bidWorker!!.bidNB(u, loanRespItem.ListingId, ruleItem.amount, ruleItem.id,
                                                    System.currentTimeMillis(), ConsumeSrc.FETCHER)
                                            bidCount++
                                            break //break out of current user's rules
                                        }
                                    } else {
                                        //global setting mismatch
                                    }
                                }//user-rules
                            } else {
                                //user doesn't enable AA rule
                            }
                        } else {
                            //user is null
                        }
                    }//users
                }//listings
    }

    fun PpdResp.LoanListResp.SingleListingResp.matchCustomAARule(rule: CustomAARule): Boolean {
        if (this.Rate < rule.minRate) {
            return false
        }
        if (this.Rate > rule.maxRate) {
            return false
        }
        if (this.Months < rule.minMonths) {
            return false
        }
        if (this.Months > rule.maxMonths) {
            return false
        }

        return true
    }

    fun MutableList<Int>.shuffle() {
        java.util.Collections.shuffle(this)
    }

    private fun matchGlobalSettingAA(u: PpdUser, loan: PpdResp.LoanListResp.SingleListingResp, bidAmount: Int): Boolean {
//        if (u.MinRate != null && u.MinRate!! > loan.Rate) {
//            log.info("matchGlobalSetting: minRate: u.min=${u.MinRate}, loan.rate=${loan.Rate}")
//            return false
//        }
//        if (u.MinMonth != null && u.MinMonth!! > loan.Months) {
//            log.info("matchGlobalSetting: minMonth: u.minMonth=${u.MinMonth}, loan.month=${loan.Months}")
//            return false
//        }
//
//        if (u.MaxMonth != null && u.MaxMonth!! < loan.Months) {
//            log.info("matchGlobalSetting: maxMonth: u.maxMonth=${u.MaxMonth}, loan.month=${loan.Months}")
//            return false
//        }

        if (u.holdBalance != null && u.holdBalance!! >= u.UserBalance) {
            userService.rmUser(u.UserId)
            log.info("Below holding balance: uid=" + u.UserId)
            return false
        }

        if (u.Score <= bidAmount * 10) {//5000 hunter coin, 0.5 RMB
            userService.rmUser(u.UserId)
            log.info("Low P2PHUNTER Deposit: uid=" + u.UserId)
            return false
        }

        /* FIXME: speed
        if (u.UserBalance <= bidAmount) {
            userService.rmUser(u.UserId)
            log.info("Low BALANCE Deposit: uid=" + u.UserId)
            return false
        }
        */

        return true
    }

    @Autowired
    private val bidWorker: BidWorker? = null

    @Autowired
    private val sniperProps: SniperProps? = null

    companion object {
        private val log = LoggerFactory.getLogger("SafeLoanConsumer")
    }
}
