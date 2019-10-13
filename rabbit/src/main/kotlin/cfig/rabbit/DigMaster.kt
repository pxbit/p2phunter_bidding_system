package cfig.rabbit

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import okhttp3.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class DigMaster : RabbitCfg() {
    val taskQueue = "dig.task"
    private val resultQueue = "dig.result"

    val statusTaskQueue = "status.task"
    private val log = LoggerFactory.getLogger(DigMaster::class.java)

    override fun prep(connectionName: String?) {
        super.prep(connectionName)
        channel.queueDeclare(taskQueue, false, false, false, null)

        channel.queueDeclare(resultQueue, false, false, true, null)
        channel.queueBind(resultQueue, EXCHANGE_NAME, resultQueue)

        //XStatus task
        channel.queueDeclare(statusTaskQueue, false, false, false, null)
    }

    fun recvResult() {
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope,
                                        properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body, Charset.forName("UTF-8"))
                log.info("Received Ret '$message'")
            }
        }

        ctag = channel.basicConsume(resultQueue, true, consumer)
    }

    fun sendTask(msg: String, queueName: String) {
        channel.basicPublish(EXCHANGE_NAME, queueName, null, msg.toByteArray())
    }

    fun sendTasks() {
        (1..1000L step 10).forEach {
            val data = "$it - ${it + 10 - 1}"
            channel.basicPublish(EXCHANGE_NAME, taskQueue, null, ("TASK: $data").toByteArray())
            log.info("Sent Task: $data")
            TimeUnit.MILLISECONDS.sleep(500)
        }
    }//end-of-function

    fun queryTasks(queueName: String): Int? {
        var ret: Int? = null
        val auth = object : Authenticator {
            override fun authenticate(route: Route, response: Response): Request? {
                val credential = Credentials.basic(factory.username, factory.password)
                return response.request().newBuilder().header("Authorization", credential).build()
            }
        }
        val client = OkHttpClient.Builder()
                .authenticator(auth)
                .build()

        val req = Request.Builder()
                .url("http://${super.factory.host}:8080/api/queues/sniper/$queueName")
                .get()
                .build()
        val resp = client.newCall(req).execute()
        if (resp.isSuccessful) {
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                log.debug("respJson: $respJson")
                val node = ObjectMapper().readTree(respJson)
                node.fields().iterator().forEach {
                    if (it.key == "message_bytes_unacknowledged") {
                        ret = Integer.parseInt(it.value.toString())
                    }
                }
            } else {
                log.error("loanDetail() got null body")
            }
        } else {
            log.error("API resp fail")
        }

        log.info("Query Task: $ret")
        return ret
    }
}
