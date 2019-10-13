package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.HunterResp.HunterUserSettingResp.HunterUserSettingItem
import cfig.p2phunter.th.HunterDiyRulesResp.HunterDiyRuleItem
import cfig.p2phunter.utils.HunterMatcher
import cfig.p2phunter.utils.SystemRuleMatcher
import cfig.p2psniper.common.entity.Loan
import cfig.p2psniper.common.entity.PpdBid2
import cfig.p2psniper.common.entity.PpdBid2.ConsumeSrc
import cfig.p2psniper.common.entity.PpdUser
import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.params.PpdResp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreditLoanConsumer {
    private val genericMaxCount = 15

    fun MutableList<Int>.shuffle() {
        java.util.Collections.shuffle(this)
    }

    //digAnt ->
    fun consumeNB(loanInfoResp: PpdResp.LoanInfoResp, tsAfterDig: Long) {
        if (!sniperProps!!.bidEnable) {
            return
        }
        Thread(Runnable {
            try {
                consumeInternal(loanInfoResp, tsAfterDig)
            } catch (e: Exception) {
                LogHelper.dump("CreditLoanConsumer", e)
            }
        }).start()
    }

    //consume directly from db
    fun consumeNB(inLoan: Loan, tsAfterDig: Long) {
        if (!sniperProps!!.bidEnable) {
            return
        }
        Thread(Runnable {
            try {
                consumeInternal(inLoan, tsAfterDig, ConsumeSrc.DATABASE)
            } catch (e: Exception) {
                LogHelper.dump("CreditLoanConsumer", e)
            }
        }).start()
    }

    //called from consume_from_db
    private fun consumeInternal(inLoan: Loan, tsAfterDig: Long, consumeSrc: ConsumeSrc = ConsumeSrc.DIGGER) {
        if (inLoan.CreditCode == "AA") {
            return
        }
        //start bidding now
        val users = userService!!.getUsersCheap()
        val userSysRules = userService.getUserSysCreditRulesCheap()
        val userRules = userService.getUserDiyRulesCheap()
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

        var biddingList: MutableList<BidInfo> = mutableListOf()

        val r1 = Runnable { consumeLoanWithSysRule(inLoan, tsAfterDig, userList, users, userSysRules, consumeSrc, biddingList) }
        val r2 = Runnable { consumeLoanWithDiyRule(inLoan, tsAfterDig, userList, users, userRules, consumeSrc, biddingList) }
        if (sniperProps!!.useBiddingQueue) {//using bidding queue
            r1.run()
            r2.run()
            //sorting
            biddingList.sortByDescending { it.bidAmount }
            biddingList.forEach {
                bidWorker!!.bidNB(it.u, it.listingId, it.bidAmount, it.strategyId, tsAfterDig, consumeSrc)
            }
        } else {
            Thread(r1).start()
            Thread(r2).start()
        }
    }

    data class BidInfo(
            var u: PpdUser,
            var listingId: Long,
            var bidAmount: Int,
            var strategyId: Long,
            var ts2: Long,
            var consumeSrc: ConsumeSrc
    )

    private fun consumeInternal(detailResp: PpdResp.LoanInfoResp, tsAfterDig: Long) {
        if (null == detailResp.LoanInfos) {
            log.warn("Nothing to consume")
            return
        }
        //start bidding now
        val users = userService!!.getUsersCheap()
        val userSysRules = userService.getUserSysCreditRulesCheap()
        val userRules = userService.getUserDiyRulesCheap()
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

        detailResp.LoanInfos!!
                .filter { it.CreditCode != "AA" }
                .forEach { item ->
                    var biddingList: MutableList<BidInfo> = mutableListOf()
                    val aLoan = HunterMatcher.loanDetailItem2Loan(item)

                    val r1 = Runnable { consumeLoanWithSysRule(aLoan, tsAfterDig, userList, users, userSysRules, PpdBid2.ConsumeSrc.DIGGER, biddingList) }
                    val r2 = Runnable { consumeLoanWithDiyRule(aLoan, tsAfterDig, userList, users, userRules, PpdBid2.ConsumeSrc.DIGGER, biddingList) }
                    if (sniperProps!!.useBiddingQueue) {//using bidding queue
                        r1.run()
                        r2.run()
                        //sorting
                        biddingList.sortByDescending { it.bidAmount }
                        biddingList.forEach {
                            bidWorker!!.bidNB(it.u, it.listingId, it.bidAmount, it.strategyId, tsAfterDig, PpdBid2.ConsumeSrc.DIGGER)
                        }
                    } else {
                        Thread(r1).start()
                        Thread(r2).start()
                    }
                }
        //start bidding now -- END
    }

    private fun consumeLoanWithSysRule(aLoan: Loan,
                                       tsAfterDig: Long,
                                       userList: LinkedList<Int>,
                                       users: HashMap<Int, PpdUser>,
                                       userSysRules: HashMap<Int, MutableList<HunterUserSettingItem>>,
                                       consumeSrc: PpdBid2.ConsumeSrc = PpdBid2.ConsumeSrc.DIGGER,
                                       biddingList: MutableList<BidInfo>) {
        var thisMaxCount = aLoan.RemainFunding / 50
        if (thisMaxCount < 1) {
            log.debug("loan ${aLoan.ListingId} has low remain funding ${aLoan.RemainFunding}")
            thisMaxCount = 1.0f
        }
        var bidCount = 0
        for (uid in userList) {
            if (bidCount > genericMaxCount || bidCount > thisMaxCount) {
                log.warn("Loan " + aLoan.ListingId + " has been tried, $bidCount > min($genericMaxCount, $thisMaxCount)")
                break
            }
            val u = users[uid]
            if (u != null && userSysRules[uid] != null) {
                for (aRule in userSysRules[uid]!!) {
                    if (matchGlobalSetting(u, aLoan, aRule.BidAmount)) {
                        try {
                            if (SystemRuleMatcher.matchRule(aLoan, aRule.StrategyId)) {
                                if (sniperProps!!.useBiddingQueue) {
                                    biddingList.add(BidInfo(u, aLoan.ListingId, aRule.BidAmount, aRule.StrategyId, tsAfterDig, consumeSrc))
                                } else {
                                    log.info("Match: u=$uid, rule=${aRule.StrategyId}, loan=${aLoan.ListingId}")
                                    bidWorker!!.bidNB(u, aLoan.ListingId, aRule.BidAmount, aRule.StrategyId, tsAfterDig, consumeSrc)
                                }
                                bidCount++
                                break //out of rules of current user
                            }
                        } catch (e: Exception) {
                            userService!!.rmUser(uid)
                            log.error("Remove user $uid from list")
                            LogHelper.dump("Bid", e)
                        }
                    } else {
                        //global setting mismatch
                    }
                }
            } else {
                //user null or rules null
            }
        }
    }

    fun matchGlobalSetting(u: PpdUser, loan: Loan, bidAmount: Int): Boolean {
        if (u.MinRate != null && u.MinRate!! > loan.CurrentRate) {
            //log.info("matchGlobalSetting: minRate: u.min=${u.MinRate}, loan.rate=${loan.CurrentRate}")
            return false
        }
        if (u.MinMonth != null && u.MinMonth!! > loan.Months) {
            //log.info("matchGlobalSetting: minMonth: u.minMonth=${u.MinMonth}, loan.month=${loan.Months}")
            return false
        }
        if (u.MaxMonth != null && u.MaxMonth!! < loan.Months) {
            //log.info("matchGlobalSetting: maxMonth: u.maxMonth=${u.MaxMonth}, loan.month=${loan.Months}")
            return false
        }

        if (u.holdBalance != null && u.holdBalance!! >= u.UserBalance) {
            userService!!.rmUser(u.UserId)
            log.info("matchGlobalSetting: Below holding balance: uid=" + u.UserId)
            return false
        }

        if (u.Score <= bidAmount * 10) {//5000 hunter coin, 0.5 RMB
            userService!!.rmUser(u.UserId)
            log.info("matchGlobalSetting: Low P2PHUNTER coin: uid=" + u.UserId)
            return false
        }

        //FIXME: donot care low balance
        /*
        if (u.UserBalance <= bidAmount) {
            userService!!.rmUser(u.UserId)
            log.info("Low BALANCE Deposit: uid=" + u.UserId)
            return false
        }
        */
        //FIXME

        return true
    }

    private fun consumeLoanWithDiyRule(aLoan: Loan,
                                       tsAfterDig: Long,
                                       userList: LinkedList<Int>,
                                       users: HashMap<Int, PpdUser>,
                                       userRules: HashMap<Int, MutableList<HunterDiyRuleItem>>,
                                       consumeSrc: PpdBid2.ConsumeSrc = ConsumeSrc.DIGGER,
                                       biddingList: MutableList<BidInfo>) {
        for (uid in userList) {
            val u = users[uid]
            if (u != null && userRules[uid] != null) {
                for (aRule in userRules[uid]!!) {
                    try {
                        if (matchGlobalSetting(u, aLoan, aRule.BidAmount)) {
                            if (HunterMatcher.matchRule2(aLoan, aRule)) {
                                if (sniperProps!!.useBiddingQueue) {
                                    biddingList.add(BidInfo(u, aLoan.ListingId, aRule.BidAmount, aRule.StrategyId, tsAfterDig, consumeSrc))
                                } else {
                                    bidWorker!!.bidNB(u, aLoan.ListingId, aRule.BidAmount, aRule.StrategyId, tsAfterDig, consumeSrc)
                                    log.info("Match: u=$uid, rule=${aRule.StrategyId}, loan=${aLoan.ListingId}")
                                }
                                break //out of rules of current user
                            }
                        } else {
                            //global settings mismatch
                        }
                    } catch (e: Exception) {
                        userService!!.rmUser(uid)
                        log.warn("Remove user $uid from list")
                        LogHelper.dump("Bid", e)
                    }
                }
            } else {
                //user null or rules null
            }
        }
    }

    @Autowired
    private val bidWorker: BidWorker? = null

    @Autowired
    private val sniperProps: SniperProps? = null

    @Autowired
    private val userService: HunterUserService? = null

    companion object {
        private val log = LoggerFactory.getLogger("CreditLoanConsumer")
    }
}
