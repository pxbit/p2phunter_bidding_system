package cfig.p2phunter.controller

import cfig.p2phunter.component.LoanDigger
import cfig.p2phunter.th.SendListingIdReq
import cfig.p2phunter.th.SniperResp
import cfig.p2psniper.ppdsdk.PpdServiceBase
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class SniperController {
    private val log = LoggerFactory.getLogger(SniperController::class.java)
    private val mapper2 = ObjectMapper()

    @Autowired
    private val loanDigger: LoanDigger? = null

    init {
        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()
    }

    @RequestMapping("/login")
    fun login(): String {
        return "login"
    }

    @PostMapping(value = ["/dig"], produces = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE))
    fun preDig(@Valid @RequestBody shadowList: SendListingIdReq): SniperResp.PreBidResp {
        val loanList = loanDigger!!.preDig(shadowList.ids)
        val ret = SniperResp.PreBidResp(loanList)
        log.debug("dig $shadowList, got ${ret.loanList.size}")
        return ret
    }
}
