package cfig.p2phunter.th

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.module.kotlin.readValue

class HunterAiRuleTest {
    private val log = LoggerFactory.getLogger(HunterAiRuleTest::class.java)
    /*
        [{"ListingId": 143335048, "StrategyId": 504}]
    */
    @Test
    fun testAiRuleParse() {
        val message = "[{\"ListingId\": 143335048, \"StrategyId\": 504}]"
        val msgData: List<HunterResp.AiRule> = ObjectMapper().readValue(message)
        msgData.forEach {
            log.info("ListingId ${it.ListingId} - RuleId ${it.StrategyId}")
        }

        val msgData2 = ObjectMapper().readValue<List<HunterResp.AiRule>>(message)
    }
}