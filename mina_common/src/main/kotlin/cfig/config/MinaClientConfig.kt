package cfig.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.logging.MdcInjectionFilter
import org.apache.mina.transport.socket.nio.NioSocketAcceptor
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.apache.mina.core.service.IoAcceptor
import org.apache.mina.core.session.IdleStatus
import java.net.InetSocketAddress
import cfig.component.ObjHandler

@Configuration
class MinaClientConfig {
    @Bean
    fun getNioSocketConnector(
            mdcFilter: MdcInjectionFilter,
            protocalFilter: ProtocolCodecFilter,
            logFilter: LoggingFilter,
            objHandler: ObjHandler): NioSocketConnector {
        val conn = NioSocketConnector()
        conn.setConnectTimeoutMillis(2000)
        conn.getFilterChain().addLast("mdc", mdcFilter)
        conn.getFilterChain().addLast("codec", protocalFilter)
        //conn.getFilterChain().addLast("logger", logFilter)
        conn.setHandler(objHandler)
        return conn
    }
}
