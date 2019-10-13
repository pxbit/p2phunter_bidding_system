package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.HunterCreditRulesResp
import cfig.p2phunter.th.HunterCreditRulesResp.CreditRuleItem
import cfig.p2phunter.th.HunterDiyRulesResp
import cfig.p2phunter.th.HunterDiyRulesResp.HunterDiyRuleItem
import cfig.p2phunter.th.HunterResp
import cfig.p2phunter.th.HunterResp.*
import cfig.p2phunter.th.HunterResp.HunterUserSettingResp.HunterUserSettingItem
import cfig.p2psniper.common.entity.PpdUser
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import javax.annotation.PostConstruct

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/5/17
 */
@Component
class HunterUtils {
    private val log = LoggerFactory.getLogger(HunterUtils::class.java)
    private val hunterGw = "http://p2phunter.cn/home/api"

    @Autowired
    private val sniperStore: SniperStore? = null

    private val sP: SniperProps

    constructor(sniperProps: SniperProps) {
        sP = sniperProps
    }

    @PostConstruct
    fun postConstructFun() {
        log.info("post construct")
    }

    fun getUserInfo(index: Int): MutableList<PpdUser> {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\n", "").replace("\r", "")
        val ret: MutableList<PpdUser> = mutableListOf()

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "$index")
                .add("time", "$ts")
                .add("sign", "$signat")
                .build()
        val req = Request.Builder()
                .url("$hunterGw/apiGetUserInfo")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        val resp = client.newCall(req).execute()
        resp.body()?.let { respBody ->
            val respJson = respBody.string().trim()
            val userResp = ObjectMapper().readValue(respJson, HunterUserInfoResp::class.java)
            if (userResp.status == 0 && userResp.msg == "OK") {
                log.info("Recv ${userResp.userinfo.size} user info")
            } else {
                log.error("Server replied error")
            }
            userResp.userinfo.forEach { ret.add(it.toPpdUser()) }
        }

