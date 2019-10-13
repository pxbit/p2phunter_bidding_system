package cfig.rabbit

import cfig.jdbc.PendingReviewer
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val fs = DigMaster()
    fs.prep("dig_master")
//    fs.recvResult()
//    fs.sendTasks()
//    fs.queryTasks()
//    TimeUnit.MINUTES.sleep(20)

    val pr = PendingReviewer(fs)
    pr.tableExists("PendingLoan")
    var round = 0
    while (true) {
        println("Round: $round")
        pr.reviewPending(fromTop = true, maxSize = 20 * 10000)
        round++
        TimeUnit.SECONDS.sleep(10)
    }

    //pr.tableExists("Loan")
    //pr.reviewLoanStatus(fromTop = true, maxSize = 1 * 100)

    fs.tearDown()
}
