package musixise.repository;

import musixise.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhaowei on 17/5/14.
 */
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Config findOneByCkey(String ckey);
}
