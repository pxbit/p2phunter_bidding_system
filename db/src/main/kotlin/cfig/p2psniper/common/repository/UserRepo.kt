package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.User
import org.springframework.data.repository.CrudRepository

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/24/17
 */
interface UserRepo : CrudRepository<User, Long> {
    fun findByName(name: String): User?
    fun findByEmail(email: String): User?
}