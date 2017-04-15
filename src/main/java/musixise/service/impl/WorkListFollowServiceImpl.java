package musixise.service.impl;

import musixise.domain.WorkListFollow;
import musixise.repository.UserRepository;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.WorkListRepository;
import musixise.security.SecurityUtils;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.WorkListFollowDTO;
import musixise.web.rest.mapper.WorkListFollowMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 16/12/14.
 */
@Service
@Transactional
public class WorkListFollowServiceImpl {

    @Inject
    private WorkListFollowRepository workListFollowRepository;

    @Inject private UserRepository userRepository;

    @Inject WorkListRepository workListRepository;

    @Inject WorkListFollowMapper workListFollowMapper;

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


    public PageDTO<WorkListFollowDTO> findAllByUserIdOrderByIdDesc(Long uid, Pageable pageable) {

        Page<WorkListFollow> result = workListFollowRepository.findAllByUserIdOrderByIdDesc(uid, pageable);

        List<WorkListFollowDTO> workListDTOList = workListFollowMapper.workListFollowsToWorkListFollowDTOs(result.getContent());

        PageDTO pageDTO = new PageDTO(workListDTOList, result.getTotalElements(),
            result.hasNext(), result.getTotalPages(), result.getSize(), result.getNumber(),
            result.getSort(), result.isFirst(), result.getNumberOfElements());

        return pageDTO;
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
