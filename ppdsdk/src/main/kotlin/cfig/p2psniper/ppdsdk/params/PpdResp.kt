package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.text.SimpleDateFormat

class PpdResp {
    /*
     {"Code":"GTW-BRQ-INVALIDTOKEN","Message":"令牌校验失败:'用户无效或令牌已过有效期！'"}
     */
    data class OopsMsg(
            @JsonProperty("Code")
            var Code: String = "",
            @JsonProperty("Message")
            var Message: String = ""
    )

    data class AccountBalance(
            var lockedCashOut: Float = 0f, //"用户备付金.用户提现锁定",
            var lockedOrg: Float = 0f, //"用户备付金.机构资金锁定"
            var lockedDeposit: Float = 0f, //"用户备付金.用户充值锁定",
            var lockedBid: Float = 0f, //"用户备付金.用户投标锁定",
            var lockedOther: Float = 0f, //"用户备付金.用户其他锁定",
            var remains: Float = 0f/*"用户备付金.用户现金余额"*/)

    data class RegisterResp(
            @JsonProperty("ReturnCode")
            var ReturnCode: Int = 0,
            @JsonProperty("ReturnMessage")
            var ReturnMessage: String?,
            @JsonProperty("UserName")
            var UserName: String?,
            @JsonProperty("OpenID")
            var OpenID: String?,
            @JsonProperty("AccessToken")
            var AccessToken: String?,
            @JsonProperty("RefreshToken")
            var RefreshToken: String?,
            @JsonProperty("ExpiresIn")
            var ExpiresIn: Int?
    )

    data class BalanceResp(
            @JsonProperty("Result") var result: Int = 0,
            @JsonProperty("ResultMessage") var resultMessage: String = "",
            @JsonProperty("Balance") var balanceList: List<BalanceItem> = listOf(BalanceItem())
    ) {
        fun toAccountBalance(): AccountBalance {
            var ret = AccountBalance()
            for (item in balanceList) {
                when (item.category) {
                    "用户备付金.用户投标锁定" -> ret.lockedBid = item.balanceValue
                    "用户备付金.用户现金余额" -> ret.remains = item.balanceValue
                    "用户备付金.用户提现锁定" -> ret.lockedCashOut = item.balanceValue
                    "用户备付金.机构资金锁定" -> ret.lockedOrg = item.balanceValue
                    "用户备付金.用户其他锁定" -> ret.lockedOther = item.balanceValue
                    "用户备付金.用户充值锁定" -> ret.lockedDeposit = item.balanceValue
                }
            }
            return ret
        }

        data class BalanceItem(
                @JsonProperty("AccountCategory") var category: String = "",
                @JsonProperty("Balance") var balanceValue: Float = 0f)
    }

    /*
    {
      "AccessToken": "eb598c99-d448-4991-8892-11cd06abe578",
      "RefreshToken": "44b954c6-738c-409a-af4a-b19054e98279",
      "ExpiresIn": 604800
    }
    */
    data class RefreshTokenResp(
            var AccessToken: String = "",
            var RefreshToken: String = "",
            var ExpiresIn: Int = 0
    )

    // QueryUserNameByOpenID
    data class QueryUserNameResp(
            @JsonProperty("ReturnCode") var ReturnCode: Int = 0,
            @JsonProperty("ReturnMessage") var ReturnMessage: String = "",
            @JsonProperty("UserName") var UserName: String = "")

