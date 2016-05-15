package musixise.repository;

import musixise.domain.StagesFollow;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StagesFollow entity.
 */
public interface StagesFollowRepository extends JpaRepository<StagesFollow,Long> {

}
