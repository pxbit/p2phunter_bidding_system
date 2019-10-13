package cfig.component

import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.session.IoSession
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Scope
import cfig.Jargon

@Component
@Scope("prototype")
class MessengerV2(val conn: NioSocketConnector) {
    @JvmOverloads
    fun sendNB(inJ: Jargon, retryNum: Int = 1, inDelayTime: Int = 0) {
        val aDelayTime = if ((inDelayTime < 0)) 0 else inDelayTime
        val r = object : Runnable {
            override fun run() {
                try {
                    TimeUnit.SECONDS.sleep(aDelayTime.toLong())
                    send(inJ, retryNum)
                } catch (e: InterruptedException) {
                    log.error("Messenger is interrupted ! SendMsg cancelled !")
                }
            }
        }
        Thread(r).start()
    }

    @JvmOverloads
    fun send(inJ: Jargon, inRetryNum: Int = 1): Boolean {
        var retryNum = inRetryNum
        var ret: Boolean
        val reportJar = inJ
        var targetAdd: InetSocketAddress?
        retryNum = if ((retryNum < 0)) 0 else retryNum
        val totalRetryNum = retryNum
        var reporterIP: InetAddress? = null
        var reporterPort = -1
        var future: ConnectFuture?
        var session: IoSession?

        try {
            reporterIP = reportJar.theDstIP
            reporterPort = reportJar.theDstPort
            targetAdd = InetSocketAddress(reporterIP, reporterPort)
        } catch (e1: Exception) {
            log.error("Error create InetSocketAddress(" + reporterIP!!.canonicalHostName + ", " + reporterPort + ")")
            return false
        }

        var i = 0
        do {
            if (i > 0) {
                log.info("Re-Sending " + i + "/" + totalRetryNum + "... -> " + reporterIP.hostAddress + ":" + (reporterPort).toString())
            }
            i++
            try {
                if (i > 0) {
                    i = if ((i > 7)) 7 else i
                    TimeUnit.SECONDS.sleep(Math.pow(3.0, (i - 1).toDouble()).toLong())
                }
                future = conn.connect(targetAdd)
                future!!.awaitUninterruptibly()
                session = future.getSession()
                if (null != session) {
                    session.setAttribute("attribute1", "this_is_1")
                    session.write(reportJar)
                    session.closeOnFlush()
                    session.getCloseFuture().awaitUninterruptibly()
                    ret = true
                } else {
                    log.warn("Can not get session")
                    ret = false
                }
            } catch (e: InterruptedException) {
                log.warn("Messenger is Interrupted {}", targetAdd.toString())
                retryNum = 0
                ret = false
            } catch (e: RuntimeException) {
                log.warn("RuntimeException: " + targetAdd.toString() + ", ErrMsg:[" + e.message + "], Cause:[" + e.cause + "]")
                ret = false
            } catch (e: Exception) {
                log.warn("Can not connect to " + targetAdd.toString() + ", ErrMsg:[" + e.message + "], Cause:[" + e.cause + "]")
                ret = false
            } finally {
                // null
            }
            retryNum--
        } while ((false == ret) && (retryNum >= 0))

        return ret
    }

    companion object {
        private val log = LoggerFactory.getLogger(MessengerV2::class.java)
    }
}
