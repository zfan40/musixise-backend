package musixise.service.impl;

import musixise.service.StagesFollowService;
import musixise.domain.StagesFollow;
import musixise.repository.StagesFollowRepository;
import musixise.repository.search.StagesFollowSearchRepository;
import musixise.web.rest.dto.StagesFollowDTO;
import musixise.web.rest.mapper.StagesFollowMapper;
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
 * Service Implementation for managing StagesFollow.
 */
@Service
@Transactional
public class StagesFollowServiceImpl implements StagesFollowService{

    private final Logger log = LoggerFactory.getLogger(StagesFollowServiceImpl.class);
    
    @Inject
    private StagesFollowRepository stagesFollowRepository;
    
    @Inject
    private StagesFollowMapper stagesFollowMapper;
    
    @Inject
    private StagesFollowSearchRepository stagesFollowSearchRepository;
    
    /**
     * Save a stagesFollow.
     * 
     * @param stagesFollowDTO the entity to save
     * @return the persisted entity
     */
    public StagesFollowDTO save(StagesFollowDTO stagesFollowDTO) {
        log.debug("Request to save StagesFollow : {}", stagesFollowDTO);
        StagesFollow stagesFollow = stagesFollowMapper.stagesFollowDTOToStagesFollow(stagesFollowDTO);
        stagesFollow = stagesFollowRepository.save(stagesFollow);
        StagesFollowDTO result = stagesFollowMapper.stagesFollowToStagesFollowDTO(stagesFollow);
        stagesFollowSearchRepository.save(stagesFollow);
        return result;
    }

    /**
     *  Get all the stagesFollows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<StagesFollow> findAll(Pageable pageable) {
        log.debug("Request to get all StagesFollows");
        Page<StagesFollow> result = stagesFollowRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one stagesFollow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StagesFollowDTO findOne(Long id) {
        log.debug("Request to get StagesFollow : {}", id);
        StagesFollow stagesFollow = stagesFollowRepository.findOne(id);
        StagesFollowDTO stagesFollowDTO = stagesFollowMapper.stagesFollowToStagesFollowDTO(stagesFollow);
        return stagesFollowDTO;
    }

    /**
     *  Delete the  stagesFollow by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StagesFollow : {}", id);
        stagesFollowRepository.delete(id);
        stagesFollowSearchRepository.delete(id);
    }

    /**
     * Search for the stagesFollow corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StagesFollow> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StagesFollows for query {}", query);
        return stagesFollowSearchRepository.search(queryStringQuery(query), pageable);
    }
}
