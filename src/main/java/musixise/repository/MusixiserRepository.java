package musixise.repository;

import musixise.domain.Musixiser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Musixiser entity.
 */
public interface MusixiserRepository extends JpaRepository<Musixiser,Long> {

    Musixiser findOneByUserId(Long userId);
}
