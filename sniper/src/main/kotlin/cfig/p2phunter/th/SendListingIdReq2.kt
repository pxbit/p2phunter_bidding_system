package cfig.p2phunter.th

import cfig.p2psniper.ppdsdk.params.PpdResp.LoanListResp.SingleListingResp
import com.fasterxml.jackson.annotation.JsonProperty

data class SendListingIdReq2(
        @JsonProperty("ids") var ids: List<SingleListingResp> = mutableListOf()
)
