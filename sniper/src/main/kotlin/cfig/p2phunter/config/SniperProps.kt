package cfig.p2phunter.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope

@ConfigurationProperties(prefix = "sniper")
@RefreshScope
class SniperProps {
    //work mode
    var mode: String = "hunter_slave" //"standalone"

    //[Multi Auth] app index
    var appIndex: Int = -1

    //fetch
    var fetchEnable: Boolean = false
    var mina_peer: String = ""
    var fetcherApp: MutableList<String> = mutableListOf()
    var threads: Int = 2

    //pre-fetch (listingId discover)
    var preFetchEnable: Boolean = false
    var preFetchClients: MutableList<String> = mutableListOf()

    //bid
    var bidEnable: Boolean = false

    //redis working mode
    //  mode 0: disabled
    //  mode 1: dev
    //  mode 2: production
    var redisMode: Int = 0

    //rabbit working mode:
    //  mode 0: disabled
    //  mode 1: dev
    //  mode 2: production
    var rabbitMode: Int = 0

    //hunter: -1: all users, 0: user group 0, 1: user group 1
    var userGroup: Int = -1

    //loanInfo publication
    var jedisChannel: String = "listingid"

    //publish loan detail to this channel
    //  if enabled, should be set to "channelDetailInfo"
    //  disabled by default, set to ""
    var detailChannel: String = ""

    //consume AI strategy generator
    //  if enabled, should be set to "channelAiStrategy"
    //  disabled by default, set to ""
    var aiChannel: String = ""

    var useBiddingQueue: Boolean = false

    private fun strMode(mode: Int): String {
        return when (mode) {
            0 -> "disabled"
            1 -> "dev"
            2 -> "production"
            else -> "ERROR"
        }
    }

    private fun appIndexToName(index: Int): String {
        return "key_" + when (index) {
            1 -> "hunter"
            2 -> "paizhitou"
            3 -> "funbid"
            4 -> "gold"
            5 -> "p2psniper"
            6 -> "tide"
            7 -> "twisted"
            8 -> "zidingyi"
            9 -> "driver"
            10 -> "wangdai"
            else -> "somethingwrong"
        }
    }

    fun getBiddingApp(): String {
        return appIndexToName(this.appIndex)
    }

    fun dumpInfo() {
        log.info("property dump start")
        log.info("  Standalone Mode: ${this.isStandAlone()}")
        log.info("  prefetch: $preFetchEnable, prefetch-clients: $preFetchClients")
        log.info("  bidEnable: $bidEnable, bid-app: ${getBiddingApp()}, " + if (bidEnable) "useBiddingQueue=$useBiddingQueue" else "")
        log.info("  userGroup: $userGroup")
        log.info("  fetchEnable: $fetchEnable; mina_peer: $mina_peer; fetcherApp: $fetcherApp; thread: $threads")
        log.info("  appIndex: $appIndex")
        log.info("  redis mode : $redisMode, ${strMode(redisMode)}")
        if (redisMode in setOf(1, 2)) {
            log.info("      [pub/sub] channel: $jedisChannel")
            log.info("      [pub/   ] detail:  $detailChannel, status: " + if (detailChannel.isBlank()) "disabled" else "enabled")
            log.info("      [   /sub] channel: $aiChannel, status: " + if (aiChannel.isBlank()) "disabled" else "enabled")
        }
        log.info("  rabbit mode: $rabbitMode, ${strMode(rabbitMode)}")
        log.info("property dump end")
    }

    fun isStandAlone(): Boolean {
        return mode == "standalone"
    }

    companion object {
        private val log = LoggerFactory.getLogger(SniperProps::class.java)
    }
}
