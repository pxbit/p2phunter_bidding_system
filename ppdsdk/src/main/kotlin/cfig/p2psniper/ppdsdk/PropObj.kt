package cfig.p2psniper.ppdsdk

class PropObj(val k: String, val v: Any, private val sign: Boolean = true) {
    override fun toString(): String {
        return k.toLowerCase() + PropFormatter.anyFormat(v)
    }

    val needSign: Boolean
        get() = sign && v.toString().isNotBlank()
}
