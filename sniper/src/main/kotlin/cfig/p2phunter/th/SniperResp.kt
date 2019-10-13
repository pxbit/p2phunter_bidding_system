package cfig.p2phunter.th

import cfig.p2psniper.common.entity.Loan

class SniperResp {
    data class PreBidResp(
            var loanList: List<Loan> = mutableListOf()
    )
}