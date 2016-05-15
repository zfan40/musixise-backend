package musixise.repository;

import musixise.domain.Audience;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Audience entity.
 */
public interface AudienceRepository extends JpaRepository<Audience,Long> {

}
