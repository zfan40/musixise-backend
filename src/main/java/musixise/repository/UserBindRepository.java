package musixise.repository;

import musixise.domain.UserBind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhaowei on 16/12/20.
 */
public interface UserBindRepository extends JpaRepository<UserBind, Long> {
    UserBind findByOpenId(String openId);
    UserBind findByOpenIdAndProvider(String openId, String provider);
    List<UserBind> findAllByLogin(String login);
}
