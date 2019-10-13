package cfig.p2psniper.common.sdk2

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class PacerTest {
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    @Test
    fun testPacer() {
        val pacer = Pacer(timeSlot = 5 * 1000, maxPace = 5)
        val run1 = Runner2(pacer, "run1")
        val run2 = Runner2(pacer, "run2")
        Thread(run1).start()
        Thread(run2).start()
        TimeUnit.SECONDS.sleep(20)
    }
}
