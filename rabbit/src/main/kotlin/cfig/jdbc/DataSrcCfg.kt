package cfig.jdbc

data class DataSrcCfg(
        val url: String = "jdbc:mysql://n4.cfig.me:3306/sniperkt",
        val user: String = "your_name",
        val password: String = "your_password",
        var table: String? = null
) {
//    val url: String = "jdbc:mysql://localhost:3306/sniperkt",
}
