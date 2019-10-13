package cfig.jdbc

import cfig.rabbit.DigMaster
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.TimeUnit

class PendingReviewer(val digMaster: DigMaster) {
    fun tableExists(tableName: String): Boolean {
        var ret = false
        jdbcConnection.createStatement().use { statement ->
            val checkSql = "show tables like '$tableName';"
            log.info(checkSql)
            statement.executeQuery(checkSql).use { r ->
                if (r.next()) {
                    log.info("rs: " + r.getString(1))
                    log.info("table $tableName exists")
                    ret = true
                } else {
                    log.info("table $tableName doesn't exist")
                }
            }//end-of-ResultSet
        }//end-of-Statement

        return ret
    }

    fun findPendingLoans(maxSize: Int, fromTop: Boolean): List<Long> {
        val ret: MutableList<Long> = mutableListOf()
        val orderStr = if (fromTop) "desc" else "asc"
        jdbcConnection.createStatement().use { statement ->
            var queryStr = "SELECT c.ListingId FROM PendingLoan c where c.dead = false order by c.ListingId $orderStr limit $maxSize"
            statement.executeQuery(queryStr).use { rs ->
                while (rs.next()) {
                    val listingId = rs.getLong("ListingId")
                    ret.add(listingId)
                }
            }
        }

        return ret
    }

    fun findLoansWithoutStatus(maxSize: Int, fromTop: Boolean): List<Long> {
        val ret: MutableList<Long> = mutableListOf()
        val orderStr = if (fromTop) "desc" else "asc"
        jdbcConnection.createStatement().use { statement ->
            var queryStr = "SELECT c.ListingId FROM Loan c where c.XStatus is null order by c.ListingId $orderStr limit $maxSize"
            statement.executeQuery(queryStr).use { rs ->
                while (rs.next()) {
                    val listingId = rs.getLong("ListingId")
                    ret.add(listingId)
                }
            }
        }

        return ret
    }

    fun reviewLoanStatus(fromTop: Boolean, maxSize: Int) {
        var lap = 0
        val todoList = findLoansWithoutStatus(maxSize, fromTop)
        if (todoList.isNotEmpty()) {
            log.info("[XStatus]Reviewing XStatus, min=" + todoList[0] + ", max="
                    + todoList[if (todoList.size == 1) 0 else (todoList.size - 1)])
            var pointer = 0
            while (true) {
                if (lap % 20 == 0) {
                    val ongoingTasks = digMaster.queryTasks(digMaster.statusTaskQueue)
                    if (ongoingTasks == null) {
                        log.warn("[XStatus]did not get valid XStatus tasks, wait a moment ...")
                        TimeUnit.SECONDS.sleep(10)
                        log.warn("[XStatus]did not get valid XStatus tasks, wait done")
                        continue
                    }

                    if (ongoingTasks > 20) {
                        log.warn("[XStatus]work load is heavy, wait a moment ...")
                        TimeUnit.SECONDS.sleep(10)
                        log.warn("[XStatus]work load is heavy, wait done")
                        continue
                    }
                }
                val startId = pointer
                val endId = minOf(todoList.size, startId + 10)
                if (startId >= endId) break
                lap++
                val currentLap = lap
                log.info("[XStatus]review sublist: $startId, $endId, lap $currentLap")
                val req = SendListingIdReq(ids = todoList.subList(startId, endId))
                digMaster.sendTask(ObjectMapper().writeValueAsString(req), digMaster.statusTaskQueue)
                if (pointer >= todoList.size) break
                pointer += 10
                TimeUnit.MILLISECONDS.sleep(100)
            }
        } else {
            log.info("Review history done")
        }
    }

    fun reviewPending(fromTop: Boolean, maxSize: Int) {
        var lap = 0
        val todoList = findPendingLoans(maxSize, fromTop)
        if (todoList.isNotEmpty()) {
            log.info("Reviewing history, min=" + todoList[0] + ", max="
                    + todoList[if (todoList.size == 1) 0 else (todoList.size - 1)])
            var pointer = 0
            while (true) {
                if (lap % 20 == 0) {
                    val ongoingTasks = digMaster.queryTasks(digMaster.taskQueue)
                    if (ongoingTasks == null) {
                        log.warn("did not get valid pending dig tasks, wait a moment ...")
                        TimeUnit.SECONDS.sleep(10)
                        log.warn("did not get valid pending dig tasks, wait done")
                        continue
                    }

                    if (ongoingTasks > 20) {
                        log.warn("work load is heavy, wait a moment ...")
                        TimeUnit.SECONDS.sleep(10)
                        log.warn("work load is heavy, wait done")
                        continue
                    }
                }
                val startId = pointer
                val endId = minOf(todoList.size, startId + 10)
                if (startId >= endId) break
                lap++
                val currentLap = lap
                log.info("review sublist: $startId, $endId, lap $currentLap")
                val req = SendListingIdReq(ids = todoList.subList(startId, endId))
                digMaster.sendTask(ObjectMapper().writeValueAsString(req), digMaster.taskQueue)
                if (pointer >= todoList.size) break
                pointer += 10
                TimeUnit.MILLISECONDS.sleep(100)
            }
        } else {
            log.info("Review history done")
        }
    }

    data class SendListingIdReq(
            val ids: List<Long> = mutableListOf()
    )

    companion object {
        private val log = LoggerFactory.getLogger(PendingReviewer::class.java)
        private val driverClass = "org.mariadb.jdbc.Driver"
        private var jdbcConnection: Connection

        init {
            try {
                Class.forName(driverClass)
                log.info("driver class loaded: ${driverClass}")
            } catch (e: ClassNotFoundException) {
                log.error("fail to find driver class: ${driverClass}")
                throw RuntimeException("fail to find driver class: ${driverClass}")
            }

            val src = DataSrcCfg(table = "PendingLoan")
            jdbcConnection = DriverManager.getConnection(src.url, src.user, src.password)
        }
    }
}
