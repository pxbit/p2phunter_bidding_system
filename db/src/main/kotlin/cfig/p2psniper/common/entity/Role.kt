package cfig.p2psniper.common.entity

import org.hibernate.validator.constraints.Length
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.NotEmpty

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/24/17
 */
@Entity
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @NotNull
        @NotEmpty
        @Length(max = 20)
        @Column(unique = true)
        var role: String? = null
)
