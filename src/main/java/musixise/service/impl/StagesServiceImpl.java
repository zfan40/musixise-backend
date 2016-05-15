package musixise.service.impl;

import musixise.service.StagesService;
import musixise.domain.Stages;
import musixise.repository.StagesRepository;
import musixise.repository.search.StagesSearchRepository;
import musixise.web.rest.dto.StagesDTO;
import musixise.web.rest.mapper.StagesMapper;
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
 * Service Implementation for managing Stages.
 */
@Service
@Transactional
public class StagesServiceImpl implements StagesService{

    private final Logger log = LoggerFactory.getLogger(StagesServiceImpl.class);
    
    @Inject
    private StagesRepository stagesRepository;
    
    @Inject
    private StagesMapper stagesMapper;
    
    @Inject
    private StagesSearchRepository stagesSearchRepository;
    
    /**
     * Save a stages.
     * 
     * @param stagesDTO the entity to save
     * @return the persisted entity
     */
    public StagesDTO save(StagesDTO stagesDTO) {
        log.debug("Request to save Stages : {}", stagesDTO);
        Stages stages = stagesMapper.stagesDTOToStages(stagesDTO);
        stages = stagesRepository.save(stages);
        StagesDTO result = stagesMapper.stagesToStagesDTO(stages);
        stagesSearchRepository.save(stages);
        return result;
    }

    /**
     *  Get all the stages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Stages> findAll(Pageable pageable) {
        log.debug("Request to get all Stages");
        Page<Stages> result = stagesRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one stages by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StagesDTO findOne(Long id) {
        log.debug("Request to get Stages : {}", id);
        Stages stages = stagesRepository.findOne(id);
        StagesDTO stagesDTO = stagesMapper.stagesToStagesDTO(stages);
        return stagesDTO;
    }

    /**
     *  Delete the  stages by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stages : {}", id);
        stagesRepository.delete(id);
        stagesSearchRepository.delete(id);
    }

    /**
     * Search for the stages corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stages> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stages for query {}", query);
        return stagesSearchRepository.search(queryStringQuery(query), pageable);
    }
}