        return ret
    }

    fun getAARules(index: Int): HunterSafeRulesResp? {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\n", "").replace("\r", "")

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "${index}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiGetSysSafeStrategy")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        val resp = client.newCall(req).execute()
        log.info(resp.toString())
        resp.body()?.let { respBody ->
            val respJson = respBody.string().trim()

            val ruleResp = ObjectMapper().readValue(respJson, HunterSafeRulesResp::class.java)
            if (ruleResp.status == 0 && ruleResp.msg == "OK") {
                log.info("Recv ${ruleResp.sys.size} system AA rules")
                return ruleResp
            } else {
                log.error("Server replied error")
                return null
            }
        }
        //respBody is null
        log.error("getAARules() got null body")
        return null
    }

    fun getUserSettings(index: Int): List<HunterUserSettingItem> {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "${index}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiGetUserSetting")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        val resp = client.newCall(req).execute()
        log.info(resp.toString())
        var ret = mutableListOf<HunterUserSettingItem>()
        resp.body()?.let { respBody ->
            val respJson = respBody.string().trim()
            val respSetting = ObjectMapper().readValue(respJson, HunterUserSettingResp::class.java)
            if (respSetting.status == 0 && respSetting.msg == "OK") {
                log.info("Recv ${respSetting.setting.size} user settings")
                return respSetting.setting
            } else {
                log.error("Server replied error")
                return ret
            }
        }
        log.error("getUserSettings() got null empty")
        return ret
    }

    fun getSysCreditRules(index: Int): MutableList<CreditRuleItem> {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\n", "").replace("\r", "")

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "${index}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiGetSysCreditStrategy")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        val resp = client.newCall(req).execute()
        log.info(resp.toString())
        resp.body()?.let { respBody ->
            val respJson = respBody.string().trim()
            log.info(respJson)
            val sysCreditRules = ObjectMapper().readValue(respJson, HunterCreditRulesResp::class.java)
            if (sysCreditRules.status == 0 && sysCreditRules.msg == "OK") {
                log.info("Recv ${sysCreditRules.sys.size} system credit rules")
            } else {
                log.error("Server replied error")
            }
            return sysCreditRules.sys
        }

        log.error("getSysCreditRules() got null empty")
        return mutableListOf()
    }

    fun getDiyRules(index: Int): MutableList<HunterDiyRuleItem> {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\\s+\t\n\r", "")

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "${index}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiGetDiyStrategy")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        try {
            val resp = client.newCall(req).execute()
            log.debug(resp.toString())
            resp.body()?.let { respBody ->
                val respJson = respBody.string().trim()
                log.debug(respJson)
                val respDiyRules = ObjectMapper().readValue(respJson, HunterDiyRulesResp::class.java)
                if (respDiyRules.status == 0 && respDiyRules.msg == "OK") {
                    log.info("Recv ${respDiyRules.diy.size} user DIY rules")
                } else {
                    log.error("Server replied error")
                }
                return respDiyRules.diy
            }
            log.error("getDiyRules() got null body")
            return mutableListOf()
        } catch (e: Exception) {
            log.error(e.toString())
            return mutableListOf()
        }
    }

    /*
        Tommy (926) (1697)
     */
    fun getVIPList(index: Int): MutableList<Int> {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\\s+\t\n\r", "")

        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("index", "${index}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiGetVipUserId")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        try {
            val resp = client.newCall(req).execute()
            log.info(resp.toString())
            resp.body()?.let { respBody ->
                val respJson = respBody.string().trim()
                log.debug(respJson)
                val respDiyRules = ObjectMapper().readValue(respJson, HunterResp.HunterVIPResp::class.java)
                if (respDiyRules.status == 0) {
                    log.info("Recv ${respDiyRules.count} VIP users")
                } else {
                    log.error("Server replied error")
                }
                return respDiyRules.vip
            }
            log.error("getVIPList() got null body")
            return mutableListOf()
        } catch (e: Exception) {
            log.error(e.toString())
            return mutableListOf()
        }
    }

    private class PpdNamingStrategy : PropertyNamingStrategy() {
        override fun nameForGetterMethod(config: MapperConfig<*>?, method: AnnotatedMethod?, defaultName: String?): String {
            return method!!.name.substring(3)
        }
    }

    fun FormBody.Builder.addBidRecord(bidList: MutableList<BidRecordItem>): FormBody.Builder {
        for (index in 0 until bidList.size) {
            this.add("BidRecord[$index][UserId]", bidList[index].UserId.toString())
            this.add("BidRecord[$index][ListingId]", bidList[index].ListingId.toString())
            this.add("BidRecord[$index][StrategyId]", bidList[index].StrategyId.toString())
            this.add("BidRecord[$index][BidAmount]", bidList[index].BidAmount.toString())
            this.add("BidRecord[$index][appIndex]", bidList[index].appIndex.toString())
        }
        return this
    }

    data class BidRecordItem(
            @JsonProperty("UserId") var UserId: Int = 0,
            @JsonProperty("StrategyId") var StrategyId: Long = 0,
            @JsonProperty("ListingId") var ListingId: Long = 0,
            @JsonProperty("BidAmount") var BidAmount: Float = 0f,
            @JsonProperty("appIndex") var appIndex: Int = 0
    )

    //POST: send back bid record
    fun sendBidRecord(bidList: MutableList<BidRecordItem>): Boolean {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val count = bidList.size
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${count}${ts}".toByteArray()))
                .replace("\n", "").replace("\r", "")
        //fabricate request
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("count", "${count}")
                .add("time", "${ts}")
                .add("sign", "${signat}")
                .addBidRecord(bidList)
                .build()
        val req = Request.Builder()
                .url(hunterGw + "/apiSendBidRecord")
                .post(formBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .build()
        val resp = client.newCall(req).execute()
        log.info(resp.toString())
        resp.body()?.let { respBody ->
            val respJson = respBody.string().trim()
            log.info(respJson)

            val userResp = ObjectMapper().readValue(respJson, BidRecordResp::class.java)
            return if (userResp.status == 0 && userResp.msg == "OK") {
                true
            } else {
                log.error("Failed to send back bid record")
                false
            }
        }
        log.error("sendBidRecord() got null body")
        return false
    }

    fun postDiyRule() {
        //fabricate data
        val ts = System.currentTimeMillis() / 1000
        val index = 1
        val signat = String(sniperStore!!.hunterClient!!.signToBase64("${index}${ts}".toByteArray()))
                .replace("\n", "").replace("\r", "")

        val template = RestTemplate()
        val param = LinkedMultiValueMap<String, String>()
        param.set("index", "${index}")
        param.set("time", "${ts}")
        param.set("sign", "${signat}")
        log.info("index=" + index)
        log.info("time=" + ts)
        log.info("sign=" + signat)
        val resp = template.postForEntity<String>(hunterGw + "/apiGetDiyStrategy", param, String::class.java)
        val sb = StringBuilder(resp.body)
        log.info(sb.toString())
    }
}
