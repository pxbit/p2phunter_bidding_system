package cfig.p2psniper.ppdsdk

import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PropFormatterTest {
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    @Test
    fun anyFormat() {
        val nowLocalDateTime = LocalDateTime.now()
        val zonedDateTime = nowLocalDateTime.atZone(ZoneId.of("UTC"))
        println("XXX")
        println(PropFormatter.anyFormat(nowLocalDateTime))
        println(PropFormatter.anyFormat(zonedDateTime.toEpochSecond()))
        println("XXX")
    }

    @Test
    fun tt() {
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSS]")

        val now = LocalDateTime.now()
        println("Now :    " + now)

        val localTime = LocalDateTime.parse("1970-01-01 08:00:00.0000", dtf)
        println("Current: " + localTime)

        val sigaTime = localTime.atZone(ZoneId.of("Asia/Singapore"))
        println("siga   : " + sigaTime)

        val nyTime = sigaTime.withZoneSameInstant(ZoneId.of("America/New_York"))
        println("ny     : " + nyTime)

        println("\n---DateTimeFormatter---")
        println("Date (Singapore) : " + dtf.format(sigaTime))
        println("Date (New York) : " + dtf.format(nyTime))
    }

    @Test
    fun t2() {
        val now = LocalDateTime.now()
        println("Now " + now)

        println("new " + now.atZone(ZoneId.of("Asia/Tokyo")))

        var zonedDateTime = ZonedDateTime.now()
        println("Now " + zonedDateTime)
    }
}
