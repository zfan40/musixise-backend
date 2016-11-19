package musixise.repository;

import musixise.domain.MusixiserFollow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the MusixiserFollow entity.
 */
public interface MusixiserFollowRepository extends JpaRepository<MusixiserFollow,Long> {

    Page<MusixiserFollow> findAllByUserId(Pageable pageable, Long userId);

    @Modifying
    @Transactional
    @Query("delete MusixiserFollow where user_id=?1 and follow_uid  = ?2")
    int deleteByUserIdAndFollowUid(Long userId,  Long followUid);
}