    data class LoanInfoResp(
            @JsonProperty("LoanInfos") var LoanInfos: List<LoanDetailItem>? = listOf(LoanDetailItem()),
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultMessage") var ResultMessage: String? = "",
            @JsonProperty("ResultCode") var ResultCode: Int? = null
    ) {
        data class LoanDetailItem(// 返回39项
                @JsonProperty("ListingId") var ListingId: Long = 0L,
                @JsonProperty("FistBidTime") var FirstBidTime: String? = null, //spell error
                @JsonProperty("LastBidTime") var LastBidTime: String? = null,
                @JsonProperty("LenderCount") var LenderCount: Int = 0,
                @JsonProperty("AuditingTime") var AuditingTime: String? = null,
                @JsonProperty("RemainFunding") var RemainFunding: Float = 0f,
                @JsonProperty("DeadLineTimeOrRemindTimeStr") var DeadLineTimeOrRemindTimeStr: String = "",
                @JsonProperty("CreditCode") var CreditCode: String? = "",
                @JsonProperty("Amount") var Amount: Float = 0f,
                @JsonProperty("Months") var Months: Int = 0,
                @JsonProperty("CurrentRate") var CurrentRate: Float = 0f,
                @JsonProperty("BorrowName") var BorrowName: String = "",
                @JsonProperty("Gender") var Gender: Int = 0,
                @JsonProperty("EducationDegree") var EducationDegree: String? = "",
                @JsonProperty("GraduateSchool") var GraduateSchool: String? = "",
                @JsonProperty("StudyStyle") var StudyStyle: String? = "",
                @JsonProperty("Age") var Age: Int = 0,
                @JsonProperty("SuccessCount") var SuccessCount: Int = 0,
                @JsonProperty("WasteCount") var WasteCount: Int? = 0,
                @JsonProperty("CancelCount") var CancelCount: Int? = 0,
                @JsonProperty("FailedCount") var FailedCount: Int? = 0,
                @JsonProperty("NormalCount") var NormalCount: Int = 0,
                @JsonProperty("OverdueLessCount") var OverdueLessCount: Int? = 0,
                @JsonProperty("OverdueMoreCount") var OverdueMoreCount: Int? = 0,
                @JsonProperty("OwingPrincipal") var OwingPrincipal: Float = 0f,
                @JsonProperty("OwingAmount") var OwingAmount: Float? = 0f,
                @JsonProperty("AmountToReceive") var AmountToReceive: Float? = 0f,
                @JsonProperty("FirstSuccessBorrowTime") var FirstSuccessBorrowTime: String? = "",
                @JsonProperty("LastSuccessBorrowTime") var LastSuccessBorrowTime: String? = "",
                @JsonProperty("RegisterTime") var RegisterTime: String? = "",
                @JsonProperty("CertificateValidate") var CertificateValidate: Boolean = false,
                @JsonProperty("NciicIdentityCheck") var NciicIdentityCheck: Boolean = false,
                @JsonProperty("PhoneValidate") var PhoneValidate: Boolean = false,
                @JsonProperty("VideoValidate") var VideoValidate: Boolean = false,
                @JsonProperty("CreditValidate") var CreditValidate: Boolean = false,
                @JsonProperty("EducateValidate") var EducateValidate: Boolean? = false,
                @JsonProperty("HighestPrincipal") var HighestPrincipal: Float? = 0f,
                @JsonProperty("HighestDebt") var HighestDebt: Float? = 0f,
                @JsonProperty("TotalPrincipal") var TotalPrincipal: Float? = 0f)
    }

    /*
    {
        "ResultCode": 500,
        "ResultMessage": "Internal Server Error"
    }
    */
    data class CommonError(
            @JsonProperty("ResultCode")
            var ResultCode: Int = 0,
            @JsonProperty("ResultMessage")
            var ResultMessage: String? = null
    )


    data class LoanListResp(
            @JsonProperty("LoanInfos") var listings: List<SingleListingResp> = listOf(),
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultMessage") var ResultMessage: String = "",
            @JsonProperty("ResultCode") var ResultCode: String? = null) {
        data class SingleListingResp(
                @JsonProperty("ListingId") var ListingId: Long = 0,
                @JsonProperty("Title") var Title: String = "",
                @JsonProperty("CreditCode") var CreditCode: String = "",
                @JsonProperty("Amount") var Amount: Float = 0f,
                @JsonProperty("Rate") var Rate: Float = 0f,
                @JsonProperty("Months") var Months: Int = 0,
                @JsonProperty("PayWay") var PayWay: Int = 0,
                @JsonProperty("RemainFunding") var RemainFunding: Float = 0f,
                @JsonProperty("PreAuditTime") var PreAuditTime: String = "")
    }

    // for listingBidInfos
    data class ListingBidsInfoResp(
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultCode") var ResultCode: Int? = null,
            @JsonProperty("ResultMessage") var ResultMessage: String = "",
            @JsonProperty("ListingBidsInfos") var infos: List<InnerResp> = listOf()
    ) {
        data class InnerResp(
                @JsonProperty("ListingId") var ListingId: Long = 0,
                @JsonProperty("Bids") var bids: List<BidInfoResp> = listOf()
        )

        data class BidInfoResp(
                @JsonProperty("LenderName") var LenderName: String = "",
                @JsonProperty("BidAmount") var BidAmount: Float = 0f,
                @JsonProperty("BidDateTime") var BidDateTime: String = "")
    }

    //bidding resp
    data class BidResp(
            @JsonProperty("ListingId") var ListingId: Long = 0L,
            @JsonProperty("Amount") var Amount: Float = 0f,
            @JsonProperty("ParticipationAmount") var ParticipationAmount: Float = 0f,
            @JsonProperty("CouponAmount") var CouponAmount: Float = 0f,
            @JsonProperty("CouponStatus") var CouponStatus: Int = 0,
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultMessage") var ResultMessage: String? = null,
            @JsonProperty("InvestId") var InvestId: Long = 0
    )

