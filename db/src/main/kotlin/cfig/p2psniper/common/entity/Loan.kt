package cfig.p2psniper.common.entity

import com.fasterxml.jackson.databind.ObjectMapper
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by cfig (yuyezhong@gmail.com)) on 8/15/17.
 */
@Entity
data class Loan(
        /* G1 */
        @Id var ListingId: Long = 0,

        //首次投资时间
        @Column(name = "FistBidTime") var FirstBidTime: Timestamp? = null, //spell error
        //末笔投资时间
        var LastBidTime: Timestamp? = null,
        //投标人数
        var LenderCount: Int = 0,
        //成交日期
        var AuditingTime: Timestamp? = null,

        /* G2 */
        //剩余可投金额
        var RemainFunding: Float = 0f,
        //截止时间: 2016/11/19或者14天15时57分(剩余时间)
        var DeadLineTimeOrRemindTimeStr: String = "",
        //标的等级
        var CreditCode: String = "",
        //借款金额
        var Amount: Float = 0f,
        //期限
        var Months: Int = 0,

        /* G3 */
        //利率
        var CurrentRate: Float = 0f,
        //借款人的用户名
        var BorrowName: String? = null,
        //性别	1 男 2 女 0 未知
        var Gender: Int = 0,
        //学历
        var EducationDegree: String? = null,
        //毕业院校
        var GraduateSchool: String? = null,
        /* G4 */
        //学习形式
        var StudyStyle: String? = null,
        var Age: Int = 0,

        //成功借款次数
        var SuccessCount: Int = 0,
        //流标次数
        var WasteCount: Int = 0,
        //撤标次数
        var CancelCount: Int = 0,

        /* G5 */
        //失败次数
        var FailedCount: Int = 0,

        //正常还清次数
        var NormalCount: Int = 0,
        //逾期(1-15)还清次数
        var OverdueLessCount: Int = 0,
        //逾期(15天以上)还清次数
        var OverdueMoreCount: Int = 0,
        //剩余待还本金
        var OwingPrincipal: Float = 0f,

        /* G6 */
        //待还金额
        var OwingAmount: Float = 0f,
        //待收金额
        var AmountToReceive: Float = 0f,
        //第一次成功借款时间
        var FirstSuccessBorrowTime: Timestamp? = null,
        //最后一次成功借款时间
        var LastSuccessBorrowTime: Timestamp? = null,
        //注册时间
        var RegisterTime: Timestamp? = null,

        /* G7 */
        //学历认证
        var CertificateValidate: Boolean = false,
        //户籍认证
        var NciicIdentityCheck: Boolean = false,
        //手机认证
        var PhoneValidate: Boolean = false,
        //视频认证
        var VideoValidate: Boolean = false,
        //征信认证
        var CreditValidate: Boolean = false,
        /* G8 */
        //学籍认证
        var EducateValidate: Boolean? = false,
        //单笔最高借款金额
        var HighestPrincipal: Float = 0f,
        //历史最高负债
        var HighestDebt: Float = 0f,
        //累计借款金额
        var TotalPrincipal: Float = 0f,

        var XFetchTime: Timestamp? = Timestamp(System.currentTimeMillis()),
        /* G9 */
        var XStatus: Short? = null

) {
    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}
