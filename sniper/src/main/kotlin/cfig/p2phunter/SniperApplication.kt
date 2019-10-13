package cfig

import cfig.p2phunter.config.SniperProps
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.io.File
import java.lang.management.ManagementFactory

@SpringBootApplication
@EnableConfigurationProperties(SniperProps::class)
class SniperApplication

fun main(args: Array<String>) {
    File("run.pid").printWriter().use {
        it.print(getPid())
    }
    SpringApplication.run(SniperApplication::class.java, *args)
}

fun getPid(): Int {
    val runtime = ManagementFactory.getRuntimeMXBean()
    val name = runtime.name // format: "pid@hostname"
    return try {
        Integer.parseInt(name.substring(0, name.indexOf('@')))
    } catch (e: Exception) {
        -1
    }
}
