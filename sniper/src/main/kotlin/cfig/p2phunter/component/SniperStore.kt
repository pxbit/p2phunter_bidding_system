package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2psniper.ppdsdk.PpdClient
import cfig.p2psniper.ppdsdk.PpdApp
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import org.springframework.boot.info.BuildProperties;
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Component
class SniperStore {
    private val log = LoggerFactory.getLogger("SniperStore")
    private val callbackUrl = ""
    var clientA: PpdClient? = null
    var clientB: PpdClient? = null
    var bidClient: PpdClient? = null
    var hunterClient: PpdClient? = null
    private val sP: SniperProps

    constructor(buildProp: BuildProperties, sniperProps: SniperProps) {
        sP = sniperProps
        sP.dumpInfo()
        buildProp.forEach {
            if (it.key == "time") {
                val ldt: LocalDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(System.currentTimeMillis()),
                        TimeZone.getDefault().toZoneId())
                log.info("props: " + it.key + ":" + ldt.toString())
            } else {
                log.info("props: " + it.key + ":" + it.value)
            }
        }

        try {
            clientA = genAppFromCfg(sP.fetcherApp[0])
            clientB = genAppFromCfg(sP.fetcherApp[1])
            bidClient = genAppFromCfg(sP.getBiddingApp())
            hunterClient = genAppFromCfg("key_hunter")

            if (clientA == null) {
                throw NullPointerException("clientA")
            }
            if (clientB == null) {
                throw NullPointerException("clientB")
            }
            if (bidClient == null) {
                throw NullPointerException("bidClient")
            }
        } catch (e: Exception) {
            log.error(e.toString())
            System.exit(2)
        }
    }

    private fun genAppFromCfg(appDir: String): PpdClient {
        log.info("Parsing App config from $appDir ...")
        val goldName = String(Files.readAllBytes(Paths.get("config/$appDir/name"))).trim()
        val goldPrivk = String(Files.readAllBytes(Paths.get("config/$appDir/privk"))).trim()
        val goldPubk = String(Files.readAllBytes(Paths.get("config/$appDir/pubk"))).trim()
        val goldAppId = String(Files.readAllBytes(Paths.get("config/$appDir/appid"))).trim()
        val goldClient = PpdClient(PpdApp(goldAppId, goldPubk, goldPrivk, callbackUrl, goldName))
        log.info("Parsing App config from $appDir Finished")
        return goldClient
    }
}
