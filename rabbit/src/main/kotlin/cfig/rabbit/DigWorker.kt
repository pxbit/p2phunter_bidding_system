package cfig.rabbit

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class DigWorker : RabbitCfg() {
    private val taskQueue = "dig.task"
    private val resultQueue = "dig.result"
    private val statusTaskQueue = "status.task"
    private val log = LoggerFactory.getLogger(DigWorker::class.java)
    private val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]")
    private val rand = Random()

    fun fetchStatusTasks() {
        channel.queueDeclare(taskQueue, false, false, false, null)
        channel.queueDeclare(statusTaskQueue, false, false, false, null)
        channel.queueBind(taskQueue, EXCHANGE_NAME, taskQueue)
        channel.queueBind(statusTaskQueue, EXCHANGE_NAME, statusTaskQueue)
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope,
                                        properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body, Charset.forName("UTF-8"))
                log.info(" [x] Received '$message'")
                executeTaskWithAckNB(message, envelope.deliveryTag)
            }
        }

        ctag = channel.basicConsume(statusTaskQueue, false, consumer)
        log.info("[$statusTaskQueue]Consumer up  : $ctag")
    }

    private fun executeTaskWithAckNB(msg: String, deliveryTag: Long) {
        Thread(Runnable {
            TimeUnit.SECONDS.sleep(10L + rand.nextInt(10))
            val data = dtf.format(LocalDateTime.now()) + " " + msg
            //FIXME
            //channel.basicPublish(EXCHANGE_NAME, resultQueue, null, ("RESULT: $data").toByteArray())
            //FIXME
            log.info("Post result: $data")
            //manual ack
            channel.basicAck(deliveryTag, false)
        }).start()
    }
}
