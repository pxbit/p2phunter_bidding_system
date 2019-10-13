package cfig.p2phunter.utils

import cfig.p2phunter.th.HunterDiyRulesResp
import cfig.p2psniper.common.entity.Loan
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.junit.Assert.*

class HunterMatcherCoverageTest {
    private lateinit var aRule: HunterDiyRulesResp.HunterDiyRuleItem
    private lateinit var aLoan: Loan
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Before
    fun setUp() {
        val aRuleStr = """
            {
              "StrategyId": "100333",
              "UserId": "885",
              "StrategyLevel": "1",
              "StrategyName": "j58-2",
              "Description": "我的策略描述",
              "AmountA": "-1",
              "AmountB": "-1",
              "MonthA": "-1",
              "MonthB": "-1",
              "RateA": "16.00",
              "RateB": "-1.00",
              "Credit": "126",
              "CertificateValidate": "0",
              "CreditValidate": "0",
              "EducateValidate": "0",
              "VideoValidate": "0",
              "PhoneValidate": "1",
              "NciicIdentityCheck": "0",
              "Degree": "63",
              "StudyStyle": "63",
              "School": "15",
              "Gender": "3",
              "Age": "30",
              "LastSuccessIntervalA": "-1",
              "LastSuccessIntervalB": "-1",
              "FirstSuccessIntervalA": "-1",
              "FirstSuccessIntervalB": "-1",
              "SuccessCountA": "-1",
              "SuccessCountB": "-1",
              "WasteCountA": "-1",
              "WasteCountB": "-1",
              "NormalCountA": "-1",
              "NormalCountB": "-1",
              "NormalSuccessRatioA": "5.00",
              "NormalSuccessRatioB": "-1.00",
              "DelayNormalRatioA": "-1.00",
              "DelayNormalRatioB": "0.20",
              "OverdueCountA": "-1",
              "OverdueCountB": "-1",
              "OverdueMoreCountA": "0",
              "OverdueMoreCountB": "0",
              "OwingAmountA": "-1.00",
              "OwingAmountB": "-1.00",
              "OwingPrevHighDebtRatioA": "-1.00",
              "OwingPrevHighDebtRatioB": "0.40",
              "OwingHighDebtRatioA": "-1.00",
              "OwingHighDebtRatioB": "-1.00",
              "LastHighestBorrowRatioA": "-1.00",
              "LastHighestBorrowRatioB": "2.00",
              "BidAmount": "58",
              "ConditionMask": "1040187390",
              "ValidateCode": "16",
              "Status": "0",
              "DayAmountLimit": "0",
              "DayAmount": "58",
              "LastBidTime": "2017-11-01 00:10:50",
              "SettingUpdateTime": "2017-10-30 18:35:54"
            }
"""

        val aLoanStr = """
            {
              "amount": 7343,
              "months": 6,
              "listingId": 80950559,
              "firstBidTime": 1509466373677,
              "creditCode": "B",
              "lastBidTime": 1509466377013,
              "lenderCount": 9,
              "auditingTime": 946656000000,
              "remainFunding": 700,
              "deadLineTimeOrRemindTimeStr": "19天23时28分",
              "currentRate": 18,
              "borrowName": "pdu5152045180",
              "gender": 1,
              "educationDegree": "专科",
              "graduateSchool": "东莞南博职业技术学院",
              "studyStyle": "成人",
              "age": 30,
              "successCount": 0,
              "wasteCount": 1,
              "cancelCount": 0,
              "failedCount": 0,
              "normalCount": 28,
              "overdueLessCount": 0,
              "overdueMoreCount": 0,
              "owingPrincipal": 756.2,
              "owingAmount": 767.57,
              "amountToReceive": 0,
              "firstSuccessBorrowTime": 1484763597000,
              "lastSuccessBorrowTime": 1498213083000,
              "registerTime": 1484600581000,
              "certificateValidate": true,
              "nciicIdentityCheck": false,
              "phoneValidate": true,
              "videoValidate": false,
              "creditValidate": true,
              "educateValidate": false,
              "highestPrincipal": 5500,
              "highestDebt": 8488.9,
              "totalPrincipal": 14160,
              "xfetchTime": null
            }
"""
        aRule = ObjectMapper().readValue(aRuleStr.trim(), HunterDiyRulesResp.HunterDiyRuleItem::class.java)
        aLoan = ObjectMapper().readValue(aLoanStr.trim(), Loan::class.java)
    }

