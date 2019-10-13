package cfig.p2psniper.common.repository;

import cfig.p2psniper.common.entity.PpdUser;
import cfig.p2psniper.common.entity.User;
import cfig.p2psniper.common.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 * Author: cfig (yuyezhong@gmail.com))
 * Created on: 10/1/17
 */
@Repository
public class UserRepoExt {
    private Logger log = LoggerFactory.getLogger(UserRepoExt.class);

    @Autowired
    private UserRepo userRepo;

    public boolean isPpdUserBindedByOther(String ppdOpenId, Long userId) {
        Iterator<User> itu = userRepo.findAll().iterator();
        while (itu.hasNext()) {
            User cu = itu.next();
            Iterator<PpdUser> itp = cu.getPpdUsers().iterator();
            while (itp.hasNext()) {
                if (itp.next().getOpenID().equals(ppdOpenId) && (cu.getId() != userId)) {
                    log.warn(ppdOpenId + " already binded with user (" + userId + ", " + cu.getName() + ")");
                    return true;
                }
            }
        }
        return false;
    }
}
