package musixise.service;

import musixise.domain.Musixiser;
import musixise.web.rest.dto.MusixiserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Musixiser.
 */
public interface MusixiserService {

    /**
     * Save a musixiser.
     *
     * @param musixiserDTO the entity to save
     * @return the persisted entity
     */
    MusixiserDTO save(MusixiserDTO musixiserDTO);

    /**
     *  Get all the musixisers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Musixiser> findAll(Pageable pageable);

    /**
     *  Get the "id" musixiser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MusixiserDTO findOne(Long id);

    /**
     *  Delete the "id" musixiser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the musixiser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Musixiser> search(String query, Pageable pageable);

    String getDefaultAvatar();
}
