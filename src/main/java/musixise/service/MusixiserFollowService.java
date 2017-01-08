package musixise.service;

import musixise.domain.MusixiserFollow;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MusixiserFollow.
 */
public interface MusixiserFollowService {

    /**
     * Save a musixiserFollow.
     *
     * @param musixiserFollowDTO the entity to save
     * @return the persisted entity
     */
    MusixiserFollowDTO save(MusixiserFollowDTO musixiserFollowDTO);

    /**
     *  Get all the musixiserFollows.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MusixiserFollow> findAll(Pageable pageable);

    Page<MusixiserFollow> findAllByUserId(Pageable pageable, Long userId);

    PageDTO<MusixiserFollowDTO> findFollowsByUserId(Pageable pageable, Long userId);

    /**
     *  Get the "id" musixiserFollow.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MusixiserFollowDTO findOne(Long id);

    /**
     *  Delete the "id" musixiserFollow.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the musixiserFollow corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<MusixiserFollow> search(String query, Pageable pageable);

}
