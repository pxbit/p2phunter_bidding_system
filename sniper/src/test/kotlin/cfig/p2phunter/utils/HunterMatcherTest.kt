package cfig.p2phunter.utils

import cfig.p2phunter.th.HunterDiyRulesResp
import cfig.p2psniper.common.entity.Loan
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import java.sql.Timestamp
import java.text.SimpleDateFormat

class HunterMatcherTest {

    @Before
    fun setUp() {
    }

    @Test
    fun test1() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val rule100334 = """
            {"StrategyId":"100334","UserId":"885","StrategyLevel":"1","StrategyName":"j58-3","Description":"\u6211\u7684\u7b56\u7565\u63cf\u8ff0","AmountA":"-1","AmountB":"-1","MonthA":"-1","MonthB":"-1","RateA":"16.00","RateB":"-1.00","Credit":"126","CertificateValidate":"0","CreditValidate":"0","EducateValidate":"0","VideoValidate":"0","PhoneValidate":"1","NciicIdentityCheck":"0","Degree":"63","StudyStyle":"63","School":"15","Gender":"3","Age":"30","LastSuccessIntervalA":"-1","LastSuccessIntervalB":"-1","FirstSuccessIntervalA":"-1","FirstSuccessIntervalB":"-1","SuccessCountA":"-1","SuccessCountB":"-1","WasteCountA":"-1","WasteCountB":"-1","NormalCountA":"-1","NormalCountB":"-1","NormalSuccessRatioA":"5.00","NormalSuccessRatioB":"-1.00","DelayNormalRatioA":"-1.00","DelayNormalRatioB":"0.20","OverdueCountA":"-1","OverdueCountB":"-1","OverdueMoreCountA":"0","OverdueMoreCountB":"0","OwingAmountA":"-1.00","OwingAmountB":"-1.00","OwingPrevHighDebtRatioA":"-1.00","OwingPrevHighDebtRatioB":"0.40","OwingHighDebtRatioA":"-1.00","OwingHighDebtRatioB":"-1.00","LastHighestBorrowRatioA":"-1.00","LastHighestBorrowRatioB":"2.00","BidAmount":"58","ConditionMask":"1040187390","ValidateCode":"16","Status":"0","DayAmountLimit":"0","DayAmount":"2320","LastBidTime":"2017-10-31 22:29:04","SettingUpdateTime":"2017-10-30 18:36:45"}
            """
        val rule100334Obj = ObjectMapper().readValue(rule100334.trim(), HunterDiyRulesResp.HunterDiyRuleItem::class.java)
        println(rule100334Obj.toString())

