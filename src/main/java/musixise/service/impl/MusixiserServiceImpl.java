package musixise.service.impl;

import musixise.service.MusixiserService;
import musixise.domain.Musixiser;
import musixise.repository.MusixiserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.web.rest.dto.MusixiserDTO;
import musixise.web.rest.mapper.MusixiserMapper;
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
 * Service Implementation for managing Musixiser.
 */
@Service
@Transactional
public class MusixiserServiceImpl implements MusixiserService{

    private final Logger log = LoggerFactory.getLogger(MusixiserServiceImpl.class);
    
    @Inject
    private MusixiserRepository musixiserRepository;
    
    @Inject
    private MusixiserMapper musixiserMapper;
    
    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;
    
    /**
     * Save a musixiser.
     * 
     * @param musixiserDTO the entity to save
     * @return the persisted entity
     */
    public MusixiserDTO save(MusixiserDTO musixiserDTO) {
        log.debug("Request to save Musixiser : {}", musixiserDTO);
        Musixiser musixiser = musixiserMapper.musixiserDTOToMusixiser(musixiserDTO);
        musixiser = musixiserRepository.save(musixiser);
        MusixiserDTO result = musixiserMapper.musixiserToMusixiserDTO(musixiser);
        musixiserSearchRepository.save(musixiser);
        return result;
    }

    /**
     *  Get all the musixisers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Musixiser> findAll(Pageable pageable) {
        log.debug("Request to get all Musixisers");
        Page<Musixiser> result = musixiserRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one musixiser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MusixiserDTO findOne(Long id) {
        log.debug("Request to get Musixiser : {}", id);
        Musixiser musixiser = musixiserRepository.findOne(id);
        MusixiserDTO musixiserDTO = musixiserMapper.musixiserToMusixiserDTO(musixiser);
        return musixiserDTO;
    }

    /**
     *  Delete the  musixiser by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Musixiser : {}", id);
        musixiserRepository.delete(id);
        musixiserSearchRepository.delete(id);
    }

    /**
     * Search for the musixiser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Musixiser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Musixisers for query {}", query);
        return musixiserSearchRepository.search(queryStringQuery(query), pageable);
    }
}