    @Test
    fun testAmount() {
        // Amount
        aRule.AmountA = 1000
        aRule.AmountB = -1
        aLoan.Amount = 5000f
        assertTrue(HunterMatcher.matchRule2(aLoan, aRule))

        aRule.AmountA = 1000
        aRule.AmountB = -1
        aLoan.Amount = 500f
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule))
    }

    @Test
    fun testMonths() {
        // Months
        aRule.MonthA = 3
        aRule.MonthB = -1
        aLoan.Months = 9
        assertTrue("month test fail", HunterMatcher.matchRule2(aLoan, aRule))

        aRule.MonthA = 3
        aRule.MonthB = -1
        aLoan.Months = 1
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule))

        aRule.MonthA = 1
        aRule.MonthB = 1
        aLoan.Months = 3
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule))
    }

    @Test
    fun testLastSuccessInterval() {
        //today is 2017-11-2
        //LastSuccessInterval
        /*
        aRule.LastSuccessIntervalA = 10
        aRule.LastSuccessIntervalB = 90
        aLoan.LastSuccessBorrowTime = Timestamp(sdf.parse("2017-10-1 12:00:33").time)
        assertTrue(HunterMatcher.matchRule2(aLoan, aRule, true))

        aRule.LastSuccessIntervalA = 3
        aRule.LastSuccessIntervalB = 70
        aLoan.LastSuccessBorrowTime = Timestamp(sdf.parse("2017-11-1 12:00:33").time)
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule, true))
        */

        aRule.LastSuccessIntervalA = -1
        aRule.LastSuccessIntervalB = -1
        aLoan.LastSuccessBorrowTime = Timestamp(sdf.parse("2018-11-1 12:00:33").time)
        assertTrue(HunterMatcher.matchRule2(aLoan, aRule))

        aRule.LastSuccessIntervalA = 30
        aRule.LastSuccessIntervalB = -1
        aLoan.LastSuccessBorrowTime = Timestamp(sdf.parse("2017-10-1 12:00:33").time)
        assertTrue(HunterMatcher.matchRule2(aLoan, aRule))
    }

    @Test
    fun testFirstSuccessInterval() {
        //today is 2017-11-2
        aRule.FirstSuccessIntervalA = 60
        aRule.FirstSuccessIntervalB = -1
        aLoan.FirstSuccessBorrowTime = Timestamp(sdf.parse("2017-11-1 12:00:33").time)
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule))

        aRule.FirstSuccessIntervalA = 0
        aRule.FirstSuccessIntervalB = -1
        aLoan.FirstSuccessBorrowTime = Timestamp(sdf.parse("2017-11-1 12:00:33").time)
        assertTrue(HunterMatcher.matchRule2(aLoan, aRule))

        aRule.FirstSuccessIntervalA = -1
        aRule.FirstSuccessIntervalB = 10
        aLoan.FirstSuccessBorrowTime = Timestamp(sdf.parse("2017-10-1 12:00:33").time)
        assertFalse(HunterMatcher.matchRule2(aLoan, aRule))
    }

    @Test
    fun testCreditCodeMask() {
        //{"strategyId":100669,"description":"X1_Desc","userId":95,"status":0,"rateA":-1,"rateB":-1,"age":15,"lastBidTime":null,"gender":2,"studyStyle":7,"certificateValidate":0,"nciicIdentityCheck":0,"phoneValidate":0,"videoValidate":0,"creditValidate":0,"educateValidate":0,"strategyLevel":0,"strategyName":"X1","amountA":-1,"amountB":-1,"monthA":-1,"monthB":-1,"credit":6,"degree":7,"school":3,"lastSuccessIntervalA":-1,"lastSuccessIntervalB":-1,"firstSuccessIntervalA":50,"firstSuccessIntervalB":-1,"successCountA":1,"successCountB":-1,"wasteCountA":-1,"wasteCountB":-1,"normalCountA":-1,"normalCountB":-1,"normalSuccessRatioA":-1,"normalSuccessRatioB":-1,"delayNormalRatioA":-1,"delayNormalRatioB":-1,"overdueCountA":0,"overdueCountB":5,"overdueMoreCountA":0,"overdueMoreCountB":0,"owingAmountA":-1,"owingAmountB":-1,"owingPrevHighDebtRatioA":-1,"owingPrevHighDebtRatioB":-1,"owingHighDebtRatioA":-1,"owingHighDebtRatioB":-1,"lastHighestBorrowRatioA":-1,"lastHighestBorrowRatioB":-1,"bidAmount":51,"conditionMask":521724806,"validateCode":0,"dayAmountLimit":1000,"dayAmount":0,"settingUpdateTime":"2017-11-05 12:50:49"}
        aRule.ConditionMask = 521724806
        aLoan.RemainFunding = 1000f

        aLoan.CreditCode = "AA"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0000001"))

        aLoan.CreditCode = "A"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0000010"))

        aLoan.CreditCode = "B"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0000100"))

        aLoan.CreditCode = "C"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0001000"))

        aLoan.CreditCode = "D"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0010000"))

        aLoan.CreditCode = "E"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("0100000"))

        aLoan.CreditCode = "F"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertTrue(Integer.toBinaryString(HunterMatcher.getMask(aLoan)).endsWith("1000000"))

        aLoan.CreditCode = "G"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(0, HunterMatcher.getMask(aLoan))
    }

    @Test
    fun testDegreeMask() {
        aLoan.EducationDegree = null
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "博士研究生"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "硕士研究生"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "本科"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "专科"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "专科(高职)"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.EducationDegree = "乱七八糟"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(0, HunterMatcher.getMask(aLoan))
    }

    @Test
    fun testAgeMask() {
        aLoan.Age = 15
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(0, HunterMatcher.getMask(aLoan))

        aLoan.Age = 19
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 22
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 27
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 30
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 33
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 43
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 53
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))

        aLoan.Age = 90
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(0, HunterMatcher.getMask(aLoan))
    }

    @Test
    fun testStudyStyleMask() {
        aLoan.StudyStyle = "全日制"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
    }

    //Overall cond mask test
    @Test
    fun testConditionalMask() {
        aRule.ConditionMask = 521724806
        aLoan.RemainFunding = 1000f

        aLoan.CreditCode = "A"
        aLoan.Gender = 2 //女
        aLoan.GraduateSchool = "上海大学"
        aLoan.EducationDegree = "本科"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(true, HunterMatcher.matchRule2(aLoan, aRule))

        aLoan.CreditCode = "A"
        aLoan.Gender = 2 //女
        aLoan.GraduateSchool = "上海大学"
        aLoan.EducationDegree = "专科"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(false, HunterMatcher.matchRule2(aLoan, aRule))

        aLoan.CreditCode = "A"
        aLoan.Gender = 2 //女
        aLoan.GraduateSchool = "无名大学"
        aLoan.EducationDegree = "专科"
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(false, HunterMatcher.matchRule2(aLoan, aRule))

        aLoan.CreditCode = "A"
        aLoan.Gender = 2 //女
        aLoan.GraduateSchool = "无名大学"
        aLoan.EducationDegree = "乱七八糟"//导致错误值
        HunterMatcher.decryptMask(HunterMatcher.getMask(aLoan))
        assertEquals(false, HunterMatcher.matchRule2(aLoan, aRule))
    }
}
