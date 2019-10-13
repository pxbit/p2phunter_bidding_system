package cfig.p2psniper.common.entity

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class PpdBid2(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var listingId: Long = 0,

        var orderId: String? = null,

        var amount: Float = 0f,

        var eAmount: Float = 0f,

        var couponAmount: Float = 0f,

        var message: String? = null,

        var bidId: Long = 0,

        var ruleId: Long? = null,

        var userId: Int? = null,

        var src: Short = 0,

        var appIndex: Int = 0,

        var checked: Short = 0,

        var code: Int = 0,

        var XTime: Timestamp = Timestamp(System.currentTimeMillis())
) {
        enum class ConsumeSrc {
                FETCHER,
                DATABASE,
                DIGGER,
                AI
        }
}
