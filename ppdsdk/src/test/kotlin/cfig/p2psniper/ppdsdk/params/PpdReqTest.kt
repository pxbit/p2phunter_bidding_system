package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.*
import org.junit.Test
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PpdReqTest {
    @Test
    fun testRequest() {
        val req = PpdReq.LoanListReq(1, LocalDateTime.now())
        println(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(req))
    }
}