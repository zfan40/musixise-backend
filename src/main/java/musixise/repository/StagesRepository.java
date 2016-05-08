package musixise.repository;

import musixise.domain.Stages;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stages entity.
 */
public interface StagesRepository extends JpaRepository<Stages,Long> {

}
