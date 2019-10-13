package cfig.p2phunter.utils

import cfig.p2psniper.common.entity.Loan

class SystemRuleMatcher {
    companion object {
        fun matchAny(loan: Loan): Boolean {
            return match12(loan) || match13(loan) ||
                    match19(loan) || match20(loan) ||
                    match21(loan) || match22(loan) || match23(loan) || match24(loan) ||
                    match25(loan) || match26(loan) || match27(loan) || matchRule28(loan) ||
                    matchRule29(loan)
        }

        fun matchRule(loan: Loan, ruleId: Long): Boolean {
            when (ruleId) {
                12L -> return match12(loan)
                13L -> return match13(loan)
                19L -> return match19(loan)
                20L -> return match20(loan)
                21L -> return match21(loan)
                22L -> return match22(loan)
                23L -> return match23(loan)
                24L -> return match24(loan)
                25L -> return match25(loan)
                26L -> return match26(loan)
                27L -> return match27(loan)
                28L -> return matchRule28(loan)
                29L -> return matchRule29(loan)
                30L -> return matchRule30(loan)
                31L -> return matchRule31(loan)
                32L -> return matchRule32(loan)
                33L -> return matchRule33(loan)
                34L -> return matchRule34(loan)
                35L -> return matchRule35(loan)
                36L -> return matchRule36(loan)
                37L -> return matchRule37(loan)
                38L -> return matchRule38(loan)
            }

            return false
        }

        //12
        private fun match12(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            if (null == loan.EducationDegree || loan.EducationDegree != "本科") {
                return false
            }

            //年龄 >=33
            if (loan.Age < 33) {
                return false
            }

            val overdueParam1 = 1.0 * loan.OverdueLessCount / loan.NormalCount
            if (overdueParam1 >= 0.03) {
                return false
            }

            //学习形式
            if (!setOf("普通", "成人").contains(loan.StudyStyle)) {
                return false
            }

            return true
        }

        //13
        private fun match13(loan: Loan): Boolean {
            //6月18
            if (loan.CurrentRate.toInt() != 18 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            if (null == loan.EducationDegree || loan.EducationDegree != "本科") {
                return false
            }

            //年龄 [18, 32]
            if (loan.Age > 32 || loan.Age < 18) {
                return false
            }

            val overdueParam1 = 1.0 * loan.OverdueLessCount / loan.NormalCount
            if (overdueParam1 >= 0.03) {
                return false
            }

            //B
            if (!setOf("B").contains(loan.CreditCode)) {
                return false
            }

            return true
        }

        //19
        private fun match19(loan: Loan): Boolean {
            //6月18
            if (loan.CurrentRate.toInt() != 18 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            //学籍认证
            if (null == loan.EducateValidate || !loan.EducateValidate!!) {
                return false
            }

            return true
        }

        //20
        private fun match20(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            //女
            if (loan.Gender != 2) {
                return false
            }

            //年龄 [24, 37]
            if (loan.Age < 24 || loan.Age > 37) {
                return false
            }

            if (null == loan.EducationDegree || loan.EducationDegree != "本科") {
                return false
            }

            return true
        }

        //21
        private fun match21(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //学籍认证
            if (null == loan.EducateValidate || !loan.EducateValidate!!) {
                return false
            }

            return true
        }

        //22
        private fun match22(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            //年龄 [24, 37]
            if (loan.Age < 24 || loan.Age > 37) {
                return false
            }

            if (loan.Gender != 2) {
                return false
            }

            //学习形式
            if (!setOf("普通", "成人", "研究生").contains(loan.StudyStyle)) {
                return false
            }

            return true
        }

        //23
        private fun match23(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            //有学历
            if (null == loan.EducationDegree) {
                return false
            }

            //年龄 [24, 28]
            if (loan.Age < 24 || loan.Age > 28) {
                return false
            }

            //还过款
            if (loan.NormalCount == 0) {
                return false
            }

            val overdueParam1 = 1.0 * loan.OverdueLessCount / loan.NormalCount
            if (overdueParam1 >= 0.03) {
                return false
            }
            return true
        }

        //魔镜学历女
        private fun match24(loan: Loan): Boolean {
            //6月18
            if (loan.CurrentRate.toInt() != 18 || loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //征信认证
            if (!loan.CreditValidate) {
                return false
            }

            if (loan.Gender != 2) {
                return false
            }

            //B,C
            if (!setOf("B", "C").contains(loan.CreditCode)) {
                return false
            }

            return true
        }

        //低息飞首
        private fun match25(loan: Loan): Boolean {
            //6月16/15/14
            if (!setOf(14, 15, 16).contains(loan.CurrentRate.toInt()) ||
                    loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //无逾期
            if (loan.OverdueLessCount > 0) {
                return false
            }

            //还过款
            if (loan.NormalCount == 0) {
                return false
            }

            return true
        }

        //低息零逾
        private fun match26(loan: Loan): Boolean {
            //6月16/15/14
            if (!setOf(14, 15, 16).contains(loan.CurrentRate.toInt()) ||
                    loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //无逾期
            if (loan.OverdueLessCount > 0) {
                return false
            }

            //待还本金比例: [0.2, 0.6]
            //OwingPrincipal剩余待还本金/TotalPrincipal累计借款金额
            val owe = 1.0 * loan.OwingPrincipal / loan.TotalPrincipal
            if (owe < 0.2 || owe > 0.6) {
                return false
            }

            return true
        }

        //漫界无逾
        private fun match27(loan: Loan): Boolean {
            //6月18
            if (loan.CurrentRate.toInt() != 18 || loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //无逾期
            if (loan.OverdueLessCount > 0) {
                return false
            }

            //还过款
            if (loan.NormalCount == 0) {
                return false
            }

            //年龄 [25, 28]
            if (loan.Age < 25 || loan.Age > 28) {
                return false
            }

            //待还本金比例: [0.2, 0.6]
            //OwingPrincipal剩余待还本金/TotalPrincipal累计借款金额
            val owe = 1.0 * loan.OwingPrincipal / loan.TotalPrincipal
            if (owe < 0.2 || owe > 0.6) {
                return false
            }

            //还款参数1: [3, 5]
            //(NormalCount + OverdueLessCount)/SuccessCount
            //(正常&短逾期还款次数) / 成功借款次数
            val repayParam1 = 1.0 * (loan.NormalCount + loan.OverdueLessCount) / loan.SuccessCount
            if (repayParam1 < 3 || repayParam1 > 5) {
                return false
            }

            return true
        }

        //飞首无逾
        private fun matchRule28(loan: Loan): Boolean {
            //6月18
            if (loan.CurrentRate.toInt() != 18 || loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //无逾期
            if (loan.OverdueLessCount > 0) {
                return false
            }

            //还过款
            if (loan.NormalCount == 0) {
                return false
            }

            //年龄 [25, 28]
            if (loan.Age < 25 || loan.Age > 28) {
                return false
            }

            //待还本金比例: [0.2, 0.6]
            //OwingPrincipal剩余待还本金/TotalPrincipal累计借款金额
            val owe = 1.0 * loan.OwingPrincipal / loan.TotalPrincipal
            if (owe < 0.2 || owe > 0.6) {
                return false
            }

            return true
        }

        //漫界飞首
        private fun matchRule29(loan: Loan): Boolean {
            //6月20
            if (loan.CurrentRate.toInt() != 20 || loan.Months != 6) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //还过款
            if (loan.NormalCount == 0) {
                return false
            }

            //B,C,D
            if (!setOf("B", "C", "D").contains(loan.CreditCode)) {
                return false
            }

            //待还本金比例: [0.2, 0.6]
            //OwingPrincipal剩余待还本金/TotalPrincipal累计借款金额
            val owe = 1.0 * loan.OwingPrincipal / loan.TotalPrincipal
            if (owe < 0.2 || owe > 0.6) {
                return false
            }

            //还款参数1: [6, 12]
            //(NormalCount + OverdueLessCount)/SuccessCount
            //(正常&短逾期还款次数) / 成功借款次数
            val repayParam1 = 1.0 * (loan.NormalCount + loan.OverdueLessCount) / loan.SuccessCount
            if (repayParam1 < 6 || repayParam1 > 12) {
                return false
            }

            return true
        }

        private fun matchRule30(loan: Loan): Boolean {
            if (!loan.CurrentRate.almostEqual(22.0f)) {
                return false
            }

            if (!loan.CreditValidate) {//征信
                return false
            }

            if (loan.NormalCount == 0) {
                return false
            }

            if (loan.EducationDegree != "本科") {
                return false
            }

            if (loan.Age < 18 || loan.Age > 32) {
                return false
            }

            if (loan.CreditCode != "B") {
                return false
            }

            if (loan.OverdueLessCount / loan.NormalCount >= 0.03) {
                return false
            }

            return true
        }

        private fun matchRule31(loan: Loan): Boolean {
            if (!loan.CurrentRate.almostEqual(22.0f)) {
                return false
            }

            if (!loan.CreditValidate) {//征信
                return false
            }

            //女
            if (loan.Gender != 2) {
                return false
            }

            if (loan.EducationDegree != "本科") {
                return false
            }

            if (loan.CreditCode != "B") {
                return false
            }

            return true
        }

        private fun matchRule32(loan: Loan): Boolean {
            if (!loan.CurrentRate.almostEqual(22.0f)) {
                return false
            }

            if (!loan.CreditValidate) {//征信
                return false
            }

            //女
            if (loan.Gender != 2) {
                return false
            }

            //学历认证
            if (!loan.CertificateValidate) {
                return false
            }

            //B,C
            if (!setOf("B", "C").contains(loan.CreditCode)) {
                return false
            }

            return true
        }

        private fun matchRule33(loan: Loan): Boolean {
            if (loan.Amount <= 16000) {
                return false
            }

            if (!loan.PhoneValidate) {
                return false
            }

            if (loan.SuccessCount == 0) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)

            if (br < 6 || dr >= 0.2) {
                return false
            }

            return true
        }

        private fun matchRule34(loan: Loan): Boolean {
            if (loan.Amount <= 15000) {
                return false
            }

            if (!loan.PhoneValidate) {
                return false
            }

            if (loan.EducationDegree.isNullOrEmpty()) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)

            if (br < 5 || dr >= 0.2) {
                return false
            }

            return true
        }

        //低债有学
        private fun matchRule35(loan: Loan): Boolean {
            if (!loan.PhoneValidate) {
                return false
            }

            if (loan.EducationDegree.isNullOrEmpty()) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)
            val bor = loan.OwingAmount / (loan.HighestDebt + 0.001)
            val chr = loan.Amount / (loan.HighestPrincipal + 0.001)

            if (br < 6 || dr >= 0.2) {
                return false
            }

            if (bor >= 0.5 || chr > 2) {
                return false
            }

            return true
        }

        private fun matchRule36(loan: Loan): Boolean {
            if (!loan.PhoneValidate) {
                return false
            }

            if (loan.EducationDegree.isNullOrEmpty()) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)
            val bor = loan.OwingAmount / (loan.HighestDebt + 0.001)
            val chr = loan.Amount / (loan.HighestPrincipal + 0.001)

            if (br < 6 || dr >= 0.2) {
                return false
            }

            if (bor >= 0.5 || chr > 2) {
                return false
            }

            return true
        }

        private fun matchRule37(loan: Loan): Boolean {
            if (!loan.PhoneValidate) {
                return false
            }

            if (!loan.CreditValidate) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)
            val bor = loan.OwingAmount / (loan.HighestDebt + 0.001)
            val chr = loan.Amount / (loan.HighestPrincipal + 0.001)

            if (br < 6) {
                return false
            }

            if (dr >= 0.2) {
                return false
            }

            if (bor >= 0.5) {
                return false
            }

            if (chr > 2) {
                return false
            }

            return true
        }

        //低债高息
        private fun matchRule38(loan: Loan): Boolean {
            if (!loan.PhoneValidate) {
                return false
            }

            if (loan.OverdueMoreCount > 0) {
                return false
            }

            if (loan.Age < 23) {
                return false
            }

            if (loan.CurrentRate < 18) {
                return false
            }

            val br = loan.NormalCount / (loan.SuccessCount + 0.001)
            val dr = loan.OverdueLessCount / (loan.NormalCount + 0.001)
            val bor = loan.OwingAmount / (loan.HighestDebt + 0.001)
            val chr = loan.Amount / (loan.HighestPrincipal + 0.001)

            if (br < 6 || dr >= 0.2) {
                return false
            }

            if (bor >= 0.5 || chr > 3) {
                return false
            }

            return true
        }

        private fun Float.almostEqual(v: Float): Boolean {
            return Math.abs(this - v) < 0.01
        }
    }
}
