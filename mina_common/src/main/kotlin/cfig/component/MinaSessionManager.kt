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
import org.springframework.beans.factory.annotation.Autowired
import cfig.Jargon

@Component
class MinaSessionManager {
    companion object {
        private val log = LoggerFactory.getLogger(MinaSessionManager::class.java)
    }
    private val theSessionMap: HashMap<Int, IoSession?> = HashMap()

    @Autowired
    private val conn: NioSocketConnector? = null

    init {
        log.info("session manager started ...")
    }

    fun getSession(inJargon: Jargon): IoSession? {
        var aSession: IoSession? = null
        synchronized(this) {
            val remoteAddress = InetSocketAddress(inJargon.theDstIP, inJargon.theDstPort)
            aSession = theSessionMap.get(remoteAddress.hashCode())
            val marker = remoteAddress.address.hostAddress
            if (aSession != null) {
                if (aSession!!.isConnected) {
                    log.debug("[$marker] found valid session")
                    return aSession
                } else {
                    log.info("[$marker] found invalid session")
                    theSessionMap.remove(remoteAddress.hashCode())
                }
            } else {
                log.info("[$marker] session not found")
            }
            var future: ConnectFuture?
            try {
                future = conn!!.connect(remoteAddress)
                future!!.awaitUninterruptibly()
                aSession = future.getSession()
            } catch (e: InterruptedException) {
                log.warn("[$marker] Messenger is Interrupted {}", remoteAddress.toString())
            } catch (e: RuntimeException) {
                log.warn("[$marker] RuntimeException: " + remoteAddress.toString() + ", ErrMsg:[" + e.message + "], Cause:[" + e.cause + "]")
            } catch (e: Exception) {
                log.warn("[$marker] Can not connect to " + remoteAddress.toString() + ", ErrMsg:[" + e.message + "], Cause:[" + e.cause + "]")
            } finally {
                log.info("[$marker] Session re-connect " + (if (aSession == null) "failed" else "success"))
            }
            aSession.let { theSessionMap.put(remoteAddress.hashCode(), aSession) }
        }
        return aSession
    }
}
