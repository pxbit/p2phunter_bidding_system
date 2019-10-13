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
class MinaConfig {
    //filter
    @Bean
    fun getLoggingFilter(): LoggingFilter {
        return LoggingFilter()
    }

    //filter
    @Bean
    fun getProtocolCodecFilter(objCodec: ObjectSerializationCodecFactory): ProtocolCodecFilter  {
        return ProtocolCodecFilter(objCodec)
    }

    //filter
    @Bean
    fun getMdcInjectionFilter(): MdcInjectionFilter {
        return MdcInjectionFilter()
    }

    //codec
    @Bean
    fun getObjectSerializationCodecFactory(): ObjectSerializationCodecFactory {
        return ObjectSerializationCodecFactory()
    }
}
