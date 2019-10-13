package cfig.p2psniper.common.sdk2

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

open class Runner<K, V>(val nutBox: NutBox<K, V>,
            val pacer: Pacer,
            val name: String = "") : Runnable {
    override fun run() {
        Thread.currentThread().name = name
        log.info("Runner $name started with pacer+nutbox")
        TimeUnit.SECONDS.sleep(10)
        while (true) {
            if (pacer.overSpeed()) {
                TimeUnit.MILLISECONDS.sleep(pacer.napTime)
                log.debug("$name nap ${pacer.napTime}ms...")
            } else {
                fetchOnce(name)
            }
        }
    }

    open fun fetchOnce(name: String) {
        TimeUnit.MILLISECONDS.sleep(100L + Random().nextInt(2000))
        log.info("${name} done once");
        val now = System.currentTimeMillis()
        pacer.update(now)
    }

    companion object {
        private val log = LoggerFactory.getLogger("Runner")
    }
}
