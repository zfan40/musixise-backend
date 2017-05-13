package musixise.repository;

import musixise.domain.WorkList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkList entity.
 */
public interface WorkListRepository extends JpaRepository<WorkList,Long> {

    // PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "startTime");
    Page<WorkList> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<WorkList> findAllByUserIdOrderByIdDesc(Long userId);

    @Transactional
    @Modifying
    @Query("update WorkList w set w.status = ?1 where w.id = ?2 and w.userId=?3")
    int updateStatusByUserIdAndWorkId(Integer status, Long workId, Long userId);

    @Transactional
    @Modifying
    @Query("update WorkList w set w.collectNum = ?2 where w.id = ?1 ")
    int updateCollectNumById(Long id, Integer collectNum);

    int countByUserId(Long userId);

    List<WorkList> findTop10ByOrderByPvDesc();
    List<WorkList> findTop10ByOrderByIdDesc();
}


