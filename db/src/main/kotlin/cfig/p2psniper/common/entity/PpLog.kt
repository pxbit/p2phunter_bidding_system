package cfig.p2psniper.common.entity

import java.sql.Timestamp
import javax.persistence.*

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 8/31/17
 */
@Entity
data class PpLog(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @OneToOne
        var user: PpdUser = PpdUser(),

        var info: String = "",

//        @Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
        var ts: Timestamp? = Timestamp(System.currentTimeMillis()))
