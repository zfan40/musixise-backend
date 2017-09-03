package musixise.service.impl;

import musixise.domain.WorkList;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.service.WorkListService;
import musixise.utils.PageUtil;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.mapper.WorkListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing WorkList.
 */
@Service
@Transactional
public class WorkListServiceImpl implements WorkListService{

    private final Logger log = LoggerFactory.getLogger(WorkListServiceImpl.class);

    @Inject private WorkListRepository workListRepository;

    @Inject private WorkListMapper workListMapper;

    @Inject private WorkListSearchRepository workListSearchRepository;

    @Inject WorkListFollowServiceImpl workListFollowService;

    /**
     * Save a workList.
     *
     * @param workListDTO the entity to save
     * @return the persisted entity
     */
    public WorkListDTO save(WorkListDTO workListDTO) {
        log.debug("Request to save WorkList : {}", workListDTO);
        WorkList workList = workListMapper.workListDTOToWorkList(workListDTO);
        workList = workListRepository.save(workList);
        WorkListDTO result = workListMapper.workListToWorkListDTO(workList);
        workListSearchRepository.save(workList);
        return result;
    }

    /**
     *  Get all the workLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkList> findAll(Pageable pageable) {
        log.debug("Request to get all WorkLists");
        Page<WorkList> result = workListRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one workList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public WorkListDTO findOne(Long id) {
        log.debug("Request to get WorkList : {}", id);
        WorkList workList = workListRepository.findOne(id);
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(workList);
        //+pv
        updatePvById(id);
        return workListDTO;
    }

    @Async
    private void updatePvById(Long id) {
        workListRepository.updatePvById(id);
    }

    /**
     *  Delete the  workList by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkList : {}", id);
        workListRepository.delete(id);
        workListSearchRepository.delete(id);
    }

    /**
     * Search for the workList corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkLists for query {}", query);
        return workListSearchRepository.search(queryStringQuery(query), pageable);
    }

    public PageDTO<WorkListDTO> findAllByUserIdOrderByIdDesc(Long uid, Pageable pageable) {

        Page<WorkList> result = workListRepository.findAllByUserIdOrderByIdDesc(uid, pageable);

        List<WorkListDTO> workListDTOList = workListMapper.workListsToWorkListDTOs(result.getContent());

        return PageUtil.getPage(workListDTOList, result);
    }

    @Override
    public List<WorkListDTO> getHotSongs(Long userId) {
        List<WorkList> workLists = workListRepository.findTop10ByOrderByPvDesc();
        return getWorkListDTO(userId, workLists);
    }

    @Override
    public List<WorkListDTO> getLatestSongs(Long userId) {
        List<WorkList> workLists = workListRepository.findTop10ByOrderByIdDesc();
        return getWorkListDTO(userId, workLists);
    }

    public List<WorkListDTO> getWorkListDTO(Long userId, List<WorkList> workListList) {
        List<WorkListDTO> workListDTOList = workListMapper.workListsToWorkListDTOs(workListList);

        if (userId > 0) {
            for (WorkListDTO workListDTO : workListDTOList) {
                if (workListFollowService.isFavorite(userId, workListDTO.getId())) {
                    workListDTO.setFavStatus(1);
                } else {
                    workListDTO.setFavStatus(0);
                }
            }
        }
        return workListDTOList;
    }
}
