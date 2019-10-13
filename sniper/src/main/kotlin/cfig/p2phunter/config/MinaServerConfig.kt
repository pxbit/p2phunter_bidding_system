package cfig.p2phunter.config

import cfig.component.ObjHandler
import org.apache.mina.core.service.IoAcceptor
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.filter.logging.MdcInjectionFilter
import org.apache.mina.transport.socket.nio.NioSocketAcceptor
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
class MinaServerConfig {
    private val log = LoggerFactory.getLogger(MinaServerConfig::class.java)

    //server
    @Bean(initMethod = "bind", destroyMethod = "unbind")
    fun getNioSocketAcceptor(
            mdcFilter: MdcInjectionFilter,  //filters will be injected
            protocalFilter: ProtocolCodecFilter,
            logFilter: LoggingFilter,
            objHandler: ObjHandler): IoAcceptor { //handler is injected
        log.info("getNioSocketAcceptor() start")
        val PORT = 4570
        if (serverInited.getAndSet(true)) {
            log.error("Server already there, abort")
            System.exit(1)
        }
        if (!isPortAvailable(PORT)) {
            log.error("Port unavailable")
            System.exit(2)
        }
        val acceptor: IoAcceptor = NioSocketAcceptor() //object used to listen for incoming connections

        acceptor.getFilterChain().addLast("mdc", mdcFilter)
        acceptor.getFilterChain().addLast("protocol", protocalFilter)
        //acceptor.getFilterChain().addLast("logger", logFilter) //filters

        acceptor.setHandler(objHandler) //handler must implement IoHandler interface

        acceptor.getSessionConfig().setReadBufferSize(2048) //other configs
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60)

        acceptor.setDefaultLocalAddress(InetSocketAddress(PORT))

        log.info("getNioSocketAcceptor() returned")
        return acceptor
    }

    fun isPortAvailable(inPort: Int): Boolean {
        var ret = false
        try {
            ServerSocket(inPort).use {
                //AutoClosable
                ret = true
            }
        } catch (e: java.net.BindException) {
            log.warn("BindException(port=$inPort): ${e.message}")
        }
        return ret
    }

    companion object {
        private var serverInited = AtomicBoolean(false)
    }
}
