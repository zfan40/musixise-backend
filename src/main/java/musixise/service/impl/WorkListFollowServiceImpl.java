package musixise.service.impl;

import musixise.domain.WorkListFollow;
import musixise.repository.UserRepository;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.WorkListRepository;
import musixise.security.SecurityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by zhaowei on 16/12/14.
 */
@Service
@Transactional
public class WorkListFollowServiceImpl {

    @Inject
    private WorkListFollowRepository workListFollowRepository;

    @Inject
    private UserRepository userRepository;

    @Inject WorkListRepository workListRepository;


    public Optional<WorkListFollow> getFollowWorkInfo(Long workId) {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).map(
            u -> {
                Optional<WorkListFollow> workList = workListFollowRepository.findOneByUserIdAndWorkId(u.getId(), workId);
                if (workList.isPresent()) {
                    return workList.get();
                } else {
                    return null;
                }
            });
    }

    /**
     * 更新指定作品收藏数
     * @param id 作品ID
     */
    @Async
    public void updateFavoriteCount(Long id) {
        int count =  workListFollowRepository.countByWorkId(id);
        int u = workListRepository.updateCollectNumById(id, count);
    }
}