    //new bidding resp
    data class BidResp2(
            @JsonProperty("Amount") var Amount: Float = 0f,
            @JsonProperty("ListingId") var ListingId: Long = 0L,
            @JsonProperty("OrderId") var OrderId: String? = null,
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultCode") var ResultCode: Int? = null,
            @JsonProperty("ResultMessage") var ResultMessage: String? = null
    )

    //new bidding query resp
    data class BidQueryResp2(
            @JsonProperty("result") var result: Int,
            @JsonProperty("resultMessage") var resultMessage: String? = null,
            @JsonProperty("resultContent") var resultContent: RetContent = RetContent()
    ) {
        data class RetContent(
                @JsonProperty("bidId") var bidId: Long = 0,
                @JsonProperty("listingId") var listingId: Long = 0,
                @JsonProperty("bidMessage") var bidMessage: String? = null,
                @JsonProperty("bidAmount") var bidAmount: Float = 0f,
                @JsonProperty("participationAmount") var participationAmount: Float = 0f,
                @JsonProperty("succCouponAmount") var succCouponAmount: Float = 0f,
                @JsonProperty("bidTime") var bidTime: String? = null
        )
    }

    //LLoanInfoService.BatchListingStatusInfos
    data class ListingStatusResp(
            @JsonProperty("Infos") var Infos: List<ListingStatusItem> = listOf(),
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultMessage") var ResultMessage: String? = null,
            @JsonProperty("ResultCode") var ResultCode: Int? = null
    ) {
        data class ListingStatusItem(
                @JsonProperty("ListingId") var ListingId: Long = 0L,
                @JsonProperty("Status") var Status: Int = 0)
    }

    //debt
    data class DebtListResp(
            @JsonProperty("ResultCode") var ResultCode: String? = null,
            @JsonProperty("Result") var Result: Int,
            @JsonProperty("ResultMessage") var ResultMessage: String?,
            @JsonProperty("Count") var Count: Int,
            @JsonProperty("DebtInfos") var DebtInfos: List<DebtInfoItem> = mutableListOf()
    ) {
        data class DebtInfoItem(
                @JsonProperty("DebtdealId") var DebtdealId: Long,
                @JsonProperty("OwingNumber") var OwingNumber: Int,
                @JsonProperty("PriceforSaleRate") var PriceforSaleRate: Float,
                @JsonProperty("PriceforSale") var PriceforSale: Float,
                @JsonProperty("ListingId") var ListingId: Long,
                @JsonProperty("CreditCode") var CreditCode: String
        )
    }

    //debt
    data class DebtDetailResp(
            @JsonProperty("Result") var Result: Int = 0,
            @JsonProperty("ResultCode") var ResultCode: String? = null,
            @JsonProperty("ResultMessage") var ResultMessage: String? = null,
            @JsonProperty("DebtInfos") var DebtInfos: List<DebtDetailItem> = mutableListOf()
    ) {
        data class DebtDetailItem(
                @JsonProperty("AllowanceRadio") var AllowanceRadio: Float,//折让比例（%号之前的数值）
                @JsonProperty("BidDateTime") var BidDateTime: String,//投标时间
                @JsonProperty("CreditCode") var CreditCode: String,//列表等级
                @JsonProperty("CurrentCreditCode") var CurrentCreditCode: String,//当前评级
                @JsonProperty("Days") var Days: Int,//距离下次还款的天数
                @JsonProperty("DebtId") var DebtId: Long,//债转编号
                @JsonProperty("Lender") var Lender: String,//投标人
                @JsonProperty("ListingAmount") var ListingAmount: Int,//发标金额
                @JsonProperty("ListingId") var ListingId: Long,//列表ID 原标的信息
                @JsonProperty("ListingMonths") var ListingMonths: Int,//标期限
                @JsonProperty("ListingRate") var ListingRate: Float,//标的利率
                @JsonProperty("ListingTime") var ListingTime: String,//发标时间
                @JsonProperty("OwingInterest") var OwingInterest: Float,//待收利息
                @JsonProperty("OwingNumber") var OwingNumber: Int, //剩余期数
                @JsonProperty("OwingPrincipal") var OwingPrincipal: Float, //待还本金
                @JsonProperty("PastDueDay") var PastDueDay: Int,//曾最大逾期天数
                @JsonProperty("PastDueNumber") var PastDueNumber: Int,//曾逾期期数
                @JsonProperty("PreferenceDegree") var PreferenceDegree: Int,//优惠度
                @JsonProperty("PriceforSale") var PriceforSale: Float,//转让价
                @JsonProperty("PriceforSaleRate") var PriceforSaleRate: Float,//转让价利率
                @JsonProperty("Seller") var Seller: String,//转出人
                @JsonProperty("StatusId") var StatusId: Int //1出售中2已完成 0其它无效状态
        )
    }
}
