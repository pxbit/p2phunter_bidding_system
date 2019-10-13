package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.MultiAuth
import org.springframework.data.repository.CrudRepository

interface MultiAuthRepo: CrudRepository<MultiAuth, MultiAuth.MultiKey> {
}