package cfig.p2psniper.common.repository

import cfig.p2psniper.common.entity.Loan
import org.springframework.data.repository.CrudRepository

/**
 * Created by cfig (yuyezhong@gmail.com)) on 8/18/17.
 */
interface LoanRepo: CrudRepository<Loan, Long>
