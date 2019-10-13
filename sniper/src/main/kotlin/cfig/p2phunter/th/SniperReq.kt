package cfig.p2phunter.th

import com.fasterxml.jackson.annotation.JsonProperty

class SniperReq {
    data class LoanIdList(
            @JsonProperty("ids")
            var ids: MutableList<Long> = mutableListOf()
    )
}
