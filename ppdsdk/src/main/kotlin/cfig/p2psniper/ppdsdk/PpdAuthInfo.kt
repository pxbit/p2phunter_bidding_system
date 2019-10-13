package cfig.p2psniper.ppdsdk

import com.fasterxml.jackson.annotation.JsonProperty

data class PpdAuthInfo(
        //错误信息
        @JsonProperty("ErrMsg")
        private var errMsg: String? = null,

        //用户在第三方唯一标识
        @JsonProperty("OpenID")
        private var openID: String? = null,

        //访问令牌
        @JsonProperty("AccessToken")
        private var accessToken: String? = null,

        //刷新令牌
        @JsonProperty("RefreshToken")
        private var refreshToken: String? = null,

        //超时时间，单位秒
        @JsonProperty("ExpiresIn")
        private var expiresIn: Int = 0
)
