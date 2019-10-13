package cfig.p2phunter.component

import cfig.p2phunter.th.HunterResp
import cfig.p2psniper.common.entity.Loan
import cfig.p2psniper.common.entity.PpdBid2
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class AiConsumer {
    fun consumeAiResults(aiRule: HunterResp.AiRule) {
        //start bidding now
        val users = userService!!.getUsersCheap()
        val userAiRules = userService.getAiRulesCheap()
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

        //read back info from database
        val aLoan: Loan
        try {
            aLoan = loanRepo!!.findById(aiRule.ListingId).get()
        } catch (e: NoSuchElementException) {
            log.error("Can not find loan ${aiRule.ListingId}")
            LogHelper.dump("AiConsumer", e)
            return
        }

        for (uid in userList) {
            val u = users[uid]
            if (u != null && userAiRules[uid] != null) {
                for (aRule in userAiRules[uid]!!) {
                    if (creditLoanConsumer!!.matchGlobalSetting(u, aLoan, aRule.BidAmount)) {
                        try {
                            if (aRule.StrategyId == aiRule.StrategyId) {
                                log.info("Match AI: u=$uid, rule=${aRule.StrategyId}, loan=${aLoan.ListingId}")
                                bidWorker!!.bidNB(u, aLoan.ListingId, aRule.BidAmount, aRule.StrategyId,
                                        System.currentTimeMillis(), PpdBid2.ConsumeSrc.AI)
                                break //out of rules of current user
                            }
                        } catch (e: Exception) {
                            userService.rmUser(uid)
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

    @Autowired
    private val userService: HunterUserService? = null

    @Autowired
    private val creditLoanConsumer: CreditLoanConsumer? = null

    @Autowired
    private val loanRepo: LoanRepo? = null

    @Autowired
    private val bidWorker: BidWorker? = null

    companion object {
        private val log = LoggerFactory.getLogger("AiConsumer")
    }
}