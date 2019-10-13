package cfig.p2psniper.common.repository;

import cfig.p2psniper.common.entity.PendingLoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PendingLoanRepoExt {
    private Logger log = LoggerFactory.getLogger(PendingLoanRepoExt.class);

    @PersistenceContext
    private EntityManager em;

    public Long findMaxListingId() {
        Long theMax = 100000000L;
        try {
            BigInteger cnt = (BigInteger) em.createNativeQuery("select count(ListingId) from PendingLoan").getSingleResult();
            if (cnt.intValue() > 0) {
                theMax = (Long) em.createQuery("SELECT max(c.ListingId) FROM PendingLoan c").getSingleResult();
            } else {
                log.info("Empty PendingList");
            }
        } catch (NoResultException e) {
            //ignore
        } catch (Exception e) {
            log.error(e.toString());
            log.warn("using default maxId");
        }

        return theMax;
    }

    public PendingLoan findById(Long id) {
        PendingLoan ret = null;
        try {
            Query q = em.createQuery("select c from PendingLoan c where c.ListingId = :id");
            q.setParameter("id", id);
            ret = (PendingLoan) q.getSingleResult();
        } catch (NoResultException e) {
            log.debug("Pending id " + id + " not found");
        }
        return ret;
    }

    public List<Long> findRecentIds(int max) {
        log.info("findRecentIds()...");
        long start = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        try {
            BigInteger cnt = (BigInteger) em.createNativeQuery("select count(ListingId) from PendingLoan c where c.dead = false").getSingleResult();
            Query q = em.createQuery("SELECT c.ListingId FROM PendingLoan c where c.dead = false order by c.ListingId desc");
            q.setMaxResults(max);
            if (cnt.intValue() > 0) {
                list = (List<Long>) q.getResultList();
            } else {
                log.info("Empty PendingList");
            }
        } catch (NoResultException e) {
            //ignore
        } catch (Exception e) {
            log.error(e.toString());
            log.warn("using default maxId");
        }
        long end = System.currentTimeMillis();
        log.info("findRecentIds() took " + (end - start) + "ms");

        return list;
    }

    public List<Long> findOldIds(int max) {
        log.info("findOldIds()...");
        long start = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        try {
            BigInteger cnt = (BigInteger) em.createNativeQuery("select count(ListingId) from PendingLoan c where c.dead = false").getSingleResult();
            Query q = em.createQuery("SELECT c.ListingId FROM PendingLoan c where c.dead = false order by c.ListingId asc");
            q.setMaxResults(max);
            if (cnt.intValue() > 0) {
                list = (List<Long>) q.getResultList();
            } else {
                log.info("Empty PendingList");
            }
        } catch (NoResultException e) {
            //ignore
        } catch (Exception e) {
            log.error(e.toString());
            log.warn("using default maxId");
        }
        long end = System.currentTimeMillis();
        log.info("findOldIds() took " + (end - start) + "ms");

        return list;
    }
}
