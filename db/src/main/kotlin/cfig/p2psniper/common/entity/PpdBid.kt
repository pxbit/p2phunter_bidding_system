package cfig.p2psniper.common.entity

import java.sql.Timestamp
import javax.persistence.*

@Entity
data class PpdBid(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var investId: Long = 0,

        var listingId: Long = 0,

        var amount: Float = 0f,

        var eAmount: Float = 0f,

        var message: String? = null,

        var ruleId: Long? = null,

        var userId: Int? = null,

        var src: Short = 0,

        //deprecate mysql specific table definition syntax
        //@Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
        var XTime: Timestamp = Timestamp(System.currentTimeMillis())
)
