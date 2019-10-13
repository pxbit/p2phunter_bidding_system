package cfig.rabbit

import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val fc = DigWorker()
    fc.prep("status_worker")
    fc.fetchStatusTasks()
    TimeUnit.MINUTES.sleep(1)
    fc.tearDown()
}
