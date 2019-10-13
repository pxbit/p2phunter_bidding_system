package cfig.p2psniper.common.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

//@Entity
data class CustomCreditRule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var amountA: Float? = null,
        var amountB: Float? = null,

        var monthsA: Int? = null,
        var monthsB: Int? = null,

        var rateA: Float? = null,
        var rateB: Float? = null,

        var code: String = "ABCDEF",

        //6 certs
        var certDegree: Boolean? = null,
        var certStudent: Boolean? = null,
        var certCredit: Boolean? = null,
        var certNciic: Boolean? = null,
        var certVideo: Boolean? = null,
        var certPhone: Boolean? = null,

        var studyStyle: Int? = null,
        var schoolLevel: Int? = null
)
