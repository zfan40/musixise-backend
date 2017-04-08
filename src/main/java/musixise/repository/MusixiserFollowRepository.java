package musixise.repository;

import musixise.domain.MusixiserFollow;
import musixise.domain.WorkListFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.PermitAll;
import java.util.Optional;

/**
 * Spring Data JPA repository for the MusixiserFollow entity.
 */
public interface MusixiserFollowRepository extends JpaRepository<MusixiserFollow,Long> {

    Page<MusixiserFollow> findAllByUserId(Pageable pageable, Long userId);

    Page<MusixiserFollow> findAllByFollowId(Pageable pageable, Long userId);

    @Modifying
    @Transactional
    @Query("delete MusixiserFollow where user_id=?1 and follow_uid  = ?2")
    int deleteByUserIdAndFollowUid(Long userId,  Long followUid);

    @Query(value="SELECT * FROM `mu_musixiser_follow` m LEFT JOIN mu_user u on u.id=m.follow_uid WHERE m.user_id=?1 and m.follow_uid=?2", nativeQuery = true)
    MusixiserFollow findByUserIdAndFollowUid(@Param("userId") Long userId, @Param("followId") Long followId);

    int countByUserId(Long userId);

    int countByFollowUid(Long userId);

    Optional<MusixiserFollow> findOneByUserIdAndFollowId(@Param("userId") Long userId, @Param("followId") Long followId);


}
