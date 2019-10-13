package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.PpdUser
import org.springframework.data.repository.CrudRepository

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 9/5/17
 */
interface PpdUserRepo : CrudRepository<PpdUser, Int>
