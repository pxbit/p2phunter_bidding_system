package cfig.p2phunter.th

import cfig.p2psniper.common.entity.CustomAARule
import cfig.p2psniper.common.entity.PpdUser
import com.fasterxml.jackson.annotation.JsonProperty

class HunterResp {
    data class AiRule(
            @JsonProperty("ListingId") var ListingId: Long = 0,
            @JsonProperty("StrategyId") var StrategyId: Long = 0)
    /*
        [{"ListingId": 143335048, "StrategyId": 504}]
     */

    data class BidRecordResp(
            @JsonProperty("status")
            var status: Int = 0,

            @JsonProperty("msg")
            var msg: String = ""
    )

    data class HunterUserInfoResp(
            @JsonProperty("status") var status: Int = 0,
            @JsonProperty("msg") var msg: String = "",
            @JsonProperty("count") var count: Int = 0,
            @JsonProperty("userinfo") var userinfo: MutableList<HunterUserItem> = mutableListOf()
    ) {
        data class HunterUserItem(
                @JsonProperty("UserId") var UserId: Int = 0,
                @JsonProperty("UserName") var UserName: String = "",
                @JsonProperty("AccessToken") var AccessToken: String = "",
                @JsonProperty("UserBalance") var UserBalance: Float = 0f,
                @JsonProperty("Score") var score: Int = 0,
                @JsonProperty("MinBalance") var MinBalance: Float? = null,
                @JsonProperty("MinRate") var MinRate: Float? = null, //最低起投利率
                @JsonProperty("MinMonth") var MinMonth: Int? = null, //最小月数
                @JsonProperty("MaxMonth") var MaxMonth: Int? = null //最大月数
        ) {
            fun toPpdUser(): PpdUser {
                val u = PpdUser()
                u.UserId = this.UserId
                u.UserName = this.UserName
                u.AccessToken = this.AccessToken
                u.UserBalance = this.UserBalance
                u.Score = this.score
                u.holdBalance = this.MinBalance
                u.MinRate = this.MinRate
                u.MinMonth = this.MinMonth
                u.MaxMonth = this.MaxMonth
                return u
            }
        }
    }

    data class HunterSafeRulesResp(
            @JsonProperty("status") var status: Int = 1,
            @JsonProperty("msg") var msg: String = "",
            @JsonProperty("count") var count: Int = 0,
            @JsonProperty("sys") var sys: MutableMap<Int, HunterAARule> = mutableMapOf()
    ) {
        data class HunterAARule(
                @JsonProperty("RateA") var RateA: Float = 0f,
                @JsonProperty("RateB") var RateB: Float = 0f,
                @JsonProperty("MonthsA") var MonthsA: Int = 0,
                @JsonProperty("MonthsB") var MonthsB: Int = 0
        ) {
            fun toCustomAARule(ruleId: Int): CustomAARule {
                val ret = CustomAARule()
                ret.id = ruleId.toLong()
                ret.minRate = this.RateA
                ret.maxRate = this.RateB
                ret.minMonths = this.MonthsA
                ret.maxMonths = this.MonthsB
                return ret
            }
        }
    }
/*
1=HunterAARule(RateA=11, RateB=12, MonthsA=1, MonthsB=2)
2=HunterAARule(RateA=11, RateB=12, MonthsA=3, MonthsB=4)
3=HunterAARule(RateA=12, RateB=12, MonthsA=1, MonthsB=10)
4=HunterAARule(RateA=12, RateB=13, MonthsA=1, MonthsB=19)
5=HunterAARule(RateA=13, RateB=15, MonthsA=1, MonthsB=25)
6=HunterAARule(RateA=15, RateB=25, MonthsA=1, MonthsB=37)
7=HunterAARule(RateA=14, RateB=15, MonthsA=24, MonthsB=37)
*/

    data class HunterUserSettingResp(
            @JsonProperty("status") var status: Int = 1,
            @JsonProperty("msg") var msg: String = "",
            @JsonProperty("count") var count: Int = 0,
            @JsonProperty("setting") var setting: MutableList<HunterUserSettingItem> = mutableListOf()
    ) {
        data class HunterUserSettingItem(
                @JsonProperty("UserId") var UserId: Int = 0,
                @JsonProperty("StrategyId") var StrategyId: Long = 0L,
                @JsonProperty("BidAmount") var BidAmount: Int = 0
        )
    }

/*
HunterUserSettingItem(UserId=903, StrategyId=3, BidAmount=51)
HunterUserSettingItem(UserId=903, StrategyId=1, BidAmount=51)
HunterUserSettingItem(UserId=903, StrategyId=6, BidAmount=73)
HunterUserSettingItem(UserId=903, StrategyId=7, BidAmount=51)
HunterUserSettingItem(UserId=799, StrategyId=1010, BidAmount=80)
HunterUserSettingItem(UserId=799, StrategyId=1008, BidAmount=80)
HunterUserSettingItem(UserId=799, StrategyId=1007, BidAmount=80)
HunterUserSettingItem(UserId=799, StrategyId=1006, BidAmount=80)
HunterUserSettingItem(UserId=799, StrategyId=1004, BidAmount=80)
*/

    data class HunterVIPResp(
            @JsonProperty("status") var status: Int = -1,
            @JsonProperty("count") var count: Int = 0,
            @JsonProperty("vip") var vip: MutableList<Int> = mutableListOf())
}