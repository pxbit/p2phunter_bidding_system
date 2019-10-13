package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PpdReq {
    companion object {
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]")
    }

    data class LoanListReq(
            @JsonProperty("PageIndex")
            var PageIndex: Int,

            @JsonProperty("StartDateTime")
            var StartDateTime: String
    ) {
        constructor(inPage: Int, inLocalDateTime: LocalDateTime) : this(inPage, dtf.format(inLocalDateTime))
    }

    data class ListingInfoReq(
            @JsonProperty("ListingIds")
            var ListingIds: List<Long>
    )

    data class QueryUsernameReq(
            @JsonProperty("OpenID")
            var OpenID: String
    )

    data class BidReq(
            @JsonProperty("ListingId")
            var ListingId: Long,
            @JsonProperty("Amount")
            var Amount: Int,
            @JsonProperty("UseCoupon")
            var UseCoupon: String
    )

    data class BidListReq(
            @JsonProperty("LenderNames")
            var LenderNames: List<String>,
            @JsonProperty("TopIndex")
            var TopIndex: Int
    )

    data class QueryBidReq(
            @JsonProperty("listingId") var listingId: Long,
            @JsonProperty("orderId") var orderId: String
    )

    data class AccoundExistsReq(
            @JsonProperty("AccountName")
            var AccountName: String
    )

    data class RegisterReq(
            @JsonProperty("Mobile")
            var Mobile: String?,
            @JsonProperty("Email")
            var Email: String?,
            @JsonProperty("Role")
            var Role: Int
    )

    data class DebtListReq(
            @JsonProperty("PageIndex") var PageIndex: Int,
            @JsonProperty("StartDateTime") var StartDateTime: String,
            @JsonProperty("Levels") var Levels: String = "AA"
    ) {
        constructor(inPage: Int, inLocalDateTime: LocalDateTime) : this(inPage, dtf.format(inLocalDateTime))
    }

    data class DebtInfoReq(
            @JsonProperty("DebtIds")
            var DebtIds: List<Long>
    )
}
