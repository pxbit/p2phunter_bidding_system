package cfig.p2phunter.component

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import org.junit.Assert.*
import java.nio.file.Files
import java.nio.file.Paths

class SniperStoreTest {
    data class AppIdCfg(
            val fetcherApp: List<String> = listOf(),
            val biddingApp: String = "",
            val peer: String = "",
            val userGroup: Int = 0
    )

    @Test
    fun getGoldClient() {
        val workingDir = System.getProperty("user.dir")
        println(workingDir)
        val cfgStr = String(Files.readAllBytes(Paths.get("config/app.json")))
        val cfg = ObjectMapper().readValue(cfgStr, AppIdCfg::class.java)
        println(cfg.toString())
    }

    @Test
    fun getP2psniperClient() {
        val cfg = AppIdCfg(listOf("config/1", "config/2"), "config_app3")
        println(ObjectMapper().writeValueAsString(cfg))
    }

}
