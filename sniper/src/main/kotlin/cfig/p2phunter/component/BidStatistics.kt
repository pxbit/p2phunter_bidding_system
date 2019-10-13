package cfig.p2phunter.component

import cfig.p2phunter.utils.HunterMatcher
import cfig.p2phunter.utils.SystemRuleMatcher
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

@Component
class BidStatistics {
    private val log = LoggerFactory.getLogger(BidStatistics::class.java)
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    @Autowired
    private val loanRepo: LoanRepo? = null

    @Autowired
    private val loanRepoExt: LoanRepoExt? = null

    @Autowired
    private val userService: HunterUserService? = null

    @PersistenceContext
    private val em: EntityManager? = null

    init {
    }

    fun calcBid(dt: Date?): Unit {
        log.info("calcBid: start")
        val today = sdf1.parse(sdf1.format(Date()))
        val yesterday = Date(today.time - 86400000L)
        val y2day = Date(yesterday.time - 86400000L)
        val tomorrow = Date(today.time + 86400000L)
        try {
            //loan = em!!.createQuery("SELECT c FROM Loan c WHERE c.ListingId = :id").setParameter("id", listingId).getSingleResult() as Loan

            //the day before yesterday
            calcBetween(y2day, yesterday)
            //yesterday
            calcBetween(yesterday, today)
            //today
            calcBetween(today, tomorrow)
        } catch (e: Exception) {
            log.error("Unexpected Exception: " + e.message)
        }
    }

    private fun bidSuccess(listingId: Long): Boolean {
        val bidRet = em!!.createQuery("SELECT b FROM PpdBid b where b.listingId = :listingid and b.investId != 0")
                .setParameter("listingid", listingId)
                .resultList
        return bidRet != null && bidRet.size > 0
    }

    fun calcBetween(dateFrom: Date, dateUntil: Date) {
        var queryTotal: Query = em!!.createQuery("SELECT count(c.ListingId) FROM Loan c where c.XFetchTime > :t1 and c.XFetchTime < :t2")
                .setParameter("t1", dateFrom)
                .setParameter("t2", dateUntil)
        val count: Long = queryTotal.singleResult as Long

        val l2 = em!!.createQuery("SELECT c.ListingId FROM Loan c where c.XFetchTime > :t1 and c.XFetchTime < :t2")
                .setParameter("t1", dateFrom)
                .setParameter("t2", dateUntil)
                .resultList as List<Long>
        var cnt2 = 0 //system rule match total
        var cnt22 = 0 //system rule bid total
        var cnt3 = 0 //user rule match total
        var cnt33 = 0 //user rule bid total

        val users = userService!!.getUsersCheap()
        val userRules = userService!!.getUserDiyRulesCheap()
        var userList: MutableList<Int> = users.keys.toMutableList()

        for (item in l2) {
            val loanItem = loanRepoExt!!.findByListingId(item)
            //system rule
            if (SystemRuleMatcher.matchAny(loanItem)) {
                cnt2++
                if (bidSuccess(item)) {
                    cnt22++
                }
            } else {
                //user rule (only)
                for (uid in userList) {
                    val u = users[uid]
                    if (u != null && userRules[uid] != null) {
                        for (aRule in userRules[uid]!!) {
                            if (HunterMatcher.matchRule2(loanItem, aRule)) {
                                //bingo
                                cnt3++
                                if (bidSuccess(item)) {
                                    cnt33++
                                }
                                break //out of rules of current user
                            }
                        }
                    } else {
                        //user null or rules null
                    }
                }
            }
        }
        val prefix = "[" + sdf1.format(dateFrom) + ", " + sdf1.format(dateUntil) + ")"
        log.info(prefix + ": Total $count")
        log.info("$prefix: Match(system): $cnt2, Bid $cnt22")
        log.info("$prefix: Match(user)  : $cnt3, Bid $cnt33")
    }
}
/*
val t1s = "2018-1-2"
val t1 = sdf1.parse(t1s)
println(t1)
*/
