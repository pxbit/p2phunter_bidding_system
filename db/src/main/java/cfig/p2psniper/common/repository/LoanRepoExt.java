package cfig.p2psniper.common.repository;

import cfig.p2psniper.common.entity.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by cfig (yuyezhong@gmail.com)) on 8/18/17.
 */
@Repository
public class LoanRepoExt {
    private Logger log = LoggerFactory.getLogger(LoanRepoExt.class);

    @PersistenceContext
    private EntityManager em;

    public List<Loan> findLoansWithNullStatus(int maxResults) {
        List<Loan> loans = em.createQuery("SELECT c from Loan c where c.XStatus is null")
                .setMaxResults(maxResults)
                .getResultList();
        return loans;
    }

    public List<Loan> findLoansWithNullStatus() {
        return findLoansWithNullStatus(20);
    }

    public Loan findByListingId(Long listingId) {
        Loan loan = null;
        try {
            loan = (Loan) em.createQuery("SELECT c FROM Loan c WHERE c.ListingId = :id").setParameter("id", listingId).getSingleResult();
        } catch (NoResultException e) {
            //ignore
        } catch (Exception e) {
            log.error("Unexpected Exception: findByListingId(" + listingId + "):" + e);
        }

        return loan;
    }

    public void getItemCount() {
        Query queryTotal = em.createQuery("SELECT count(c.ListingId) FROM Loan c");
        long count = (long) queryTotal.getSingleResult();
        log.info("count = " + count);
    }

    /*
     * counting and paging
     * */
    public void test() {
        List<Integer> loanIds = em.createQuery("SELECT c.ListingId FROM Loan c")
                .getResultList();
        List<Loan> loans = em.createQuery("SELECT c from Loan c where c.ListingId in :ids")
                .setParameter("ids", loanIds.subList(0, 100))
                .getResultList();
        for (Loan item : loans) {
            log.info(item.toString());
        }
    }

    @Transactional
    public void deleteList(List<Integer> dl) {
        em.createQuery("DELETE from Loan c where c.ListingId in :ids")
                .setParameter("ids", dl).executeUpdate();
    }
}
