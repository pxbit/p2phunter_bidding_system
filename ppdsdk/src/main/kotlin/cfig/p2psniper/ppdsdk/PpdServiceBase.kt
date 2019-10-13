package cfig.p2psniper.ppdsdk

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import java.security.Signature
import java.util.*
import javax.crypto.Cipher

open class PpdServiceBase(val ppdApp: PpdApp) {
    companion object {
        val log = LoggerFactory.getLogger(PpdServiceBase::class.java)
        val mapper = ObjectMapper().registerKotlinModule()
        val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")
        val gwurl = "https://openapi.ppdai.com"
    }

    fun getRequestTemplate(url: String, reqObj: Any?, acToken: String? = null, needSign: Boolean = true): Request {
        //ts
        val ts = System.currentTimeMillis() / 1000L
        val tsSign = String(this.signToBase64((ppdApp.appId + ts).toByteArray()))
        //.replace("\n", "").replace("\r", "")
        //request body
        val mapper2 = ObjectMapper()
        mapper2.propertyNamingStrategy = PpdNamingStrategy()
        val reqObjSign: String
        if (needSign) {
            val jsonString = this.getJsonValueString(reqObj)
            reqObjSign = String(this.signToBase64(jsonString.toByteArray()))
            //    .replace("\n", "").replace("\r", "")
            log.debug("hashString=|$jsonString|")
            log.debug("sign=|$reqObjSign|")
            log.debug("body=|$reqObj|")
        } else {
            reqObjSign = "SIGN"
        }
        val jsonBody = RequestBody.create(JSON_TYPE, if (reqObj == null) "{}" else mapper2.writeValueAsString(reqObj))
        val builder = Request.Builder()
                .url(url)
                .post(jsonBody)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("X-PPD-SIGNVERSION", "1")
                .addHeader("X-PPD-SERVICEVERSION", "1")
                .addHeader("X-PPD-APPID", ppdApp.appId)
                .addHeader("X-PPD-TIMESTAMP", ts.toString())
                .addHeader("X-PPD-TIMESTAMP-SIGN", tsSign)
                .addHeader("X-PPD-SIGN", reqObjSign)
        if (!acToken.isNullOrBlank()) {
            builder.addHeader("X-PPD-ACCESSTOKEN", acToken!!)
        }
        //for fix-IP hacking, disabled for now
        //builder.addHeader("Host", "openapi.ppdai.com")
        return builder.build()
    }

    class PpdNamingStrategy : PropertyNamingStrategy() {
        override fun nameForGetterMethod(config: MapperConfig<*>?, method: AnnotatedMethod?, defaultName: String?): String {
            return method!!.name.substring(3)
        }
    }

    /*
        加密
    */
    /*
    fun encryptWithPpdPubkey(data: ByteArray): ByteArray {
        val cip = Cipher.getInstance("RSA")
        cip.init(Cipher.ENCRYPT_MODE, ppdApp.ppdPubKey)
        return cip.doFinal()
    }
    */

    /*
        解密
     */
    private fun decryptWithOurPrivkey(data: ByteArray): ByteArray {
        val cip = Cipher.getInstance("RSA")
        cip.init(Cipher.DECRYPT_MODE, ppdApp.ourPrivKey)
        return cip.doFinal(data)
    }

    fun decryptBase64(data: String): String {
        return String(decryptWithOurPrivkey(Base64.getDecoder().decode(data)))
    }

    /*
        签名: 使用私钥签名
    */
    fun signToBase64(data: ByteArray): ByteArray {
        val sig = Signature.getInstance("SHA1withRSA")
        sig.initSign(ppdApp.ourPrivKey)
        sig.update(data)
        return Base64.getEncoder().encode(sig.sign())
    }

    /*
        验证ppd签名: 使用ppd公钥验证签名
     */
    fun verifyBase64(data: ByteArray, sig: String): Boolean {
        val sigInst = Signature.getInstance("SHA1withRSA")
        sigInst.initVerify(ppdApp.ppdPubKey)
        sigInst.update(data)
        return sigInst.verify(Base64.getDecoder().decode(sig))
    }

    @Throws(IllegalArgumentException::class)
    fun getJsonValueString(inObj: Any?): String {
        if (inObj == null) {
            return ""
        }
        val root: JsonNode = mapper.readTree(mapper.writeValueAsString(inObj))
        var keys: MutableList<String> = mutableListOf()
        root.fieldNames().forEach {
            keys.add(it)
        }
        keys.sort()
        val sb = StringBuilder()
        keys.forEach({
            val v: Any = root.get(it)
            when (v) {
                is BooleanNode -> {
                    log.debug("BooleanNode: $it:$v")
                    sb.append(it.toLowerCase())
                    sb.append(v.asInt())
                }
                is TextNode -> {
                    log.debug("TextNode: $it:$v")
                    sb.append(it.toLowerCase())
                    sb.append(v.toString().trim { c -> c == '"' })
                }
                is IntNode, is LongNode, is DoubleNode, is ObjectNode -> {
                    log.debug("Int/Long/Double/Object: $it:$v")
                    sb.append(it.toLowerCase())
                    sb.append(v)
                }
                is NullNode, is ArrayNode -> {
                    log.debug("NullNode, ArrayNode: $it:$v")
                    //do nothing
                }
                else -> throw IllegalArgumentException("${v::class} found in object")
            }
        })
        return sb.toString()
    }
}
