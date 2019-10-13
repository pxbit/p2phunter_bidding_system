package cfig.p2phunter.utils

import cfig.p2phunter.th.HunterDiyRulesResp
import cfig.p2psniper.common.entity.Loan
import cfig.p2psniper.common.utils.LogHelper
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import cfig.p2psniper.ppdsdk.params.PpdResp.LoanInfoResp.LoanDetailItem

class HunterMatcher {
    companion object {
        private val log = LoggerFactory.getLogger(HunterMatcher::class.java)
        private val _211Schools = hashSetOf("北京工业大学", "清华大学", "北京大学", "中国人民大学", "南京理工大学", "西安交通大学"
                , "北京化工大学", "北京理工大学", "北京航空航天大学", "上海外国语大学", "南京农业大学",
                "西安电子科技大学", "中国传媒大学", "北京邮电大学", "对外经济贸易大学", "天津医科大学 "
                , "浙江大学", "海南大学", "中央财经大学", "中央民族大学", "中国矿业大学(北京)",
                "辽宁大学", "安徽大学", "宁夏大学", "中央音乐学院", "中国政法大学", "中国石油大学(北京)"
                , "延边大学", "厦门大学", "青海大学", "北京交通大学", "北京体育大学", "北京外国语大学",
                "哈尔滨工业大学", "南昌大学", "西藏大学", "中国农业大学", "北京科技大学", "北京林业大学"
                , "苏州大学", "山东大学", "陕西师范大学", "北京师范大学", "北京中医药大学",
                "华北电力大学(北京)", "中国科学技术大学", "郑州大学", "长安大学", "上海大学"
                , "中国地质大学(北京)", "华东师范大学", "中国石油大学(华东)", "武汉大学", "南京大学",
                "东华大学", "复旦大学", "华东理工大学", "中国地质大学(武汉)", "华中农业大学", "中国药科大学"
                , "河北工业大学", "同济大学", "上海交通大学", "湖南师范大学", "湖南大学",
                "福州大学", "大连海事大学", "上海财经大学", "天津大学", "华南理工大学", "中山大学", " 石河子大学"
                , "哈尔滨工程大学", "南开大学", "西南大学", "电子科技大学", "第二军医大学",
                "中南大学", "河海大学", "重庆大学", "东北大学", "西北工业大学", "广西大学", "暨南大学", "南京师范大学"
                , "华北电力大学(保定)", "东北师范大学", "国防科学技术大学", "四川大学",
                "兰州大学", "江南大学", "太原理工大学", "东北林业大学", "中国海洋大学", "四川农业大学", "新疆大学"
                , "华中师范大学", "内蒙古大学", "东南大学", "西南交通大学", "云南大学", "武汉理工大学",
                "大连理工大学", "中国矿业大学(徐州)", "华中科技大学", "贵州大学", "华南师范大学", "吉林大学"
                , "南京航空航天大学", "中南财经政法大学", "西北大学", "西南财经大学", "东北农业大学",
                "合肥工业大学", "第四军医大学", "西北农林科大")
        private val _985Schools = hashSetOf("清华大学", "厦门大学", "南京大学", "天津大学", "浙江大学"
                , "西安交通大学", "东南大学", "上海交通大学", "山东大学", "中国人民大学", "吉林大学"
                , "电子科技大学", "四川大学", "华南理工大学", "兰州大学", "西北工业大学", "同济大学"
                , "北京大学", "中国科学技术大学", "复旦大学", "哈尔滨工业大学", "南开大学", "华中科技大学"
                , "武汉大学", "中国海洋大学", "湖南大学", "北京理工大学", "重庆大学", "大连理工大学", "中山大学"
                , "北京航空航天大学", "东北大学", "北京师范大学", "中南大学", "中国农业大学", "西北农林科技大学"
                , "中央民族大学", "国防科技大学", "华东师范大学")

        fun getMask(inLoan: Loan, debug: Boolean = false): Int {
            val creditMask = creditCodeMask(inLoan.CreditCode)
            if (creditMask == 0) return 0

            val degreeMask = educationDegreeMask(inLoan.EducationDegree)
            if (degreeMask == 0) return 0

            val studyStyleMask = studyStyleMask(inLoan.StudyStyle)
            if (studyStyleMask == 0) return 0

            val schoolMask = graduateSchoolMask(inLoan.GraduateSchool)
            if (schoolMask == 0) return 0

            val genderMask = genderMask(inLoan.Gender)
            if (genderMask == 0) return 0

            val ageMask = ageMask(inLoan.Age)
            if (ageMask == 0) return 0

            val ret = creditMask or
                    degreeMask.shl(7) or
                    studyStyleMask.shl(13) or
                    schoolMask.shl(19) or
                    genderMask.shl(23) or
                    ageMask.shl(25)

            if (debug) {
                //   00 |  00   | (24)0000  |0000  (16)00  |  00 0000 | (8)0000 0000
                //age   |gender | school    |    studyStyle|degreeMask|    creditMask
                log.debug("    Index        " + "0000 00 | 00 | 0000 | 0000 00 | 00 0000 | 000 0000")
                log.debug("    (size        " + " 6      | 2  | 4    | 6       | 6       | 7      )")
                log.debug("_______________________________________________________________________")
                log.debug(": creditMask     " + "        |    |      |         |         | " + String.format("%7s", Integer.toBinaryString(creditMask)).replace(" ", "0"))
                log.debug(", degreeMask     " + "        |    |      |         | " + String.format("%6s", Integer.toBinaryString(degreeMask)).replace(" ", "0"))
                log.debug(", studyStyleMask " + "        |    |      | " + String.format("%6s", Integer.toBinaryString(studyStyleMask)).replace(" ", "0"))
                log.debug(", schoolMask     " + "        |    | " + String.format("%4s", Integer.toBinaryString(schoolMask)).replace(" ", "0"))
                log.debug(", genderMask     " + "        | " + String.format("%2s", Integer.toBinaryString(genderMask)).replace(" ", "0"))
                log.debug(", ageMask        " + String.format("%6s", Integer.toBinaryString(ageMask).replace(" ", "0")))
                log.debug("_______________________________________________________________________")
                log.debug("Total Loan Mask  " + Integer.toBinaryString(ret))
            }
            return ret
        }

        fun decryptMask(maskValue: Int) {
            val creditMask = maskValue and 0b111_1111
            val degreeMask = maskValue.shr(7) and 0b11_1111
            val studyStyleMask = maskValue.shr(13) and 0b11_1111
            val schoolMask = maskValue.shr(19) and 0b1111
            val genderMask = maskValue.shr(23) and 0b11
            val ageMask = maskValue.shr(25) and 0b11_1111

            log.debug("ageMask | genderMask | schoolMask | studyStyleMask | degreeMask | creditMask")
            log.debug(String.format("%7s", Integer.toBinaryString(ageMask)) +
                    " | " + String.format("%10s", Integer.toBinaryString(genderMask)) +
                    " | " + String.format("%10s", Integer.toBinaryString(schoolMask)) +
                    " | " + String.format("%14s", Integer.toBinaryString(studyStyleMask)) +
                    " | " + String.format("%10s", Integer.toBinaryString(degreeMask)) +
                    " | " + String.format("%10s", Integer.toBinaryString(creditMask)))
        }

        fun ageMask(age: Int): Int {
            var condition_mask = 0
            if (age in 18..22) {
                condition_mask = 1
                return condition_mask
            }
            if (age in 23..27) {
                condition_mask = 1.shl(1)
                return condition_mask
            }

            if (age in 28..32) {
                condition_mask = 1.shl(2)
                return condition_mask
            }

            if (age in 33..40) {
                condition_mask = 1.shl(3)
                return condition_mask
            }

            if (age in 41..60) {
                condition_mask = 1.shl(4)
                return condition_mask
            }

            log.error("unsupported age: " + age)
            return condition_mask
        }

        //len: 2,
        //性别	1 男 2 女 0 未知
        fun genderMask(gender: Int): Int {
            return if (gender == 1) 1 else 2
        }

        fun isGraduateFrom985(school: String, verbose: Boolean = false): Boolean {
            if (verbose) {
                println("$school is " + (if (_985Schools.contains(school)) "" else " not ") + " 985 school")
            }
            return _985Schools.contains(school)
        }

        fun isGraduateFrom211(school: String, verbose: Boolean = false): Boolean {
            if (verbose) {
                println("$school is " + (if (_211Schools.contains(school)) "" else " not ") + " 211 school")
            }
            return _211Schools.contains(school)
        }

        fun graduateSchoolMask(school: String?): Int {
            if (school.isNullOrBlank()) return 8
            if (isGraduateFrom985(school!!)) return 1
            if (isGraduateFrom211(school)) return 2
            return 4
        }

        //NULL, "", 普通, 成人, 网络教育, 开放教育, 自考, 研究生, 自学考试, 函授, 业余, 普通全日制, 脱产, 不详, 夜大学, 全日制,
        fun studyStyleMask(studyStyle: String?): Int {
            if (studyStyle.isNullOrBlank()) return 32
            if (hashSetOf("普通", "普通全日制", "全日制").contains(studyStyle)) return 1
            if (studyStyle == "成人") return 2
            if (studyStyle == "研究生") return 4
            if (studyStyle == "网络教育") return 8
            if (hashSetOf("自学考试", "自考").contains(studyStyle)) return 16
            return 32
        }

        //len: 6
        //NULL, "", 专科, 本科, 硕士, 专科（高职）, 专科(高职), 硕士研究生, 专升本, 夜大电大函大普通班, 博士研究生, 第二学士学位,
        fun educationDegreeMask(degree: String?): Int {
            if (degree.isNullOrBlank()) return 32
            if (degree == "博士研究生") return 1
            if (degree == "硕士研究生" || degree == "硕士") return 2
            if (degree == "本科" || degree == "专升本") return 4
            if (degree == "专科") return 8
            if (degree!!.contains("专科") and degree.contains("高职")) return 16
            //有值，但不能识别，为0
            log.error("unknown degree mask: " + degree)
            return 0
        }

        //len: 7
        fun creditCodeMask(creditCode: String): Int {
            if (creditCode == "AA") return 1
            if (creditCode == "A") return 2
            if (creditCode == "B") return 4
            if (creditCode == "C") return 8
            if (creditCode == "D") return 16
            if (creditCode == "E") return 32
            if (creditCode == "F") return 64
            log.error("Unknown creditCode: " + creditCode)
            return 0
        }

        //from AutobidController.class.php.MatchBidReqList()
        fun matchRule2(loan: Loan, rule: HunterDiyRulesResp.HunterDiyRuleItem): Boolean {
            if (loan.RemainFunding < rule.BidAmount) {
                log.debug("remainFunding < rule BidAmount")
                return false
            }

            val calcMask = getMask(loan)
            if (calcMask == 0) {//can not get reasonable mask
                log.error("Can not get valid mask for loan ${loan.ListingId}")
                return false
            }
            if ((calcMask and rule.ConditionMask) != calcMask) {
                log.debug("condition mask mismatch: loanMask=" + String.format("%30s", Integer.toBinaryString(calcMask)))
                decryptMask(calcMask)
                log.debug("condition mask mismatch: ruleMask=" + String.format("%30s", Integer.toBinaryString(rule.ConditionMask)))
                decryptMask(rule.ConditionMask)
                return false
            }

            //Certificate
            if (rule.CertificateValidate != 0 && !loan.CertificateValidate) {
                log.debug("CertificateValidate mismatch")
                return false
            }
            if (rule.CertificateValidateFalse != 0 && loan.CertificateValidate) {
                log.debug("CertificateValidate:false mismatch")
                return false
            }

            //Credit
            if (rule.CreditValidate != 0 && !loan.CreditValidate) {
                log.debug("CreditValidate mismatch")
                return false
            }
            if (rule.CreditValidateFalse != 0 && loan.CreditValidate) {
                log.debug("CreditValidate:false mismatch")
                return false
            }

            /* EducateValidate is deprecated
            if (rule.EducateValidate != 0) {
                if (loan.EducateValidate == null || !loan.EducateValidate!!) {
                    log.debug("EducateValidate mismatch")
                    return false
                }
            }
            */

            /* VideoValidate is deprecated
            if (rule.VideoValidate != 0 && !loan.VideoValidate) {
                log.debug("VideoValidate mismatch")
                return false
            }
            */

            //Phone
            if (rule.PhoneValidate != 0 && !loan.PhoneValidate) {
                log.debug("PhoneValidate mismatch")
                return false
            }
            if (rule.PhoneValidateFalse != 0 && loan.PhoneValidate) {
                log.debug("PhoneValidate:false mismatch")
                return false
            }

            //户口
            if (rule.NciicIdentityCheck != 0 && !loan.NciicIdentityCheck) {
                log.debug("NciicIdentityCheck mismatch")
                return false
            }
            if (rule.NciicIdentityCheckFalse != 0 && loan.NciicIdentityCheck) {
                log.debug("NciicIdentityCheck:false mismatch")
                return false
            }

            if (!inRange(rule.AmountA, rule.AmountB, loan.Amount)) {
                log.debug("Amount mismatch")
                return false
            }
            if (!inRange(rule.MonthA, rule.MonthB, loan.Months.toFloat())) {
                log.debug("Month mismatch")
                return false
            }
            if (!inRange(rule.RateA, rule.RateB, loan.CurrentRate)) {
                log.debug("Rate mismatch")
                return false
            }
            if (!inRange(rule.SuccessCountA, rule.SuccessCountB, loan.SuccessCount.toFloat())) {
                log.debug("SuccessCount mismatch")
                return false
            }
            if (!inRange(rule.WasteCountA, rule.WasteCountB, loan.WasteCount.toFloat())) {
                log.debug("WasteCount mismatch")
                return false
            }
            if (!inRange(rule.NormalCountA, rule.NormalCountB, loan.NormalCount.toFloat())) {
                log.debug("NormalCount mismatch")
                return false
            }
            if (!inRange(rule.OverdueMoreCountA, rule.OverdueMoreCountB, loan.OverdueMoreCount.toFloat())) {
                log.debug("OverdueMoreCount mismatch")
                return false
            }
            if (!inRange(rule.OverdueCountA, rule.OverdueCountB, loan.OverdueLessCount.toFloat())) {
                log.debug("OverdueLessCount mismatch")
                return false
            }

            //正常还清/成功借款次数
            //如果一个人总是借了就还刷信用，那么这个比值会明显低于正常值
            if (!inRange(rule.NormalSuccessRatioA, rule.NormalSuccessRatioB,
                            1.0f * loan.NormalCount / (loan.SuccessCount + (1e-4).toFloat()))) {
                log.debug("NormalSuccessRatio mismatch: 正常还清/成功借款次数")
                return false
            }
            if (!inRange(rule.DelayNormalRatioA, rule.DelayNormalRatioB,
                            1.0f * loan.OverdueLessCount / (loan.NormalCount + 0.01f))) {
                log.debug("DelayNormalRatio mismatch")
                return false
            }
            if (!inRange(rule.OwingAmountA, rule.OwingAmountB, loan.OwingAmount)) {
                log.debug("OwingAmount mismatch: 借前负债")
                return false
            }
            if (!inRange(rule.OwingPrevHighDebtRatioA, rule.OwingPrevHighDebtRatioB,
                            loan.OwingAmount / (loan.HighestDebt + 0.0001f))) {
                log.debug("OwingPrevHighDebtRatio mismatch: 借前负债/历史最高负债")
                return false
            }
            if (!inRange(rule.OwingHighDebtRatioA, rule.OwingHighDebtRatioB,
                            (loan.OwingAmount + loan.Amount) / (loan.HighestDebt + 1e-4.toFloat()))) {
                log.debug("OwingHighDebtRatio mismatch: 借后负债/历史最高负债")
                return false
            }
            if (!inRange(rule.LastHighestBorrowRatioA, rule.LastHighestBorrowRatioB,
                            loan.Amount / (loan.HighestPrincipal + (1e-4).toFloat()))) {
                log.debug("LastHighestBorrowRatioA mismatch: 本次借款金额/历史最高单次借款金额")
                return false
            }

            if (loan.FirstSuccessBorrowTime != null) {
                if (!inRange(rule.FirstSuccessIntervalA.toFloat(), rule.FirstSuccessIntervalB.toFloat(),
                                daysPassed(loan.FirstSuccessBorrowTime) / 30)) {
                    log.debug("FirstSuccessBorrowTime mismatch")
                    return false
                }
            }

            if (loan.LastSuccessBorrowTime != null) {
                if (!inRange(rule.LastSuccessIntervalA, rule.LastSuccessIntervalB, daysPassed(loan.LastSuccessBorrowTime))) {
                    log.debug("LastSuccessIntervalA mismatch: 距离上次成功借款时间(天)")
                    return false
                }
            }

            //新diy参数
            if (!inRange(rule.AgeRangeA, rule.AgeRangeB, loan.Age.toFloat())) {
                return false
            }

            //XXX: 类型是int还是float -> 取float
            //平均借款间隔（天）
            //含义：平均借款频率高不高
            val avgBorrowInterval = if (loan.SuccessCount > 0) {
                daysPassed(loan.FirstSuccessBorrowTime) / loan.SuccessCount
            } else {
                0f
            }
            if (!inRange(rule.AvgBorrowIntervalA, rule.AvgBorrowIntervalB, avgBorrowInterval)) {
                return false
            }

            //$pass=$pass && $this->inRange($diy['CurAvgIntervalRatioA'], $diy['CurAvgIntervalRatioB'],
            //          $loan['SuccessCount'] > 0?
            //          ($this->strToPassedDays($loan[LastSuccessBorrowTime])
            //              / $this->strToPassedDays($loan['FirstSuccessBorrowTime'])
            //              * $loan['SuccessCount'])
            //          : 0);
            // 本次借款间隔比例 = 上借时间 / 首借时间 * 总借款次数
            // 含义：最近借款急不急?
            // 1: 与以前频率一致
            // <1: 更频繁了
            // >1: 比上次借的间隔久了
            val avgIntervalRatio = if (loan.SuccessCount > 0) {
                daysPassed(loan.LastSuccessBorrowTime) / daysPassed(loan.FirstSuccessBorrowTime) * loan.SuccessCount
            } else {
                0f
            }
            if (!inRange(rule.AvgBorrowIntervalA, rule.AvgBorrowIntervalB, avgIntervalRatio)) {
                return false
            }

            // 注册－首借 间隔月数
            // 注意：对于非首借，数值为静态数据； 如果没有成功首借，则使用当前首借月数
            //inRange($diy['RegisterFirstIntervalA'], $diy['RegisterFirstIntervalB'],
            // $this->strToPassedMonths($loan['RegisterTime']) - $this->strToPassedMonths($loan['FirstSuccessBorrowTime']));
            if (!inRange(rule.RegisterFirstIntervalA, rule.RegisterFirstIntervalB,
                            (daysPassed(loan.RegisterTime) - daysPassed(loan.FirstSuccessBorrowTime)) / 30)) {
                return false
            }

            // 注册时间
            // 意义：是否是老用户
            //$this->inRange($diy['RegisterMonthA'], $diy['RegisterMonthB'],
            // $this->strToPassedMonths($loan['RegisterTime']));
            if (!inRange(rule.RegisterMonthA, rule.RegisterMonthB, daysPassed(loan.RegisterTime))) {
                return false
            }

            // 借后总待还本金
            //$this->inRange($diy['OwingAfterAmountA'], $diy['OwingAfterAmountB'], $loan['OwingAmount'] + $loan['Amount'])
            if (!inRange(rule.OwingAfterAmountA, rule.OwingAfterAmountB, loan.OwingAmount + loan.Amount)) {
                return false
            }

            //$this->inRange($diy['TotalBorrowA'], $diy['TotalBorrowB'], $loan['TotalPrincipal']);
            if (!inRange(rule.TotalBorrowA, rule.TotalBorrowB, loan.TotalPrincipal)) {
                return false
            }

            // 当前欠款 / 历史全部累计欠款
            // 意义：当前是否已经在高欠款的比例
            //inRange($diy['OwnPreTotalBorrowRatioA'], $diy['OwnPreTotalBorrowRatioB'],
            // $loan['OwingAmount']/($loan['TotalPrincipal'] + 0.0001));
            if (!inRange(rule.OwnPreTotalBorrowRatioA, rule.OwnPreTotalBorrowRatioB,
                            loan.OwingAmount / (loan.TotalPrincipal + 1e-4))) {
                return false
            }

            // 当前借款／总待还
            //inRange($diy['CurAmountTotalBorrowRatioA'], $diy['CurAmountTotalBorrowRatioB'],
            // $loan['Amount']/($loan['TotalPrincipal'] + 0.0001));
            if (!inRange(rule.CurAmountTotalBorrowRatioA, rule.CurAmountTotalBorrowRatioB,
                            loan.Amount / (loan.TotalPrincipal + 1e-4))) {
                return false
            }

            // 总待还／成功次数
            // 意义：平均每次借款产生的负债
            //inRange($diy['AvgBorrowAmountA'], $diy['AvgBorrowAmountB'], $loan['TotalPrincipal']/($loan['SuccessCount'] + 0.0001));
            if (!inRange(rule.AvgBorrowAmountA, rule.AvgBorrowAmountB, loan.TotalPrincipal / (loan.SuccessCount + 1e-4))) {
                return false
            }

            // 当前借款／累计借款
            if (!inRange(rule.CurAmountAvgBorrowRatioA, rule.CurAmountAvgBorrowRatioB,
                            loan.Amount / (loan.TotalPrincipal + 1e-4) * loan.SuccessCount)) {
                return false
            }

            //added on 2018.11.27: if set to true, then （Amount % 10 == 0）
            //    含义：是否必须被10整除
            if (rule.TailNumber10 > 0 && (loan.Amount.toInt() % 10 != 0)) {
                return false
            }

            if (!inRange(rule.HighDebtA, rule.HighDebtB, loan.HighestDebt)) {
                return false
            }

            if (!inRange(rule.FailCountA, rule.FailCountB, loan.FailedCount.toFloat())) {
                return false
            }

            if (!inRange(rule.CancelCountA, rule.CancelCountB, loan.CancelCount.toFloat())) {
                return false
            }

            if (!inRange(rule.OwingAmountRatioA, rule.OwingAmountRatioB, loan.OwingAmount / loan.Amount)) {
                return false
            }

            if (!inRange(rule.WasteNormalRatioA, rule.WasteNormalRatioB, loan.WasteCount / (loan.NormalCount + 0.0001f))) {
                return false
            }

            return true
        }

        private fun inRange(left: Float, right: Float, v: Float): Boolean {
            return (inRange(left, right, v.toDouble()))
        }

        private fun inRange(left: Float, right: Float, v: Double): Boolean {
            val v1 = if (Math.abs(left + 1) < 1e-2) true else (v >= left)
            val v2 = if (Math.abs(right + 1) < 1e-2) true else (v <= right)
            if (!(v1 && v2)) log.debug("v1=$v1, v2=$v2 << l=$left, v=$v, r=$right")
            return v1 && v2
        }

        fun inRange(left: Int, right: Int, v: Float): Boolean {
            val v1 = if (left == -1) true else (v >= left)
            val v2 = if (right == -1) true else (v <= right)
            if (!(v1 && v2)) log.debug("v1=$v1, v2=$v2 << l=$left, v=$v, r=$right")
            return v1 && v2
        }

        fun loanDetailItem2Loan(loanRespItem: LoanDetailItem): Loan {
            return loanRespItem.toLoan()
        }

        private fun daysPassed(ts: Timestamp?): Float {
            return if (ts != null) {
                val timeDiff = System.currentTimeMillis() - ts.time
                timeDiff / 1000.0f / 3600 / 24
            } else {
                0f
            }
        }

        private fun LoanDetailItem.toLoan(): Loan {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val ret = Loan()
            ret.Age = this.Age
            ret.Amount = this.Amount
            this.AmountToReceive?.let {
                ret.AmountToReceive = it
            }
            this.AuditingTime?.let {
                ret.AuditingTime = Timestamp(sdf2.parse(this.AuditingTime!!.replace("T", " ")).time)
            }
            ret.BorrowName = this.BorrowName
            this.CancelCount?.let {
                ret.CancelCount = it
            }
            ret.CertificateValidate = this.CertificateValidate
            ret.CreditCode = this.CreditCode.toString()
            ret.CreditValidate = this.CreditValidate
            ret.CurrentRate = this.CurrentRate
            ret.DeadLineTimeOrRemindTimeStr = this.DeadLineTimeOrRemindTimeStr
            ret.EducateValidate = this.EducateValidate
            ret.EducationDegree = this.EducationDegree
            ret.FailedCount = if (this.FailedCount != null) this.FailedCount!! else 0
            if (!this.FirstSuccessBorrowTime.isNullOrEmpty()) {
                ret.FirstSuccessBorrowTime = Timestamp(sdf2.parse(this.FirstSuccessBorrowTime!!.replace("T", " ")).time)
            }
            //fallback to sdf2 if fails
            if (!this.FirstBidTime.isNullOrEmpty()) {
                try {
                    ret.FirstBidTime = Timestamp(sdf.parse(this.FirstBidTime!!.replace("T", " ")).time)
                } catch (e: Exception) {
                    ret.FirstBidTime = Timestamp(sdf2.parse(this.FirstBidTime!!.replace("T", " ")).time)
                }
            }

            ret.Gender = this.Gender
            ret.GraduateSchool = this.GraduateSchool
            ret.HighestDebt = if (this.HighestDebt != null) this.HighestDebt!! else 0f
            this.HighestPrincipal?.let {
                ret.HighestPrincipal = it
            }
            if (!this.LastBidTime.isNullOrEmpty()) {
                try {
                    ret.LastBidTime = Timestamp(sdf.parse(this.LastBidTime!!.replace("T", " ")).time)
                } catch (e: Exception) {
                    ret.LastBidTime = Timestamp(sdf2.parse(this.LastBidTime!!.replace("T", " ")).time)
                }
            }
            if (!this.LastSuccessBorrowTime.isNullOrEmpty()) {
                ret.LastSuccessBorrowTime = Timestamp(sdf2.parse(this.LastSuccessBorrowTime!!.replace("T", " ")).time)
            }
            ret.LenderCount = this.LenderCount
            ret.ListingId = this.ListingId
            ret.Months = this.Months
            ret.NciicIdentityCheck = this.NciicIdentityCheck
            ret.NormalCount = this.NormalCount
            this.OverdueLessCount?.let {
                ret.OverdueLessCount = it
            }
            this.OverdueMoreCount?.let {
                ret.OverdueMoreCount = it
            }
            this.OwingAmount?.let {
                ret.OwingAmount = it
            }
            ret.OwingPrincipal = this.OwingPrincipal
            ret.PhoneValidate = this.PhoneValidate
            try {
                if (!this.RegisterTime.isNullOrBlank()) {
                    ret.RegisterTime = Timestamp(sdf2.parse(this.RegisterTime!!.replace("T", " ")).time)
                }
            } catch (e: Exception) {
                log.error("Unexpected RegisterTime: [${this.RegisterTime}]")
                LogHelper.dump("HunterMatcher", e)
            }
            ret.RemainFunding = this.RemainFunding
            ret.SuccessCount = this.SuccessCount
            ret.StudyStyle = this.StudyStyle
            this.TotalPrincipal?.let {
                ret.TotalPrincipal = it
            }
            ret.VideoValidate = this.VideoValidate
            this.WasteCount?.let {
                ret.WasteCount = it
            }
            return ret
        }
    } //end-of-companion
}
