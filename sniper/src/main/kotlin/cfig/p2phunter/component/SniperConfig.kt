package cfig.p2phunter.component

import cfig.p2psniper.common.sdk2.Pacer
import org.springframework.stereotype.Component

@Component
class SniperConfig {
    val statusPacer = Pacer(10_1000, 47, 500L) //300 times/min
}