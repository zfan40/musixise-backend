package musixise.repository;

import musixise.domain.WorkListFollow;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkListFollow entity.
 */
public interface WorkListFollowRepository extends JpaRepository<WorkListFollow,Long> {

}
