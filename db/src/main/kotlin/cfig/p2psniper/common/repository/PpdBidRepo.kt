package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.PpdBid
import org.springframework.data.repository.CrudRepository

interface PpdBidRepo: CrudRepository<PpdBid, Long>
