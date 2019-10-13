package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Test

import org.junit.Assert.*

class RefreshTokenRespTest {
    @Test
    fun getAccessToken() {
        val s = """
{"AccessToken":"eb598c99-d448-4991-8892-11cd06abe578","RefreshToken":"44b954c6-738c-409a-af4a-b19054e98279","ExpiresIn":604800}
            """
        val o = ObjectMapper().registerKotlinModule().readValue(s.trim(), PpdResp.RefreshTokenResp::class.java)
        println(o)
    }
}
