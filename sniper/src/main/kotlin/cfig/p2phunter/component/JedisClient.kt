package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.HunterResp
import cfig.p2phunter.th.SendListingIdReq2
import cfig.p2psniper.ppdsdk.PpdServiceBase
import cfig.p2psniper.ppdsdk.params.PpdResp
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.concurrent.TimeUnit

@Component
class JedisClient(sniperProps: SniperProps) {
    final var jd: Jedis? = null
    private final var jdSub: Jedis? = null
    final var jdPubDetail: Jedis? = null
    final var jdAI: Jedis? = null
    private val sP = sniperProps
    private val log = LoggerFactory.getLogger(JedisClient::class.java)
    val mapper = ObjectMapper()
    val mapper2 = ObjectMapper()

    @Autowired
    private var fullPageFetcher: FullPagesFetcher? = null

    @Autowired
    private val aiConsumer: AiConsumer? = null

    init {
        when (sniperProps.redisMode) {
            0 -> {//disabled
                log.info("Jedis disabled")
            }
            1 -> {//dev
                log.info("Jedis running in dev mode")

            }
            2 -> {//production
                log.info("Jedis running in production mode")
            }
        }

        //[pub/sub]
        jd = getJedis()
        jdSub = getJedis()
        //[pub] detail
        jdPubDetail = getJedis()
        //[sub] AI
        jdAI = getJedis()

        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()

        if (sniperProps.redisMode != 0) {
            Thread(Runnable {
                TimeUnit.SECONDS.sleep(5)
                subscribeToListings()
                subscribeToAI()
            }).start()
        }
    }

    private fun getJedis(): Jedis? {
        try {
            when (sP.redisMode) {
                0 -> {//disabled
                    log.warn("jedis is disabled")
                    return null
                }
                1 -> {//dev
                    val innerJd = Jedis("localhost", 6379)
                    innerJd.select(4)
                    log.warn("jedisClient created in dev mode")
                    return innerJd
                }
                2 -> {//production
                    val innerJd = Jedis("red_host", 6742)
                    innerJd.auth("your_redis_password")
                    innerJd.select(4)
                    log.warn("jedisClient created in production mode")
                    return innerJd
                }
                else -> {
                    log.error("jedisClient config error")
                    return null
                }
            }
        } catch (e: Exception) {
            log.error("Fail to get redisClient: ${e.message}, cause=${e.cause}")
            return null
        }
    }

    fun publish(inIds: List<PpdResp.LoanListResp.SingleListingResp>) {
        jd?.let {
            val new2 = SendListingIdReq2()
            new2.ids = inIds
            val jsonStr = mapper2.writeValueAsString(new2)
            jd?.publish(sP.jedisChannel, jsonStr)
        }
    }

    //publish to AI strategy generator
    fun publishLoanDetail(detail: PpdResp.LoanInfoResp) {
        if (sP.redisMode == 0 || sP.detailChannel.isBlank()) {
            return
        }
        Thread(Runnable {
            try {
                val creditLoanList = detail.LoanInfos!!.filterNot { it.CreditCode.equals("AA") }
                if (!creditLoanList.isEmpty()) {
                    log.info("Publish to AI: ${detail.LoanInfos?.size}->${creditLoanList.size} loans")
                    val jsonStr = mapper2.writeValueAsString(creditLoanList)
                    if (jdPubDetail == null || !jdPubDetail!!.isConnected) {
                        log.error("creating new jedisClient")
                        jdPubDetail = getJedis()
                    }
                    jdPubDetail!!.publish(sP.detailChannel, jsonStr)
                    val idList = mutableListOf<Long>()
                    creditLoanList.forEach { idList.add(it.ListingId) }
                    log.info("Publish to AI: Done. <$idList>")
                } else {
                    log.info("Publish to AI: -")
                }
            } catch (e: Exception) {
                log.error("Exception: " + e.message + ", cause=" + e.cause)
            }
        }).start()
    }

    fun subscribeToListings() {
        Thread(Runnable {
            log.info("Subscribe to Redis channel for loanInfo: [${sP.jedisChannel}]")
            jdSub!!.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String?, message: String?) {
                    val msgData = mapper.readValue(message, SendListingIdReq2::class.java)
                    fullPageFetcher!!.onListingReceived4(msgData.ids, "redis", FullPagesFetcher.NewListingSrc.REDIS)
                }
            }, sP.jedisChannel)
        }).start()
    }

    fun subscribeToAI() {
        if (sP.redisMode == 0 || sP.aiChannel.isBlank()) {
            return
        }
        Thread(Runnable {
            log.info("Subscribe to Redis channel for AI: [${sP.aiChannel}]")
            jdAI!!.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String?, message: String?) {
                    val msgData = mapper.readValue<List<HunterResp.AiRule>>(message!!)
                    log.info("AI data: ${msgData.size} rules: $msgData")
                    msgData.forEach {
                        Thread(Runnable {
                            aiConsumer!!.consumeAiResults(it)
                        }).start()
                    }
                }
            }, sP.aiChannel)
        }).start()
    }
}
