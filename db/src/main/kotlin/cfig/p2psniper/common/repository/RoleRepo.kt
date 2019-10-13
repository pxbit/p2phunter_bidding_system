package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.Role
import org.springframework.data.repository.CrudRepository

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/24/17
 */
interface RoleRepo : CrudRepository<Role, Long> {
    fun findByRole(role: String): Role
}