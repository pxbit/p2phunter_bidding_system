package cfig.p2psniper.ppdsdk

import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class PropFormatter {
    companion object {
        fun anyFormat(obj: Any): String {
            return when (obj) {
                is LocalDateTime -> obj.toEpochSecond(ZoneOffset.UTC).toString()
                is ZonedDateTime -> obj.toEpochSecond().toString()
                is Boolean -> if (obj) "1" else "0"
                is Float, is Double -> toByteArray(obj).toHexString()
                else -> obj.toString()
            }
        }

        private fun toByteArray(value: Any): ByteArray {
            return when (value) {
                is Float -> {
                    val bytes = ByteArray(4)
                    ByteBuffer.wrap(bytes).putFloat(value)
                    bytes
                }
                is Double -> {
                    val bytes = ByteArray(8)
                    ByteBuffer.wrap(bytes).putDouble(value)
                    bytes
                }
                else -> throw IllegalArgumentException("parameter must be float/double")
            }
        }

        fun ByteArray.toHexString(): String {
            var ret = ""
            for (b in this) {
                ret += String.format("%02X", b)
            }
            return ret
        }
    }
}
