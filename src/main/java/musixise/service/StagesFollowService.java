package musixise.service;

import musixise.domain.StagesFollow;
import musixise.web.rest.dto.StagesFollowDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing StagesFollow.
 */
public interface StagesFollowService {

    /**
     * Save a stagesFollow.
     * 
     * @param stagesFollowDTO the entity to save
     * @return the persisted entity
     */
    StagesFollowDTO save(StagesFollowDTO stagesFollowDTO);

    /**
     *  Get all the stagesFollows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StagesFollow> findAll(Pageable pageable);

    /**
     *  Get the "id" stagesFollow.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StagesFollowDTO findOne(Long id);

    /**
     *  Delete the "id" stagesFollow.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stagesFollow corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<StagesFollow> search(String query, Pageable pageable);
}
