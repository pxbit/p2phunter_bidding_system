package cfig.p2phunter.sniper

import cfig.p2phunter.component.HunterUtils
import cfig.p2phunter.component.DetailFetcher
import cfig.p2phunter.th.HunterCreditRulesResp.CreditRuleItem
import cfig.p2psniper.common.repository.LoanRepo
import cfig.p2psniper.common.repository.LoanRepoExt
import cfig.p2psniper.common.repository.PendingLoanRepoExt
import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.nio.file.Files
import java.nio.file.Paths

@RunWith(SpringRunner::class)
@SpringBootTest
class SniperApplicationTests {
    @Autowired
    private val hU: HunterUtils? = null

    @Autowired
    private val loanRepo: LoanRepo? = null

    @Autowired
    private val loanRepoExt: LoanRepoExt? = null

    @Autowired
    private val detailFetcher: DetailFetcher? = null

    @Autowired
    private val pendingRepoExt: PendingLoanRepoExt? = null

    @Test
    fun getRecentIds() {
        println(pendingRepoExt!!.findRecentIds(10))
    }

    @Test
    fun getUserInfo() {
        val users = hU!!.getUserInfo(1)
        users.forEach { println(it.toMiniString()) }
    }

    @Test
    fun getDiyRules() {
        hU!!.getDiyRules(1).forEach {
            println(it.toJson())
        }
    }

    @Test
    fun getUserSettings() {
        hU!!.getUserSettings(1).forEach {
            if (it.StrategyId in 1..7) {
                println(it)
            } else if (it.StrategyId >= 1000 && it.StrategyId <= 1010) {
                println(it)
            } else {
                println("new: $it")
            }
        }
    }

    @Test
    fun getSysCreditRules() {
        hU!!.getSysCreditRules(1).forEach {
            println(it.toString())
        }
    }

    @Test
    fun getAArules() {
        hU!!.getAARules(0).let { it ->
            it!!.sys.forEach { t, u ->
                println("$t : $u")
            }
        }
    }

    @Test
    fun testSendBid() {
        val bidRecords = mutableListOf(
                HunterUtils.BidRecordItem(95, 12345678, 1000103, 1f)
        )
        assertTrue(hU!!.sendBidRecord(bidRecords))
    }

    @Test
    fun testGetVIPList() {
        hU!!.getVIPList(0)
    }

    @Test
    fun test2() {
        val rule29str = String(Files.readAllBytes(Paths.get("/Users/yu/work/bitbucket/p2psniper_kt/sniper/rule29.json"))).trim()
        val rule29 = ObjectMapper().readValue(rule29str, CreditRuleItem::class.java)
        println(rule29.toString())
    }
}
