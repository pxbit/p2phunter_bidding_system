package cfig.p2psniper.common.entity

import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/5/17
 */
@Entity
@Table(name = "ppd_user")
data class PpdUser(
        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
        //@GeneratedValue will change our preset field 'UserId'
        var UserId: Int = 0,

        @NotNull
        var UserName: String = "",
        var PhoneNumber: Int = 0,
        var AccessToken: String? = null,
        var RefreshToken: String? = null,
        var OpenID: String = "",
        var UserBalance: Float = 0f,
        var ATExpireDate: Timestamp? = null,
        var RTExpireDate: Timestamp? = null,
        var CreateTime: Timestamp = Timestamp(System.currentTimeMillis()),
        //Nick Name
        var nickName: String? = null,
        //雕币余额
        var Score: Int = 0,

        //保留余额
        var holdBalance: Float? = null,
        //起投利率
        var MinRate: Float? = null,
        //最小月数
        var MinMonth: Int? = null,
        //最大月数
        var MaxMonth: Int? = null,

        @NotNull
        //@Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
        var UpdateTime: Timestamp = Timestamp(System.currentTimeMillis())
) {
    fun toMiniString(): String {
        return "PpdUser[Id=$UserId, AT=$AccessToken, Balance=${UserBalance}]"
    }
}
