package musixise.repository;

import musixise.domain.MusixiserFollow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MusixiserFollow entity.
 */
public interface MusixiserFollowRepository extends JpaRepository<MusixiserFollow,Long> {

    Page<MusixiserFollow> findAllByUserId(Pageable pageable, Long userId);
}
