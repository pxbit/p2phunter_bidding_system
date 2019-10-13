package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.PpLog
import org.springframework.data.repository.CrudRepository

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 8/31/17
 */
interface PpLogRepo: CrudRepository<PpLog, Long>
