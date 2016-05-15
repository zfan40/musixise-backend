package musixise.service;

import musixise.domain.Audience;
import musixise.web.rest.dto.AudienceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Audience.
 */
public interface AudienceService {

    /**
     * Save a audience.
     * 
     * @param audienceDTO the entity to save
     * @return the persisted entity
     */
    AudienceDTO save(AudienceDTO audienceDTO);

    /**
     *  Get all the audiences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Audience> findAll(Pageable pageable);

    /**
     *  Get the "id" audience.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    AudienceDTO findOne(Long id);

    /**
     *  Delete the "id" audience.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the audience corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Audience> search(String query, Pageable pageable);
}
