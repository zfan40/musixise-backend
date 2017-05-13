package musixise.repository;

import musixise.domain.Musixiser;
import musixise.domain.WorkList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Musixiser entity.
 */
public interface MusixiserRepository extends JpaRepository<Musixiser,Long> {

    Musixiser findOneByUserId(Long userId);


    @Transactional
    @Modifying
    @Query("update Musixiser w set w.followNum = ?2 where w.userId = ?1 ")
    int updateFollowNumById(Long id, Integer followNum);

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.fansNum = ?2 where w.userId = ?1 ")
    int updateFanswNumById(Long id, Integer fansNum);

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.songNum = ?2 where w.userId = ?1 ")
    int updateWorkNumById(Long id, Integer songNum);


    List<Musixiser> findTop10ByOrderByPvDesc();
    List<Musixiser> findTop10ByOrderByIdDesc();

}
