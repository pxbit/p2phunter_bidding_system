package cfig.rabbit

import cfig.p2phunter.component.DetailFetcher
import cfig.p2phunter.th.SniperResp
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.Charset

class DigReceiver(inMode: Int) : RabbitCfg(inMode) {
    private val taskQueue = "dig.task"
    private val resultQueue = "dig.result"
    private val log = LoggerFactory.getLogger(DigReceiver::class.java)

    override fun prep() {
        super.prep()
        channel.queueDeclare(taskQueue, false, false, false, null)
        channel.queueDeclare(resultQueue, false, false, true, null)

        channel.queueBind(resultQueue, EXCHANGE_NAME, resultQueue)
    }

    fun recvResult(detailFetcher: DetailFetcher) {
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope,
                                        properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body, Charset.forName("UTF-8"))
                val resp = ObjectMapper().readValue(message, SniperResp.PreBidResp::class.java)
                if (resp.loanList.isNotEmpty()) {
                    log.info("Received ${resp.loanList.size} loans")
                }
                if (resp!!.loanList.isNotEmpty()) {
                    val idList: MutableList<Long> = mutableListOf()
                    resp.loanList.mapTo(idList) { it.ListingId }
                    resp.loanList.forEach {
                        detailFetcher.maybeSaveListing(it)
                        detailFetcher.maybeDeletePending(it)
                    }
                } else {
                    //empty
                }
            }
        }

        ctag = channel.basicConsume(resultQueue, true, consumer)
    }
}
