package musixise.repository;

import musixise.domain.SocialUserConnection;
import musixise.domain.WorkList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkList entity.
 */
public interface WorkListRepository extends JpaRepository<WorkList,Long> {

    List<WorkList> findAllByUserIdOrderByIdDesc(Long userId);

}
