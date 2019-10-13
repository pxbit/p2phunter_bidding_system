package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.annotation.JsonProperty

class AuthInfo {
    //错误信息
    @JsonProperty("ErrMsg")
    var errMsg: String = ""

    //用户在第三方唯一标识
    @JsonProperty("OpenID")
    var openID: String = ""

    //访问令牌
    @JsonProperty("AccessToken")
    var accessToken: String = ""

    //刷新令牌
    @JsonProperty("RefreshToken")
    var refreshToken: String = ""

    //超时时间，单位秒
    @JsonProperty("ExpiresIn")
    var expiresIn: Int = 0
}
