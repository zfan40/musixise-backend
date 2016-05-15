package musixise.service;

import musixise.domain.Stages;
import musixise.web.rest.dto.StagesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Stages.
 */
public interface StagesService {

    /**
     * Save a stages.
     * 
     * @param stagesDTO the entity to save
     * @return the persisted entity
     */
    StagesDTO save(StagesDTO stagesDTO);

    /**
     *  Get all the stages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Stages> findAll(Pageable pageable);

    /**
     *  Get the "id" stages.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StagesDTO findOne(Long id);

    /**
     *  Delete the "id" stages.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stages corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Stages> search(String query, Pageable pageable);
}
