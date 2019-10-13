package cfig.p2psniper.common.entity

import javax.persistence.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/14/17
 */
@Entity
data class CustomAARule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var minMonths: Int = 0,

        var maxMonths: Int = 0,

        var minRate: Float = 0f,

        var maxRate: Float = 99f,

        var amount: Int = 50,

        var amount1Day: Int = 10000,

        @OneToOne
        var ppdUser: PpdUser = PpdUser()
) {
    fun looksLike(other: Any?): Boolean {
        if (other !is CustomAARule) {
            return false
        }
        return (this.minMonths == other.minMonths && this.maxMonths == other.maxMonths
                && (Math.abs(this.minRate - other.minRate) < 1.0f))
    }

    fun mirror(): CustomAARule {
        val ret = CustomAARule()
        ret.id = this.id
        ret.minMonths = this.minMonths
        ret.maxMonths = this.maxMonths
        ret.minRate = this.minRate
        ret.maxRate = this.maxRate
        ret.amount = this.amount
        ret.amount1Day = this.amount1Day
        return ret
    }

    fun toMiniString(): String {
        return "CustomAARule{ id: $id, rate: $minRate-$maxRate, months: $minMonths-$maxMonths, amount: $amount, amount1Day: $amount1Day}"
    }
}
