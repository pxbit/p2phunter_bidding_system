package cfig.rabbit

import com.rabbitmq.client.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.TimeoutException

open class RabbitCfg {
    val EXCHANGE_NAME = "prefetch"

    private lateinit var conn: Connection
    protected lateinit var factory: ConnectionFactory
    lateinit var channel: Channel
    var ctag: String? = null
    private val log = LoggerFactory.getLogger(RabbitCfg::class.java)

    @Throws(IOException::class, TimeoutException::class)
    open fun prep(connectionName: String? = null) {
        val conProp = if (connectionName == null) mapOf<String, String>() else mapOf<String, String>("connection_name" to connectionName)
        factory = ConnectionFactory().apply {
//            this.host = "192.168.31.174"
            this.host = "s1.cfig.me"
            this.port = 5672
            this.username = "your_name"
            this.password = "your_password"
            this.virtualHost = "sniper"
            this.setNetworkRecoveryInterval(8*1000) //ms
            this.clientProperties = conProp
        }

        conn = factory.newConnection()
        channel = conn.createChannel()
        channel.basicQos(2)

        (channel as Recoverable).addRecoveryListener(object: RecoveryListener {
            override fun handleRecoveryStarted(recoverable: Recoverable?) {
                log.info("recovery: starting to recover channel [${(recoverable as Channel).channelNumber}]...")
            }

            override fun handleRecovery(recoverable: Recoverable?) {
                log.info("recovery: done. channel [${(recoverable as Channel).channelNumber}]")
            }
        })

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC)
    }

    fun tearDown() {
        this.channel.let {
            if (ctag != null) {
                log.info("Cancelling consumer $ctag ...")
                it.basicCancel(ctag)
            }
            if (it.isOpen) {
                it.close()
            }
        }

        this.conn.let {
            if (it.isOpen) {
                it.close()
            }
        }
    }
}
