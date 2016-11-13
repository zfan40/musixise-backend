package musixise.repository;


import musixise.domain.WorkListFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the WorkListFollow entity.
 */
public interface WorkListFollowRepository extends JpaRepository<WorkListFollow,Long> {


    List<WorkListFollow> findAllByUserIdOrderByIdDesc(Long userId);

    @Query(value="select * from work_list_follow w where w.user_id = :userId and w.work_id= :workId", nativeQuery = true)
    Optional<WorkListFollow> findOneByUserIdAndWorkId(@Param("userId") Long userId, @Param("workId") Long workId);

}
