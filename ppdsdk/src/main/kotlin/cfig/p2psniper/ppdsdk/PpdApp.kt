package cfig.p2psniper.ppdsdk

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure
import org.slf4j.LoggerFactory
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

class PpdApp {
    companion object {
        private val rsaKeyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        private val log = LoggerFactory.getLogger(PpdApp::class.java)
    }

    var ppdPubKey: PublicKey? = null
    var ourPrivKey: PrivateKey? = null
    val appId: String
    val callbackUrl: String
    val nickName: String

    constructor(inAppId: String, inPpdPubKey: String, inOurPrivKey: String, inCallbackUrl: String, inNickName: String = "") {
        if (inAppId.isNullOrBlank() || inPpdPubKey.isNullOrBlank() || inOurPrivKey.isNullOrBlank()) {
            throw IllegalArgumentException("parameter invalid")
        }
        this.callbackUrl = inCallbackUrl.trim()
        this.appId = inAppId.trim()
        this.nickName = inNickName
        var publicKey = inPpdPubKey
        var privateKey = inOurPrivKey
        publicKey = tremEnter(publicKey)
        privateKey = tremEnter(privateKey)

        //PEM转ASN1
        if (publicKey.startsWith("----")) {
            val fidx = publicKey.indexOf("\n")
            val lidx = publicKey.lastIndexOf("\n")
            publicKey = publicKey.substring(fidx + 1, lidx).replace("\n".toRegex(), "")
        }
        //初始化公钥
        val pubKeyBytes = Base64.getDecoder().decode(publicKey)
        val x509EncodedKeySpec = X509EncodedKeySpec(pubKeyBytes)
        this.ppdPubKey = PpdApp.rsaKeyFactory.generatePublic(x509EncodedKeySpec)

        //初始化私钥
        if (privateKey.startsWith("----")) {
            val fidx = privateKey.indexOf("\n")
            val lidx = privateKey.lastIndexOf("\n")
            privateKey = privateKey.substring(fidx + 1, lidx).replace("\n".toRegex(), "")
        }
        val keyBytes = Base64.getDecoder().decode(privateKey)

        try {//pkcs8
            val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(keyBytes)
            this.ourPrivKey = rsaKeyFactory.generatePrivate(pkcs8EncodedKeySpec)
        } catch (e: InvalidKeySpecException) {//pkcs1
            val asn1PrivKey = RSAPrivateKeyStructure(ASN1Sequence.fromByteArray(keyBytes) as ASN1Sequence)
            val rsaPrivKeySpec = RSAPrivateKeySpec(asn1PrivKey.modulus, asn1PrivKey.privateExponent)
            this.ourPrivKey = rsaKeyFactory.generatePrivate(rsaPrivKeySpec)
        }
        log.debug("Loaded priv key with type " + this.ourPrivKey!!.format)
        log.debug("Loaded pub  key with type " + this.ppdPubKey!!.format)
    }

    private fun tremEnter(str: String): String {
        var fidx = 0
        var lidx = str.length - 1
        while (fidx < str.length) {
            if (str[fidx] != '\n') break
            fidx++
        }
        while (lidx >= 0) {
            if (str[lidx] != '\n') break
            lidx--
        }
        return if (fidx > lidx) "" else str.substring(fidx, lidx + 1)
    }
}
