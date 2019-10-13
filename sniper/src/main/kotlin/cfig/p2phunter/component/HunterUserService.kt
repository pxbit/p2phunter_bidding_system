package cfig.p2phunter.component

import cfig.p2phunter.config.SniperProps
import cfig.p2phunter.th.HunterDiyRulesResp.HunterDiyRuleItem
import cfig.p2phunter.th.HunterResp
import cfig.p2phunter.th.HunterResp.HunterUserSettingResp.HunterUserSettingItem
import cfig.p2psniper.common.entity.CustomAARule
import cfig.p2psniper.common.entity.MultiAuth
import cfig.p2psniper.common.entity.PpdUser
import cfig.p2psniper.common.repository.CustomAARuleRepo
import cfig.p2psniper.common.repository.MultiAuthRepo
import cfig.p2psniper.common.repository.PpdUserRepo
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HunterUserService(inHunterUtils: HunterUtils, sP: SniperProps,
                        pr: PpdUserRepo, cr: CustomAARuleRepo, mar: MultiAuthRepo) {
    private val hunterUtils: HunterUtils = inHunterUtils
    private val sniperProps: SniperProps = sP
    private val customAARuleRepo: CustomAARuleRepo = cr
    private val ppdUserRepo: PpdUserRepo = pr
    private val multiAuthRepo: MultiAuthRepo = mar

    companion object {
        private val log = LoggerFactory.getLogger("HunterUserService")
        private val userMap: HashMap<Int, PpdUser> = hashMapOf()
        //credit rules
        private val userDiyCreditRuleMap: HashMap<Int, MutableList<HunterDiyRuleItem>> = hashMapOf()
        private val userSystemCreditRuleMap: HashMap<Int, MutableList<HunterUserSettingItem>> = hashMapOf()
        //AA rules
        private val systemAARules: HashMap<Int, CustomAARule> = hashMapOf()
        private val userSystemAARuleMap: HashMap<Int, MutableList<CustomAARule>> = hashMapOf()
        //AI rules
        private val aiRules: HashMap<Int, MutableList<HunterUserSettingItem>> = hashMapOf()
        //VIP
        private val vipList: MutableList<Int> = mutableListOf()
        //users with low balance
        val poorList: MutableSet<Int> = mutableSetOf()
    }

    init {
        //standalone mode
        if (sniperProps.isStandAlone()) {
            val allUsers = ppdUserRepo!!.findAll()
            allUsers.forEach {
                val customAARuleList = customAARuleRepo!!.findByPpdUser(it)
                userMap.put(it.UserId, it)
            }
        } else {
            Thread(Runnable {
                Thread.currentThread().name = "UserUpdater"
                var counter = 1
                while (true) {
                    try {
                        reloadUsers()
                        reloadSysSafeRules()
                        reloadUserCreditRules() //before new diy ready
                        reloadUserSystemRules()
                        //FIXME: ignore VIP bidding to see results
//                    reloadVIPlist()
                        //FIXME
                    } catch (e: Exception) {
                        log.error("error updating ppdUser info: $e")
                        LogHelper.dump(log.name, e)
                    }
                    //FIXME: speed
                    if (counter % 2 == 0) {
                        log.info("round $counter: clear poor users: ${poorList.size} ")
                        poorList.clear()
                    } else {
                        log.info("round $counter: found ${poorList.size} poor users")
                    }
                    TimeUnit.MINUTES.sleep(10)
                    counter++
                }
            }).start()
        }
    }

    //1 - get user list from server
    private fun reloadUsers() {
        val allUsers: MutableList<PpdUser> = mutableListOf()
        when (sniperProps.userGroup) {
            -1 -> {
                allUsers.addAll(hunterUtils.getUserInfo(0))
                allUsers.addAll(hunterUtils.getUserInfo(1))
            }
            else -> {
                allUsers.addAll(hunterUtils.getUserInfo(sniperProps.userGroup))
            }
        }

        if (sniperProps.appIndex == 1) {//appIndex
            synchronized(this) {
                userMap.clear()
                for (u in allUsers) {
                    ppdUserRepo.save(u)
                    if (!poorList.contains(u.UserId)) {
                        userMap.put(u.UserId, u)
                    }
                }
            }
        } else {//other appIndex
            //subtract valid users
            val originalUserCount = allUsers.size
            val maList: MutableList<MultiAuth> = mutableListOf()
            maList.addAll(multiAuthRepo.findAll())
            maList.removeIf { it.theKey.appIndex != sniperProps.appIndex }

            val currentAuthList = mutableListOf<Int>()
            maList.mapTo(currentAuthList) { it.theKey.userId }
            allUsers.removeIf { it.UserId !in currentAuthList }
            val afterTrimUserCount = allUsers.size
            log.info("got $originalUserCount users, authorized $afterTrimUserCount users")

            ////override token
            synchronized(this) {
                userMap.clear()
                for (u in allUsers) {
                    val maItem = maList.find { it.theKey.userId == u.UserId }
                    u.AccessToken = maItem!!.accessToken
                    if (u.UserName != maItem.userName) {
                        log.error("BUG: user info mismatch - $u, $maItem")
                        continue
                    }

                    ppdUserRepo.save(u)
                    if (!poorList.contains(u.UserId)) {
                        userMap.put(u.UserId, u)
                    }
                }
            }
        }

        log.info("reload ${userMap.size} user info, poor list ${poorList.size}")
    }

    //2 - get user credit rules from server
    private fun reloadUserCreditRules() {
        val rules: MutableList<HunterDiyRuleItem> = mutableListOf()
        rules.addAll(hunterUtils.getDiyRules(sniperProps.userGroup))
        synchronized(this) {
            userDiyCreditRuleMap.clear()
            rules.forEach { rule ->
                if (userDiyCreditRuleMap[rule.UserId] == null) {
                    userDiyCreditRuleMap.put(rule.UserId, mutableListOf())
                }
                userDiyCreditRuleMap[rule.UserId]!!.add(rule)
            }
        }
    }

    //3 - get system AA rules from server
    private fun reloadSysSafeRules() {
        val rules = hunterUtils.getAARules(0)
        synchronized(this) {
            systemAARules.clear()
            rules!!.sys.forEach { t, u ->
                log.debug("$t -> $u")
                systemAARules.put(t, u.toCustomAARule(t))
            }
        }
    }

    //4 - get user system credit && AA rules from server
    private fun reloadUserSystemRules() {
        val rules: MutableList<HunterUserSettingItem> = mutableListOf()
        rules.addAll(hunterUtils.getUserSettings(sniperProps.userGroup))
        synchronized(this) {
            userSystemCreditRuleMap.clear()
            userSystemAARuleMap.clear()
            aiRules.clear()
            rules.forEach { rule ->
                when (rule.StrategyId.toInt()) {
                    in systemAARules.keys -> {//AA rule
                        if (userSystemAARuleMap[rule.UserId] == null) {
                            userSystemAARuleMap.put(rule.UserId, mutableListOf())
                        }
                        val composeRule = systemAARules[rule.StrategyId.toInt()]!!.mirror()
                        composeRule.amount = rule.BidAmount
                        log.debug("Add AA Rule: ${rule.UserId} - ${composeRule.toMiniString()}")
                        userSystemAARuleMap[rule.UserId]!!.add(composeRule)
                    }
                    in 10..499 -> {//credit rules
                        if (userSystemCreditRuleMap[rule.UserId] == null) {
                            userSystemCreditRuleMap.put(rule.UserId, mutableListOf())
                        }
                        userSystemCreditRuleMap[rule.UserId]!!.add(rule)
                    }
                    in 500..1000 -> {//AI rules
                        if (aiRules[rule.UserId] == null) {
                            aiRules.put(rule.UserId, mutableListOf())
                        }
                        aiRules[rule.UserId]!!.add(rule)
                    }
                    else -> {//diy rules
//                        log.error("Unknown ruleId: " + rule.StrategyId.toInt())
                    }
                }//end-of-when
            }//end-of-for-each
        }//sync

        //FIXME: DEBUG
        log.info("Ai rules: dump")
        aiRules.forEach {
            log.info("  " + it.key.toString() + " : " + it.value)
        }
        log.info("Ai rules: dump end")
        //FIXME: DEBUG
    }//end-of-reloadUserSystemRules

    //5 - get VIP list
    private fun reloadVIPlist() {
        synchronized(this) {
            vipList.clear()
            vipList.addAll(hunterUtils.getVIPList(0))
            log.info("Recv VIP List: $vipList")
        }
    }

    fun rmUser(inUserId: Int) {
        log.info("remove ppdUser ${inUserId} from ppdUser map")
        synchronized(this) {
            userMap.remove(inUserId)
        }
    }

    //4 cheap get 
    fun getUserSysAARulesCheap(): HashMap<Int, MutableList<CustomAARule>> {
        return userSystemAARuleMap
    }

    fun getUsersCheap(): HashMap<Int, PpdUser> {
        return userMap
    }

    fun getUserDiyRulesCheap(): HashMap<Int, MutableList<HunterDiyRuleItem>> {
        return userDiyCreditRuleMap
    }

    fun getUserSysCreditRulesCheap(): HashMap<Int, MutableList<HunterUserSettingItem>> {
        return userSystemCreditRuleMap
    }

    fun getAiRulesCheap(): HashMap<Int, MutableList<HunterUserSettingItem>> {
        return aiRules
    }

    fun getVIPListCheap(): List<Int> {
        return vipList
    }

    //4 expensive get 
    private fun getUsers(): HashMap<Int, PpdUser> {
        val newUserMap: HashMap<Int, PpdUser> = hashMapOf()
        synchronized(this) {
            userMap.forEach { t, u ->
                newUserMap.put(t, u.copy())
            }
        }
        return newUserMap
    }

    private fun getUserDiyRules(): HashMap<Int, MutableList<HunterDiyRuleItem>> {
        val newUserRuleMap: HashMap<Int, MutableList<HunterDiyRuleItem>> = hashMapOf()
        synchronized(this) {
            userDiyCreditRuleMap.forEach { uid, u ->
                newUserRuleMap.put(uid, u)
            }
        }

        return newUserRuleMap
    }

    private fun getUserSysCreditRules(): HashMap<Int, MutableList<HunterUserSettingItem>> {
        val newUserRuleMap: HashMap<Int, MutableList<HunterUserSettingItem>> = hashMapOf()
        synchronized(this) {
            userSystemCreditRuleMap.forEach { uid, r ->
                newUserRuleMap.put(uid, r)
            }
        }

        return newUserRuleMap
    }

    private fun getUserSysAARules(): HashMap<Int, MutableList<CustomAARule>> {
        val newUserSysAARuleMap: HashMap<Int, MutableList<CustomAARule>> = hashMapOf()
        synchronized(this) {
            userSystemAARuleMap.forEach { uid, rules ->
                newUserSysAARuleMap.put(uid, rules)
            }
        }
        return newUserSysAARuleMap
    }
}

//更新用户账户余额
//var updateBalanceCounter = 0
//        allUsers.forEach {
//            try {
//                val bal = sniperStore.bidClient!!.balanceService.queryBalance(it.AccessToken!!)
//                if (bal is PpdResp.AccountBalance) {
//                    if (Math.abs(it.UserBalance - bal.remains) > 1f) {
//                        log.info("User ${it.UserId}: ${it.UserBalance} -> ${bal.remains}")
//                        updateBalanceCounter++
//                        it.UserBalance = bal.remains
//                    }
//                } else {
//                    log.warn("Fail to query balance for user ${it.UserId}")
//                }
//            } catch (e: Exception) {
//                log.error("Unexpected exception $e when querying balance for user ${it.UserId}")
//            }
//        }
//        log.info("updated balance for $updateBalanceCounter users")
