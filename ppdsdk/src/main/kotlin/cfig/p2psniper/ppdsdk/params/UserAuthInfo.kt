package cfig.p2psniper.ppdsdk.params

import java.sql.Timestamp

/**
 * Created by cfig (yuyezhong@gmail.com)) on 8/8/17.
 */
data class UserAuthInfo(
        var openID: String = "",
        var accessToken: String = "",
        var refreshToken: String = "",
        var expireTs: Timestamp = Timestamp(System.currentTimeMillis()),
        var refreshTokenExpireTs: Timestamp = Timestamp(System.currentTimeMillis() + 7776000000)) {
    constructor(openID: String, accessToken: String, refreshToken: String, expireTs: Timestamp)
            : this(openID, accessToken, refreshToken, expireTs, Timestamp(System.currentTimeMillis() + 7776000000))

    fun refreshToken(authInfo: AuthInfo): Unit {
        this.accessToken = authInfo.accessToken
        this.expireTs = Timestamp(System.currentTimeMillis() + (authInfo.expiresIn - 300) * 1000)
    }

    companion object {
        fun fromAuthInfo(authInfo: AuthInfo): UserAuthInfo {
            return UserAuthInfo(
                    authInfo.openID,
                    authInfo.accessToken,
                    authInfo.refreshToken,
                    Timestamp(System.currentTimeMillis() + (authInfo.expiresIn - 300) * 1000)) //5min earlier to be safe
        }

/*
        @Throws(NullPointerException::class)
        fun fromUser(u: PpdUser): UserAuthInfo {
            return UserAuthInfo(u.OpenID, u.AccessToken!!, u.RefreshToken!!, u.ATExpireDate!!, u.RTExpireDate!!)
        }
*/
    }
}
