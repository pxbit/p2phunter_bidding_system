package cfig.p2psniper.common.utils

import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/8/17
 */
object LogHelper {
    fun dump(tag: String, e: Exception) {
        val log = LoggerFactory.getLogger(tag)
        val sw: StringWriter = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        log.error(sw.toString())
    }

    fun dump(tag: String, e: Throwable) {
        val log = LoggerFactory.getLogger(tag)
        val sw: StringWriter = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        log.error(sw.toString())
    }
}
