package cfig.p2psniper.common.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PpdBid2RepoExt {
    private Logger log = LoggerFactory.getLogger(PpdBid2RepoExt.class);

    @Autowired
    private PpdBid2Repo ppdBid2Repo;

    @PersistenceContext
    private EntityManager em;

    public List<Long> findPendingOrders() {
        Query q = em.createQuery("SELECT c.id FROM PpdBid2 c where c.checked = 0");
        q.setMaxResults(1000);
        return (List<Long>) q.getResultList();
    }
}
