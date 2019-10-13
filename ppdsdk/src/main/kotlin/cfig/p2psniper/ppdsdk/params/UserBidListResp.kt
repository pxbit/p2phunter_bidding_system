package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.annotation.JsonProperty

data class UserBidListResp(
        @JsonProperty("BidList") var BidList: List<BidListItemResp> = listOf(BidListItemResp()),
        @JsonProperty("TotalPages") var TotalPages: Int = 0,
        @JsonProperty("TotalRecord") var TotalRecord: Int = 0,
        @JsonProperty("Result") var Result: Int = 0,
        @JsonProperty("ResultMessage") var ResultMessage: String? = "") {
    data class BidListItemResp(
            @JsonProperty("ListingId") var ListingId: Int = 0,
            @JsonProperty("Title") var Title: String? = "",
            @JsonProperty("Months") var Months: Int = 0,
            @JsonProperty("Rate") var Rate: Float = 0f,
            @JsonProperty("Amount") var Amount: Int = 0,
            @JsonProperty("BidAmount") var BidAmount: Int = 0)
}