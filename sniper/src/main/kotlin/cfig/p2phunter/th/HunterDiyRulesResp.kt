package cfig.p2phunter.th

import cfig.p2psniper.ppdsdk.PpdServiceBase
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/5/17
 */
data class HunterDiyRulesResp(
        @JsonProperty("status") var status: Int = 0,
        @JsonProperty("msg") var msg: String = "",
        @JsonProperty("count") var count: Int = 0,
        @JsonProperty("diy") var diy: MutableList<HunterDiyRuleItem> = mutableListOf()
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class HunterDiyRuleItem(
            @JsonProperty("StrategyId") var StrategyId: Long = 0,
            @JsonProperty("UserId") var UserId: Int = 0,
            @JsonProperty("StrategyLevel") var StrategyLevel: Int = 0,
            @JsonProperty("StrategyName") var StrategyName: String = "",
            @JsonProperty("Description") var Description: String = "",
            @JsonProperty("AmountA") var AmountA: Int = -1,
            @JsonProperty("AmountB") var AmountB: Int = -1,
            @JsonProperty("MonthA") var MonthA: Int = -1,
            @JsonProperty("MonthB") var MonthB: Int = -1,
            @JsonProperty("RateA") var RateA: Float = -1f,
            @JsonProperty("RateB") var RateB: Float = -1f,
            @JsonProperty("Credit") var Credit: Int = 0,

            //学历认证
            @JsonProperty("CertificateValidate") var CertificateValidate: Int = 0,
            //禁止学历认证
            @JsonProperty("CertificateValidateFalse") var CertificateValidateFalse: Int = 0,

            //征信认证
            @JsonProperty("CreditValidate") var CreditValidate: Int = 0,
            //禁止征信认证
            @JsonProperty("CreditValidateFalse") var CreditValidateFalse: Int = 0,

            //学籍认证
            @JsonProperty("EducateValidate") var EducateValidate: Int = 0,
            //视频认证
            @JsonProperty("VideoValidate") var VideoValidate: Int = 0,

            //电话认证
            @JsonProperty("PhoneValidate") var PhoneValidate: Int = 0,
            //禁止 电话认证
            @JsonProperty("PhoneValidateFalse") var PhoneValidateFalse: Int = 0,

            //强制 户籍认证
            @JsonProperty("NciicIdentityCheck") var NciicIdentityCheck: Int = 0,
            //禁止 户籍认证
            @JsonProperty("NciicIdentityCheckFalse") var NciicIdentityCheckFalse: Int = 0,

            //学历学位
            @JsonProperty("Degree") var Degree: Int = 0,

            //学习形式: 普通，成人，研究生，网络教育，自学考试，无学习形式
            @JsonProperty("StudyStyle") var StudyStyle: Int = 0,

            //学校类别:985,211，其他学校，无学校
            @JsonProperty("School") var School: Int = 0,

            @JsonProperty("Gender") var Gender: Int = 0,
            //年龄
            /*
                18-22:
                23-27:
                28-32:
                33-40:
                41-60:
             */
            @JsonProperty("Age") var Age: Int = -1,

            //距离上次成功借款时间(天)
            //刚借完又借的，你怎么看？
            @JsonProperty("LastSuccessIntervalA") var LastSuccessIntervalA: Int = -1,
            @JsonProperty("LastSuccessIntervalB") var LastSuccessIntervalB: Int = -1,

            //距离首次成功借款时间(月)
            @JsonProperty("FirstSuccessIntervalA") var FirstSuccessIntervalA: Int = -1,
            @JsonProperty("FirstSuccessIntervalB") var FirstSuccessIntervalB: Int = -1,

            //历史成功借款次数
            @JsonProperty("SuccessCountA") var SuccessCountA: Int = -1,
            @JsonProperty("SuccessCountB") var SuccessCountB: Int = -1,

            //流标次数
            //被大家很多次不看好的人，以后你看好吗？
            @JsonProperty("WasteCountA") var WasteCountA: Int = -1,
            @JsonProperty("WasteCountB") var WasteCountB: Int = -1,

            //正常还清次数
            @JsonProperty("NormalCountA") var NormalCountA: Int = -1,
            @JsonProperty("NormalCountB") var NormalCountB: Int = -1,

            //正常还清/成功借款次数
            //如果一个人总是借了就还刷信用，那么这个比值会明显低于正常值
            @JsonProperty("NormalSuccessRatioA") var NormalSuccessRatioA: Float = -1f,
            @JsonProperty("NormalSuccessRatioB") var NormalSuccessRatioB: Float = -1f,

            //逾期/正常还款期数 比
            @JsonProperty("DelayNormalRatioA") var DelayNormalRatioA: Float = -1f,
            @JsonProperty("DelayNormalRatioB") var DelayNormalRatioB: Float = -1f,

            //一般逾期次数
            //15天以内的逾期次数
            @JsonProperty("OverdueCountA") var OverdueCountA: Int = -1,
            @JsonProperty("OverdueCountB") var OverdueCountB: Int = -1,

            //严重逾期次数（天）
            //严重逾期是指逾期天数超过15天的逾期
            @JsonProperty("OverdueMoreCountA") var OverdueMoreCountA: Int = -1,
            @JsonProperty("OverdueMoreCountB") var OverdueMoreCountB: Int = -1,

            //借前负债
            //本次借款前，未还清的本息总和
            @JsonProperty("OwingAmountA") var OwingAmountA: Float = -1f,
            @JsonProperty("OwingAmountB") var OwingAmountB: Float = -1f,

            //借前负债/历史最高负债
            @JsonProperty("OwingPrevHighDebtRatioA") var OwingPrevHighDebtRatioA: Float = -1f,
            @JsonProperty("OwingPrevHighDebtRatioB") var OwingPrevHighDebtRatioB: Float = -1f,

            //借后负债/历史最高负债
            //借后负债=本次借款金额+当前待还。此指标可一定程度规避蓄意借款不还的动机
            @JsonProperty("OwingHighDebtRatioA") var OwingHighDebtRatioA: Float = -1f,
            @JsonProperty("OwingHighDebtRatioB") var OwingHighDebtRatioB: Float = -1f,

            //本次借款金额/历史最高单次借款金额
            @JsonProperty("LastHighestBorrowRatioA") var LastHighestBorrowRatioA: Float = -1f,
            @JsonProperty("LastHighestBorrowRatioB") var LastHighestBorrowRatioB: Float = -1f,

            //单标投标金额
            @JsonProperty("BidAmount") var BidAmount: Int = 0,

            //预先计算好的杂项配置
            @JsonProperty("ConditionMask") var ConditionMask: Int = 0,

            //不用，每次重新生成
            @JsonProperty("ValidateCode") var ValidateCode: Int = 0,
            //禁止ValidateCode检查
            @JsonProperty("ValidateCodeFalse") var ValidateCodeFalse: Int = 0,

            @JsonProperty("Status") var Status: Int = 0,

            //每日投标限额
            @JsonProperty("DayAmountLimit") var DayAmountLimit: Int = 0,
            //今日已投
            @JsonProperty("DayAmount") var DayAmount: Int = 0,
            //上次投标时间
            @JsonProperty("LastBidTime") var LastBidTime: String? = null,
            @JsonProperty("SettingUpdateTime") var SettingUpdateTime: String = "",

            //new vectors
            @JsonProperty("AgeRangeA") var AgeRangeA: Int = -1,
            @JsonProperty("AgeRangeB") var AgeRangeB: Int = -1,

            //平均借款间隔（天）
            //含义：平均借款频率高不高
            @JsonProperty("AvgBorrowIntervalA") var AvgBorrowIntervalA: Float = -1f, //Int -> Float: differ from hunter
            @JsonProperty("AvgBorrowIntervalB") var AvgBorrowIntervalB: Float = -1f, //Int -> Float: differ from hunter

            // 本次借款间隔比例 = 上借时间 / 首借时间 * 总借款次数
            // 含义：最近借款急不急?
            // 1: 与以前频率一致
            // <1: 更频繁了
            // >1: 比上次借的间隔久了
            @JsonProperty("CurAvgIntervalRatioA") var CurAvgIntervalRatioA: Float = -1f,
            @JsonProperty("CurAvgIntervalRatioB") var CurAvgIntervalRatioB: Float = -1f,

            // 注册－首借 间隔月数
            // 注意：对于非首借，数值为静态数据； 如果没有成功首借，则使用当前首借月数
            // 意义：注册了马上就借了，还是先注册号等等再借
            @JsonProperty("RegisterFirstIntervalA") var RegisterFirstIntervalA: Int = -1,
            @JsonProperty("RegisterFirstIntervalB") var RegisterFirstIntervalB: Int = -1,

            // 注册时间
            // 意义：是否是老用户
            @JsonProperty("RegisterMonthA") var RegisterMonthA: Int = -1,
            @JsonProperty("RegisterMonthB") var RegisterMonthB: Int = -1,

            @JsonProperty("OwingAfterAmountA") var OwingAfterAmountA: Float = -1f,
            @JsonProperty("OwingAfterAmountB") var OwingAfterAmountB: Float = -1f,

            @JsonProperty("TotalBorrowA") var TotalBorrowA: Float = -1f,
            @JsonProperty("TotalBorrowB") var TotalBorrowB: Float = -1f,

            @JsonProperty("OwnPreTotalBorrowRatioA") var OwnPreTotalBorrowRatioA: Float = -1f,
            @JsonProperty("OwnPreTotalBorrowRatioB") var OwnPreTotalBorrowRatioB: Float = -1f,

            @JsonProperty("CurAmountTotalBorrowRatioA") var CurAmountTotalBorrowRatioA: Float = -1f,
            @JsonProperty("CurAmountTotalBorrowRatioB") var CurAmountTotalBorrowRatioB: Float = -1f,

            @JsonProperty("AvgBorrowAmountA") var AvgBorrowAmountA: Float = -1f,
            @JsonProperty("AvgBorrowAmountB") var AvgBorrowAmountB: Float = -1f,

            @JsonProperty("CurAmountAvgBorrowRatioA") var CurAmountAvgBorrowRatioA: Float = -1f,
            @JsonProperty("CurAmountAvgBorrowRatioB") var CurAmountAvgBorrowRatioB: Float = -1f,

            //added on 2018.11.27: 6 more factors
            @JsonProperty("TailNumber10") var TailNumber10: Int = -1,
            @JsonProperty("HighDebtA") var HighDebtA: Int = -1,
            @JsonProperty("HighDebtB") var HighDebtB: Int = -1,
            @JsonProperty("OwingAmountRatioA") var OwingAmountRatioA: Float = -1f,
            @JsonProperty("OwingAmountRatioB") var OwingAmountRatioB: Float = -1f,
            @JsonProperty("WasteNormalRatioA") var WasteNormalRatioA: Float = -1f,
            @JsonProperty("WasteNormalRatioB") var WasteNormalRatioB: Float = -1f,
            @JsonProperty("CancelCountA") var CancelCountA: Int = -1,
            @JsonProperty("CancelCountB") var CancelCountB: Int = -1,
            @JsonProperty("FailCountA") var FailCountA: Int = -1,
            @JsonProperty("FailCountB") var FailCountB: Int = -1,

            //added on 2018.12.24
            @JsonProperty("RcdTime") var RcdTime: String? = null
    ) {

        fun toJson(): String {
            val mapper2 = ObjectMapper()
            mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()
            return mapper2.writeValueAsString(this)
        }
    }
}
