package musixise.service.impl;

import musixise.service.WorkListService;
import musixise.domain.WorkList;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.mapper.WorkListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WorkList.
 */
@Service
@Transactional
public class WorkListServiceImpl implements WorkListService{

    private final Logger log = LoggerFactory.getLogger(WorkListServiceImpl.class);

    @Inject
    private WorkListRepository workListRepository;

    @Inject
    private WorkListMapper workListMapper;

    @Inject
    private WorkListSearchRepository workListSearchRepository;

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
    @Transactional(readOnly = true)
    public WorkListDTO findOne(Long id) {
        log.debug("Request to get WorkList : {}", id);
        WorkList workList = workListRepository.findOne(id);
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(workList);
        return workListDTO;
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
}
