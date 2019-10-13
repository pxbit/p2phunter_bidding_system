package cfig.rabbit

import cfig.p2phunter.component.LoanDigger
import cfig.p2phunter.th.SendListingIdReq
import cfig.p2phunter.th.SniperResp
import com.fasterxml.jackson.databind.ObjectMapper
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

class DigWorker(inMode: Int) : RabbitCfg(inMode) {
    private val taskQueue = "dig.task"
    private val resultQueue = "dig.result"
    private val log = LoggerFactory.getLogger(DigWorker::class.java)

    fun fetchTasks(loanDigger: LoanDigger) {
        channel.queueDeclare(taskQueue, false, false, false, null)
        channel.queueBind(taskQueue, EXCHANGE_NAME, taskQueue)
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope,
                                        properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body, Charset.forName("UTF-8"))
                log.debug(" [x] Received task '$message'")
                executeTaskWithAckNB(message, envelope.deliveryTag, loanDigger)
            }
        }

        ctag = channel.basicConsume(taskQueue, false, consumer)
        log.info("Consumer up  : $ctag")
    }

    private fun executeTaskWithAckNB(msg: String, deliveryTag: Long, loanDigger: LoanDigger) {
        Thread(Runnable {
            val shadowList = ObjectMapper().readValue(msg, SendListingIdReq::class.java)
            val loanList = loanDigger.preDig(shadowList.ids)
            val ret = SniperResp.PreBidResp(loanList)
            channel.basicPublish(EXCHANGE_NAME, resultQueue, null, ObjectMapper().writeValueAsString(ret).toByteArray())
            if (ret.loanList.isNotEmpty()) {
                log.info("Post result: ${ret.loanList.size} loan details")
            }
            //manual ack
            channel.basicAck(deliveryTag, false)
        }).start()
    }
}
