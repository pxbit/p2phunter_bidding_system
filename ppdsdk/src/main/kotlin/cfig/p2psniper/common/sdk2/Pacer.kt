package cfig.p2psniper.common.sdk2

import java.util.*
import org.slf4j.LoggerFactory

/*
 *
 * @param timeSlot measure the pace in this specified time slot, in milliseconds
 * @param maxPace  max pace in recent [timeSlot]ms
 * @param napTime  nap time if overspeed
 *
 */
class Pacer(val timeSlot: Int = 10_1000,
            val maxPace: Int = 97,
            val napTime: Long = 500L,
            private val maxSize: Int = 1000) {
    private var footprints = LinkedList<Long>()

    @Synchronized
    fun overSpeed(): Boolean {
        val now = System.currentTimeMillis()
        val cnt = footprints.count { it > now - timeSlot } 
        log.debug("cnt = $cnt")
        return (cnt > maxPace)
    }

    @Synchronized
    fun update(fetchTime: Long) {
        footprints.addLast(fetchTime)
        for (i in 1..(footprints.size - maxSize)) {
            footprints.removeFirst()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Pacer::class.java)
    }
}