        val loan80758440 = """
            {"age":38,"amount":7500,"amountToReceive":0,"auditingTime":"2017-10-31T00:08:18.75","borrowName":"pdu5082013524","cancelCount":0,"certificateValidate":false,"creditCode":"C","creditValidate":false,"currentRate":20,"deadLineTimeOrRemindTimeStr":"2017/10/31","educateValidate":false,"educationDegree":null,"failedCount":0,"firstSuccessBorrowTime":"2016-07-10T15:53:53.85","firstBidTime":"2017-10-31T00:07:53.33","gender":1,"graduateSchool":null,"highestDebt":7943.46,"highestPrincipal":7500,"lastBidTime":"2017-10-31T00:08:18.22","lastSuccessBorrowTime":"2017-10-31T00:02:31","lenderCount":95,"listingId":80758440,"nciicIdentityCheck":false,"normalCount":7,"overdueLessCount":0,"overdueMoreCount":0,"owingAmount":7943.46,"owingPrincipal":7500,"phoneValidate":true,"registerTime":"2016-07-10T12:40:49","remainFunding":0,"studyStyle":null,"totalPrincipal":12000,"videoValidate":false,"wasteCount":1,"successCount":2,"months":6}
            """
        val loan80758440Obj = ObjectMapper().readValue(loan80758440, Loan::class.java)
        println(loan80758440Obj.toString())

//        println(HunterMatcher.matchRule(loan80758440Obj, rule100334Obj, debug = true))
        println(HunterMatcher.matchRule2(loan80758440Obj, rule100334Obj))
    }

    @Test
    fun test2() {
        val loan1S =
                """
{"amount":2500.0,"months":12,"listingId":80954156,"firstBidTime":1509466823017,"creditCode":"C","lastBidTime":1509466825913,"lenderCount":5,"auditingTime":946656000000,"remainFunding":0.0,"deadLineTimeOrRemindTimeStr":"14天23时53分","currentRate":20.0,"borrowName":"pdu0866607000","gender":1,"educationDegree":"本科","graduateSchool":"西安理工大学高科学院","studyStyle":"普通","age":25,"successCount":0,"wasteCount":0,"cancelCount":0,"failedCount":0,"normalCount":58,"overdueLessCount":0,"overdueMoreCount":0,"owingPrincipal":9765.57,"owingAmount":10482.43,"amountToReceive":0.0,"firstSuccessBorrowTime":1461654386000,"lastSuccessBorrowTime":1506319796000,"registerTime":1461653719000,"certificateValidate":true,"nciicIdentityCheck":false,"phoneValidate":true,"videoValidate":false,"creditValidate":false,"educateValidate":false,"highestPrincipal":9000.0,"highestDebt":10956.43,"totalPrincipal":28612.0,"xfetchTime":null}
"""
        val loan1 = ObjectMapper().readValue(loan1S.trim(), Loan::class.java)

        val rule1S = """
{"StrategyId":"100548","UserId":"1260","StrategyLevel":"1","StrategyName":"\u672c50\u4e00\u6b21","Description":"2","AmountA":"-1","AmountB":"-1","MonthA":"-1","MonthB":"-1","RateA":"-1.00","RateB":"-1.00","Credit":"8","CertificateValidate":"0","CreditValidate":"0","EducateValidate":"0","VideoValidate":"0","PhoneValidate":"0","NciicIdentityCheck":"0","Degree":"7","StudyStyle":"7","School":"7","Gender":"3","Age":"31","LastSuccessIntervalA":"-1","LastSuccessIntervalB":"-1","FirstSuccessIntervalA":"-1","FirstSuccessIntervalB":"-1","SuccessCountA":"-1","SuccessCountB":"-1","WasteCountA":"-1","WasteCountB":"-1","NormalCountA":"50","NormalCountB":"300","NormalSuccessRatioA":"-1.00","NormalSuccessRatioB":"-1.00","DelayNormalRatioA":"-1.00","DelayNormalRatioB":"-1.00","OverdueCountA":"0","OverdueCountB":"1","OverdueMoreCountA":"0","OverdueMoreCountB":"0","OwingAmountA":"-1.00","OwingAmountB":"-1.00","OwingPrevHighDebtRatioA":"0.00","OwingPrevHighDebtRatioB":"1.00","OwingHighDebtRatioA":"-1.00","OwingHighDebtRatioB":"-1.00","LastHighestBorrowRatioA":"-1.00","LastHighestBorrowRatioB":"-1.00","BidAmount":"52","ConditionMask":"1069081480","ValidateCode":"0","Status":"0","DayAmountLimit":"0","DayAmount":"156","LastBidTime":"2017-10-31 23:58:25","SettingUpdateTime":"2017-10-31 07:58:57"}
"""
        val rule1 = ObjectMapper().readValue(rule1S.trim(), HunterDiyRulesResp.HunterDiyRuleItem::class.java)
        println(HunterMatcher.matchRule2(loan1, rule1))
    }

    fun test3() {
        val r100333 = """{"StrategyId":"100333","UserId":"885","StrategyLevel":"1","StrategyName":"j58-2","Description":"\u6211\u7684\u7b56\u7565\u63cf\u8ff0","AmountA":"-1","AmountB":"-1","MonthA":"-1","MonthB":"-1","RateA":"16.00","RateB":"-1.00","Credit":"126","CertificateValidate":"0","CreditValidate":"0","EducateValidate":"0","VideoValidate":"0","PhoneValidate":"1","NciicIdentityCheck":"0","Degree":"63","StudyStyle":"63","School":"15","Gender":"3","Age":"30","LastSuccessIntervalA":"-1","LastSuccessIntervalB":"-1","FirstSuccessIntervalA":"-1","FirstSuccessIntervalB":"-1","SuccessCountA":"-1","SuccessCountB":"-1","WasteCountA":"-1","WasteCountB":"-1","NormalCountA":"-1","NormalCountB":"-1","NormalSuccessRatioA":"5.00","NormalSuccessRatioB":"-1.00","DelayNormalRatioA":"-1.00","DelayNormalRatioB":"0.20","OverdueCountA":"-1","OverdueCountB":"-1","OverdueMoreCountA":"0","OverdueMoreCountB":"0","OwingAmountA":"-1.00","OwingAmountB":"-1.00","OwingPrevHighDebtRatioA":"-1.00","OwingPrevHighDebtRatioB":"0.40","OwingHighDebtRatioA":"-1.00","OwingHighDebtRatioB":"-1.00","LastHighestBorrowRatioA":"-1.00","LastHighestBorrowRatioB":"2.00","BidAmount":"58","ConditionMask":"1040187390","ValidateCode":"16","Status":"0","DayAmountLimit":"0","DayAmount":"58","LastBidTime":"2017-11-01 00:10:50","SettingUpdateTime":"2017-10-30 18:35:54"}
"""
        val r100333Obj = ObjectMapper().readValue(r100333.trim(), HunterDiyRulesResp.HunterDiyRuleItem::class.java)

        val loan1S = """
            {"amount":7343.0,"months":6,"listingId":80950559,"firstBidTime":1509466373677,"creditCode":"B","lastBidTime":1509466377013,"lenderCount":9,"auditingTime":946656000000,"remainFunding":0.0,"deadLineTimeOrRemindTimeStr":"19天23时28分","currentRate":18.0,"borrowName":"pdu5152045180","gender":1,"educationDegree":"专科","graduateSchool":"东莞南博职业技术学院","studyStyle":"成人","age":30,"successCount":0,"wasteCount":1,"cancelCount":0,"failedCount":0,"normalCount":28,"overdueLessCount":0,"overdueMoreCount":0,"owingPrincipal":756.2,"owingAmount":767.57,"amountToReceive":0.0,"firstSuccessBorrowTime":1484763597000,"lastSuccessBorrowTime":1498213083000,"registerTime":1484600581000,"certificateValidate":true,"nciicIdentityCheck":false,"phoneValidate":true,"videoValidate":false,"creditValidate":true,"educateValidate":false,"highestPrincipal":5500.0,"highestDebt":8488.9,"totalPrincipal":14160.0,"xfetchTime":null}
"""
        val loan1 = ObjectMapper().readValue(loan1S.trim(), Loan::class.java)
        println(HunterMatcher.matchRule2(loan1, r100333Obj))
    }
}
