package cfig.p2psniper.common.entity

import javax.persistence.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/11/17
 */
@Entity
data class SystemAARule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var maxMonths: Int = 0,

        var minRate: Float = 0f,

        @ManyToMany(cascade = arrayOf(CascadeType.ALL))
        var users: Set<PpdUser> = setOf()
)
