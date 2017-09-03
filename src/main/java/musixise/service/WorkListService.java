package musixise.service;

import musixise.domain.WorkList;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.WorkListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing WorkList.
 */
public interface WorkListService {

    /**
     * Save a workList.
     *
     * @param workListDTO the entity to save
     * @return the persisted entity
     */
    WorkListDTO save(WorkListDTO workListDTO);

    /**
     *  Get all the workLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkList> findAll(Pageable pageable);

    /**
     *  Get the "id" workList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkListDTO findOne(Long id);

    /**
     *  Delete the "id" workList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the workList corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<WorkList> search(String query, Pageable pageable);

    public PageDTO<WorkListDTO> findAllByUserIdOrderByIdDesc(Long uid, Pageable pageable);

    List<WorkListDTO> getHotSongs(Long userId);

    List<WorkListDTO> getLatestSongs(Long userId);
}
