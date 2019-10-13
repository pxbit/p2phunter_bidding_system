package cfig.p2psniper.ppdsdk

import cfig.p2psniper.common.utils.LogHelper
import cfig.p2psniper.ppdsdk.params.AuthInfo
import cfig.p2psniper.ppdsdk.params.PpdReq.*
import cfig.p2psniper.ppdsdk.params.PpdResp.*
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class PpdClient(ppdApp: PpdApp) : PpdServiceBase(ppdApp) {
    private val log = LoggerFactory.getLogger(PpdClient::class.java)
    val accountService = AccountService()
    val balanceService = BalanceService()
    val loanService = LoanService()
    val bidService = BidService()
    val debtService = DebtService()
    private val gwIPEastCN = "http://118.178.213.42" //hang zhou
    private val gwIPMiddleCN = "http://116.211.167.144" //wu han
    private val gwIPNorthCN = "http://218.11.0.134" //shi jia zhuang

    //for precious APIs which may take a long time to finish
    private val longClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .build()

    inner class BidService {
        /*
            新投标接口
         */
        fun bidding2(acToken: String, listingId: Long, amount: Int, useCoupon: Boolean = true): BidResp2? {
            val subUrl = "/listing/openapi/bid"
            val bidRequest = getRequestTemplate(gwurl + subUrl,
                    BidReq(listingId, amount, if (useCoupon) "true" else "false"),
                    acToken, needSign = false)
            val resp = longClient.newCall(bidRequest).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                log.info(respJson)
                var resp2: BidResp2? = null
                try {
                    resp2 = ObjectMapper().readValue(respJson, BidResp2::class.java)
                } catch (e: UnrecognizedPropertyException) {
                    try {
                        val oopsMsg = ObjectMapper().readValue(respJson, OopsMsg::class.java)
                        if (oopsMsg.Code == "GTW-BRQ-ARREARAGE" || oopsMsg.Message.contains("欠费")) {
                            log.error("API arrearage")
                        }
                    } catch (e: Exception) {
                        log.error("BidResp format error: " + e.message)
                    }
                }
                return resp2
            } else {
                log.error("bidding($acToken, $listingId ..) got null body")
                return null
            }
        }

        /*
            新投标结果查询
        */
        fun queryBiddingResult(acToken: String, listingId: Long, orderId: String): Any? {
            var ret: Any? = null
            val subUrl = "/listingbid/openapi/queryBid"
            val bidRequest = getRequestTemplate(gwurl + subUrl,
                    QueryBidReq(listingId, orderId), acToken, needSign = false)
            val resp = longClient.newCall(bidRequest).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                log.info(respJson)
                try {
                    ret = ObjectMapper().readValue(respJson, BidQueryResp2::class.java)
                } catch (e: InvalidDefinitionException) {
                    try {
                        ret = ObjectMapper().readValue(respJson, OopsMsg::class.java)
                        log.error("queryBid($listingId, $orderId) fail: $ret")
                    } catch (e: Exception) {
                        LogHelper.dump(PpdClient::class.java.toString(), e)
                    }
                }
            } else {
                log.error("query bidding($acToken, $listingId, $orderId ..) got null body")
                return null
            }
            return ret
        }

        /*
            最近投标信息
         */
        fun batchLenderBidList(userName: String, acToken: String): String {
            val subUrl = "/bid/openapi/batchLenderBidList"
            val bidListRequest = getRequestTemplate(gwurl + subUrl,
                    BidListReq(listOf(userName), 20),
                    acToken)
            val resp = OkHttpClient().newCall(bidListRequest).execute()
            val respBody = resp.body()
            return if (respBody != null) {
                val respJson = respBody.string().trim()
                respJson
            } else {
                log.error("batchLenderBidList() got null body")
                ""
            }
        }
    }

    inner class LoanService {
        /*
            查开放的散标
        */
        fun loanList(inTs: LocalDateTime, inPage: Int): List<LoanListResp.SingleListingResp> {
            val subUrl = "/listing/openapiNoAuth/loanList"
//            val subUrl = "/invest/LLoanInfoService/LoanList"
            val ret: MutableList<LoanListResp.SingleListingResp> = mutableListOf()
            try {
                val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                        LoanListReq(inPage, inTs), needSign = false)).execute()
                val respBody = resp.body()
                if (respBody != null) {
                    val respJson = respBody.string().trim()
                    log.debug("resp: $respJson")
                    try {
                        val ret2 = ObjectMapper().readValue(respJson, LoanListResp::class.java)
                        if (ret2.Result == 1) {
                            ret.addAll(ret2.listings)
                        }
                    } catch (e: JsonMappingException) { //json parse error
                        log.error("loanList(" + ppdApp.nickName + "): $respJson")
                    }
                } else {
                    log.error("loanList($inTs, $inPage) got null body")
                }
            } catch (e: UnknownHostException) { //network error
                log.error("loanList(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketTimeoutException) { //network error
                log.error("loanList(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketException) {
                log.error("loanList(" + ppdApp.nickName + "): $e: ${e.message}")
            }

            return ret
        }

        fun loadLoanSecondsAgo(n: Long): List<LoanListResp.SingleListingResp> {
            val tsSomeTimeAgo = LocalDateTime.now().minusSeconds(n)
            var ret: MutableList<LoanListResp.SingleListingResp> = mutableListOf()
            var page = 1
            while (true) {
                val innerRet = loanList(tsSomeTimeAgo, page)
                var ll: MutableList<Long> = mutableListOf()
                innerRet.forEach { ll.add(it.ListingId) }
                ret.addAll(innerRet)
                log.debug("page $page got ${innerRet.size} listings")
                log.debug(ll.toString())
                if (innerRet.size != 200) {
                    break
                } else {
                    page++
                    log.debug("Fetch page: " + page)
                }
            }
            return ret
        }

        /*
            查标的*详情*(批量)
            @param listingIds: kotlin list of listingIDs
        */
        @Throws(SniperException.DrinkTeaException::class)
        fun listingDetail(listingIds: List<Long>): LoanInfoResp {
            val subUrl = "/listing/openapiNoAuth/batchListingInfo"
            val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                    ListingInfoReq(listingIds), needSign = false)).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                var ret: LoanInfoResp = LoanInfoResp()
                try {
                    ret = ObjectMapper().readValue(respJson, LoanInfoResp::class.java)
                } catch (e: UnrecognizedPropertyException) {
                    LogHelper.dump(log.name, e)
                    if (respJson.contains("频繁")) {
                        throw SniperException.DrinkTeaException()
                    } else {
                        throw e
                    }
                } catch (e: JsonParseException) {
                    LogHelper.dump(log.name, e)
                    log.error("listingDetail(): $e")
                } catch (e: Exception) {
                    log.error("Unexpected exception: $e")
                    LogHelper.dump(log.name, e)
                }
                if (ret.LoanInfos != null && ret.LoanInfos!!.size == listingIds.size) {
                    //good
                } else {
                    log.warn("LoanDetain size mismatch: expect ${listingIds.size}")
                }
                return ret
            } else {
                log.error("listingDetail() got null body")
                return LoanInfoResp()
            }
        }

        /*
            查标的*投资详情*(批量)
            @param listingIds: kotlin list of listingIDs
        */
        fun listingBidInfos(listingIds: List<Long>): ListingBidsInfoResp? {
            val subUrl = "/bid/openapiNoAuth/batchListingBidInfos"
            val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                    ListingInfoReq(listingIds), needSign = false)).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                return ObjectMapper().readValue(respJson, ListingBidsInfoResp::class.java)
            } else {
                log.error("listingBidInfos() got null body")
                return null
            }
        }

        /*
            投资状态
            @param listingIds: kotlin list of listingIDs
        */
        fun listingStatus(listingIds: List<Long>): ListingStatusResp {
            val subUrl = "/listing/openapiNoAuth/batchListingStatusInfo"
            try {
                val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                        ListingInfoReq(listingIds), needSign = false)).execute()
                val respBody = resp.body()
                if (respBody != null) {
                    val respJson = respBody.string().trim()
                    return ObjectMapper().readValue(respJson, ListingStatusResp::class.java)
                } else {
                    return ListingStatusResp()
                }
            } catch (e: Exception) {
                log.error("listingStatus() got $e")
                return ListingStatusResp()
            }
        }
    }

    inner class BalanceService {
        //API: BalanceService.QueryBalance
        fun queryBalance(acToken: String): Any? {
            val resp = OkHttpClient().newCall(
                    getRequestTemplate(gwurl + "/balance/balanceService/QueryBalance",
                            null, acToken, needSign = false))
                    .execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                var accountBalance: Any? = null
                var balance: BalanceResp
                try {
                    balance = mapper.readValue(respJson, BalanceResp::class.java)
                    accountBalance = balance.toAccountBalance()
                } catch (e: Exception) {
                    log.error(e.toString())
                    try {
                        accountBalance = mapper.readValue(respJson, OopsMsg::class.java)
                    } catch (e: Exception) {
                        log.error(e.toString())
                    }
                }
                return accountBalance
            } else {
                log.error("queryBalance($acToken) got null body")
                //FIXME: handle null properly
                return null
            }
        }
    }

    inner class AccountService {
        /*
            Refresh Access Token
            return: null if fail
         */
        fun refreshToken(openId: String, refToken: String): RefreshTokenResp? {
            val refreshUrl = "https://ac.ppdai.com/oauth2/refreshtoken"
            val s = String.format("{\"AppID\":\"%s\",\"OpenID\":\"%s\",\"RefreshToken\":\"%s\"}",
                    ppdApp.appId, openId, refToken)
            val jsonBody = RequestBody.create(JSON_TYPE, s)
            val req = Request.Builder()
                    .url(refreshUrl)
                    .post(jsonBody)
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .build()
            val resp = OkHttpClient().newCall(req).execute()
            var ret: RefreshTokenResp? = null
            val respBody = resp.body()
            if (respBody != null) {
                try {
                    ret = mapper.readValue(respBody.string().trim(), RefreshTokenResp::class.java)
                } catch (e: IOException) {
                    log.error(respBody.string().trim())
                }
                return ret
            } else {
                return null
            }
        }

        fun authorize(code: String): AuthInfo? {
            val authorizeUrl = "https://ac.ppdai.com/oauth2/authorize"
            val s = String.format("{\"AppID\":\"%s\",\"code\":\"%s\"}", ppdApp.appId, code)
            val jsonBody = RequestBody.create(JSON_TYPE, s)
            val req = Request.Builder()
                    .url(authorizeUrl)
                    .post(jsonBody)
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .build()
            val resp = OkHttpClient().newCall(req).execute()
            var ret: AuthInfo? = null
            val respBody = resp.body()
            if (respBody != null) {
                try {
                    ret = mapper.readValue(respBody.string().trim(), AuthInfo::class.java)
                } catch (e: IOException) {
                    log.error(respBody.string().trim())
                }
            } else {
                log.error("authorize($code) got null body")
            }
            return ret
        }

        //API: OpenApiPublicQueryService.QueryUserNameByOpenID
        fun queryUserNameByOpenID(OpenID: String): String? {
            val subUrl = "/open/openApiPublicQueryService/QueryUserNameByOpenID"
            val resp = OkHttpClient().newCall(
                    getRequestTemplate(gwurl + subUrl, QueryUsernameReq(OpenID))).execute()
            val respBody = resp.body()
            var ret: String? = null
            if (respBody != null) {
                try {
                    val respJson = respBody.string().trim()
                    val queryUserNameResp = mapper.readValue(respJson, QueryUserNameResp::class.java)
                    ret = decryptBase64(queryUserNameResp.UserName)
                } catch (e: Exception) {
                    log.error(e.toString())
                }
            } else {
                log.error("queryUserNameByOpenID($OpenID) got null body")
            }

            return ret
        }

        //API: RegisterService.AccountExist
        fun accountExists(accountName: String): String? {
            val subUrl = "/auth/registerservice/accountexist"
            val resp = OkHttpClient().newCall(
                    getRequestTemplate(gwurl + subUrl, AccoundExistsReq(accountName))).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                return respJson
            } else {
                return null
            }
        }

        //API: RegisterService.Register
        fun register(inRequest: RegisterReq): String? {
            val subUrl = "/auth/registerservice/register"
            val resp = OkHttpClient().newCall(
                    getRequestTemplate(gwurl + subUrl, inRequest)).execute()
            val respBody = resp.body()
            if (respBody != null) {
                val respJson = respBody.string().trim()
                return respJson
            } else {
                log.error("register() got null body")
                return null
            }
        }
    }

    inner class DebtService {
        fun buyDebt(DebtDealId: Long) {

        }

        fun debtList(inTs: LocalDateTime, inPage: Int): List<DebtListResp.DebtInfoItem> {
            val subUrl = "/debt/openapiNoAuth/buyList"
            var ret: List<DebtListResp.DebtInfoItem> = mutableListOf()
            try {

                val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                        DebtListReq(inPage, inTs), needSign = false)).execute()
                val respBody = resp.body()
                if (respBody != null) {
                    val respJson = respBody.string().trim()
                    log.debug("resp: $respJson")
                    try {
                        val ret2 = ObjectMapper().readValue(respJson, DebtListResp::class.java)
                        ret = ret2.DebtInfos
                    } catch (e: JsonMappingException) { //json parse error
                        log.error("debtList(" + ppdApp.nickName + "): ${e.message}, $e")
                    }
                } else {
                    log.error("debtList($inTs, $inPage) got null body")
                }
            } catch (e: UnknownHostException) { //network error
                log.error("debtList(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketTimeoutException) { //network error
                log.error("debtList(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketException) {
                log.error("debtList(" + ppdApp.nickName + "): $e: ${e.message}")
            }
            return ret
        }

        fun debtListSecondsAgo(n: Long): List<DebtListResp.DebtInfoItem> {
            val tsSomeTimeAgo = LocalDateTime.now().minusSeconds(n)
            return debtList(tsSomeTimeAgo, 1)
        }

        fun debtDetail(debtIds: List<Long>): List<DebtDetailResp.DebtDetailItem> {
            val subUrl = "/debt/openapiNoAuth/batchDebtInfo"
            var ret: List<DebtDetailResp.DebtDetailItem> = mutableListOf()
            try {
                val resp = OkHttpClient().newCall(getRequestTemplate(gwurl + subUrl,
                        DebtInfoReq(debtIds), needSign = false)).execute()
                val respBody = resp.body()
                if (respBody != null) {
                    val respJson = respBody.string().trim()
                    log.info("resp: $respJson")
                    try {
                        val ret2 = ObjectMapper().readValue(respJson, DebtDetailResp::class.java)
                        ret = ret2.DebtInfos
                    } catch (e: JsonMappingException) { //json parse error
                        log.error("debtDetail(" + ppdApp.nickName + "): ${e.message}, $e")
                    }
                } else {
                    log.error("debtDetail() got null body")
                }
            } catch (e: UnknownHostException) { //network error
                log.error("debtDetail(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketTimeoutException) { //network error
                log.error("debtDetail(" + ppdApp.nickName + "): $e: ${e.message}")
            } catch (e: SocketException) {
                log.error("debtDetail(" + ppdApp.nickName + "): $e: ${e.message}")
            }
            return ret
        }
    }
}
