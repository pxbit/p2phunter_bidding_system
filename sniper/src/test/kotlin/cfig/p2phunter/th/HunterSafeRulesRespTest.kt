package cfig.p2phunter.th

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.slf4j.LoggerFactory
import cfig.p2phunter.th.HunterResp.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/5/17
 */
class HunterSafeRulesRespTest {
    private val log = LoggerFactory.getLogger(HunterSafeRulesRespTest::class.java)

    @Test
    fun getStatus() {
        val ss = """
{"status":0,"msg":"OK","count":7,"sys":{"1":{"RateA":11,"RateB":12,"MonthsA":1,"MonthsB":2},"2":{"RateA":11,"RateB":12,"MonthsA":3,"MonthsB":4},"3":{"RateA":12,"RateB":12.5,"MonthsA":1,"MonthsB":10},"4":{"RateA":12.5,"RateB":13,"MonthsA":1,"MonthsB":19},"5":{"RateA":13,"RateB":15,"MonthsA":1,"MonthsB":25},"6":{"RateA":15,"RateB":25,"MonthsA":1,"MonthsB":37},"7":{"RateA":14,"RateB":15,"MonthsA":24,"MonthsB":37}}}
"""
        val resp = ObjectMapper().readValue(ss.trim(), HunterSafeRulesResp::class.java)
        log.info(resp.toString())
    }

    @Test
    fun tt() {
        log.info(System.currentTimeMillis().toString())
    }

}