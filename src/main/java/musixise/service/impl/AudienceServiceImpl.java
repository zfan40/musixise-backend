package musixise.service.impl;

import musixise.service.AudienceService;
import musixise.domain.Audience;
import musixise.repository.AudienceRepository;
import musixise.repository.search.AudienceSearchRepository;
import musixise.web.rest.dto.AudienceDTO;
import musixise.web.rest.mapper.AudienceMapper;
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
 * Service Implementation for managing Audience.
 */
@Service
@Transactional
public class AudienceServiceImpl implements AudienceService{

    private final Logger log = LoggerFactory.getLogger(AudienceServiceImpl.class);
    
    @Inject
    private AudienceRepository audienceRepository;
    
    @Inject
    private AudienceMapper audienceMapper;
    
    @Inject
    private AudienceSearchRepository audienceSearchRepository;
    
    /**
     * Save a audience.
     * 
     * @param audienceDTO the entity to save
     * @return the persisted entity
     */
    public AudienceDTO save(AudienceDTO audienceDTO) {
        log.debug("Request to save Audience : {}", audienceDTO);
        Audience audience = audienceMapper.audienceDTOToAudience(audienceDTO);
        audience = audienceRepository.save(audience);
        AudienceDTO result = audienceMapper.audienceToAudienceDTO(audience);
        audienceSearchRepository.save(audience);
        return result;
    }

    /**
     *  Get all the audiences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Audience> findAll(Pageable pageable) {
        log.debug("Request to get all Audiences");
        Page<Audience> result = audienceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one audience by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AudienceDTO findOne(Long id) {
        log.debug("Request to get Audience : {}", id);
        Audience audience = audienceRepository.findOne(id);
        AudienceDTO audienceDTO = audienceMapper.audienceToAudienceDTO(audience);
        return audienceDTO;
    }

    /**
     *  Delete the  audience by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Audience : {}", id);
        audienceRepository.delete(id);
        audienceSearchRepository.delete(id);
    }

    /**
     * Search for the audience corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Audience> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Audiences for query {}", query);
        return audienceSearchRepository.search(queryStringQuery(query), pageable);
    }
}
