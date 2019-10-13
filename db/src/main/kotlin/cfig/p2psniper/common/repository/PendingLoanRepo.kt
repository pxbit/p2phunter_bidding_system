package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.PendingLoan
import org.springframework.data.repository.CrudRepository

/**
 * Created by cfig (yuyezhong@gmail.com)) on 3/21/2018.
 */
interface PendingLoanRepo: CrudRepository<PendingLoan, Long> {
//    fun findByListingId(ListingId: Long): PendingLoan?
}
