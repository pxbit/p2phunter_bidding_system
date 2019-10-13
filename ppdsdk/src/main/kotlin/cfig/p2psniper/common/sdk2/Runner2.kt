package cfig.p2psniper.common.sdk2

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

open class Runner2(val pacer: Pacer,
        val name: String = "",
        val delayStart: Int = 5) : Runnable {
    override fun run() {
        Thread.currentThread().name = name
        log.info("Runner $name started with pacer(${pacer.maxPace} in ${pacer.timeSlot}ms), will launch after ${delayStart}s...")
        TimeUnit.SECONDS.sleep(delayStart.toLong())
        while (true) {
            if (pacer.overSpeed()) {
                TimeUnit.MILLISECONDS.sleep(pacer.napTime)
                log.debug("$name nap ${pacer.napTime}ms...")
            } else {
                doWorkOnce(name)
            }
        }
    }

    /*
     * implementations need to override this function to add actual work
     */
    open fun doWorkOnce(name: String) {
        TimeUnit.MILLISECONDS.sleep(100L + Random().nextInt(2000))
        log.info("${name} work done");
        pacer.update(System.currentTimeMillis())
    }

    companion object {
        private val log = LoggerFactory.getLogger("Runner")
    }
}
