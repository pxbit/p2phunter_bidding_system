package cfig.p2phunter.th

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import org.junit.Assert.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/5/17
 */
class HunterDiyRulesRespTest {
    @Test
    fun getStrategyId() {
val s1 = """

{"status":0,"msg":"OK","count":1,"diy":[{"StrategyId":"100002","UserId":"312","StrategyLevel":"0","StrategyName":"z2_1","Description":"\u6211\u7684\u7b56\u7565\u63cf\u8ff0","AmountA":"100","AmountB":"9500","MonthA":"1","MonthB":"12","RateA":"18.00","RateB":"24.00","Credit":"30","CertificateValidate":"1","CreditValidate":"0","EducateValidate":"1","VideoValidate":"0","PhoneValidate":"0","NciicIdentityCheck":"0","Degree":"7","StudyStyle":"31","School":"3","Gender":"3","Age":"14","LastSuccessIntervalA":"31","LastSuccessIntervalB":"160","FirstSuccessIntervalA":"-1","FirstSuccessIntervalB":"-1","SuccessCountA":"-1","SuccessCountB":"-1","WasteCountA":"-1","WasteCountB":"-1","NormalCountA":"-1","NormalCountB":"-1","NormalSuccessRatioA":"-1.00","NormalSuccessRatioB":"-1.00","DelayNormalRatioA":"0.00","DelayNormalRatioB":"0.10","OverdueCountA":"0","OverdueCountB":"2","OverdueMoreCountA":"0","OverdueMoreCountB":"0","OwingAmountA":"-1.00","OwingAmountB":"-1.00","OwingPrevHighDebtRatioA":"-1.00","OwingPrevHighDebtRatioB":"-1.00","OwingHighDebtRatioA":"0.00","OwingHighDebtRatioB":"1.00","LastHighestBorrowRatioA":"-1.00","LastHighestBorrowRatioB":"-1.00","BidAmount":"51","ConditionMask":"496755614","ValidateCode":"5","Status":"0","DayAmountLimit":"200","DayAmount":"0","LastBidTime":null,"SettingUpdateTime":"2017-10-17 10:46:03","CertificateValidateFalse":"0","CreditValidateFalse":"0","NciicIdentityCheckFalse":"0","PhoneValidateFalse":"0","AgeRangeA":"-1","AgeRangeB":"-1","AvgBorrowIntervalA":"-1","AvgBorrowIntervalB":"-1","CurAvgIntervalRatioA":"-1","CurAvgIntervalRatioB":"-1","RegisterFirstIntervalA":"-1","RegisterFirstIntervalB":"-1","RegisterMonthA":"-1","RegisterMonthB":"-1","OwingAfterAmountA":"-1","OwingAfterAmountB":"-1","TotalBorrowA":"-1","TotalBorrowB":"-1","OwnPreTotalBorrowRatioA":"-1","OwnPreTotalBorrowRatioB":"-1","CurAmountTotalBorrowRatioA":"-1","CurAmountTotalBorrowRatioB":"-1","AvgBorrowAmountA":"-1","AvgBorrowAmountB":"-1","CurAmountAvgBorrowRatioA":"-1","CurAmountAvgBorrowRatioB":"-1","ValidateCodeFalse":"0"}]}

""".trimIndent()
        val ss1 = ObjectMapper().readValue(s1.trim(), HunterDiyRulesResp::class.java)
        //val ss2 = ObjectMapper().readValue(s2.trim(), HunterDiyRulesResp::class.java)
        //println(ss2.toString())
    }
}
