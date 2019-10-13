package cfig.p2psniper.common.entity

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
data class MultiAuth(
        @EmbeddedId var theKey: MultiKey = MultiKey(),
        var userName: String? = null,
        var accessToken: String? = null
) {
    @Embeddable
    data class MultiKey(
            var appIndex: Int = 0,
            var userId: Int = 0
    ) : Serializable
}