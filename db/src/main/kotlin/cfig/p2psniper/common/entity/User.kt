package cfig.p2psniper.common.entity

import org.hibernate.validator.constraints.Length
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.NotEmpty

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/24/17
 */
@Entity
data class User( //uniq user
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @NotNull
        @NotEmpty
        @Length(max = 32)
        @Column(unique = true)
        var name: String = "",

        @NotNull
        @NotEmpty
        @Length(max = 128)
        var pwd: String = "",

        @Length(max = 64)
        @Column(unique = true)
        var email: String? = null,

        @NotNull
        var active: Boolean = true,

        @NotNull
        var createTime: Timestamp = Timestamp(System.currentTimeMillis()),

        @OneToMany
        var ppdUsers: MutableSet<PpdUser> = mutableSetOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.ALL))
        var roles: Set<Role> = mutableSetOf()
)
