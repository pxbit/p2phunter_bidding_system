package cfig.p2psniper.common.entity

import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by cfig (yuyezhong@gmail.com)) on 3/21/2018.
 */
@Entity
data class PendingLoan(
        @Id var ListingId: Long = 0,
        var dead: Boolean = false
)
