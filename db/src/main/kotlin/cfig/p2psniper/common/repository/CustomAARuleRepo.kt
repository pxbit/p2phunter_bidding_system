package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.CustomAARule
import cfig.p2psniper.common.entity.PpdUser
import org.springframework.data.repository.CrudRepository

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/12/17
 */
interface CustomAARuleRepo : CrudRepository<CustomAARule, Long> {
    fun findByPpdUser(ppdUser: PpdUser): List<CustomAARule>?
}