package cfig.p2psniper.common.repository;

import cfig.p2psniper.common.entity.PpdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by cfig (yuyezhong@gmail.com)) on 8/15/17.
 */
@Repository
public class PpdUserRepoExt {
    private Logger log = LoggerFactory.getLogger(PpdUserRepoExt.class);

    @PersistenceContext
    private EntityManager em;

    public PpdUser findByUserId(Integer userId) {
        PpdUser u = null;
        try {
            u = (PpdUser) em.createQuery("SELECT c FROM PpdUser c WHERE c.UserId = :userId").setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return u;
    }

    public PpdUser findByUserName(String userName) {
        PpdUser u = null;
        try {
            u = (PpdUser) em.createQuery("SELECT c FROM PpdUser c WHERE c.UserName = :name").setParameter("name", userName).getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return u;
    }

    public PpdUser findByOpenID(String openID) {
        log.info("findByOpenID " + openID);
        if (em == null) {
            log.error("em is null");
        }
        PpdUser u = null;
        try {
            u = (PpdUser) em.createQuery("SELECT c FROM PpdUser c WHERE c.OpenID = :id").setParameter("id", openID).getSingleResult();
        } catch (Exception e) {
            log.error(e.toString());
        }

        return u;
    }
}
