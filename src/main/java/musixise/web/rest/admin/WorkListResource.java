package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.WorkList;
import musixise.service.WorkListService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.mapper.WorkListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WorkList.
 */
@RestController
@RequestMapping("/api")
public class WorkListResource {

    private final Logger log = LoggerFactory.getLogger(WorkListResource.class);
        
    @Inject
    private WorkListService workListService;
    
    @Inject
    private WorkListMapper workListMapper;
    
    /**
     * POST  /work-lists : Create a new workList.
     *
     * @param workListDTO the workListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workListDTO, or with status 400 (Bad Request) if the workList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListDTO> createWorkList(@Valid @RequestBody WorkListDTO workListDTO) throws URISyntaxException {
        log.debug("REST request to save WorkList : {}", workListDTO);
        if (workListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workList", "idexists", "A new workList cannot already have an ID")).body(null);
        }
        WorkListDTO result = workListService.save(workListDTO);
        return ResponseEntity.created(new URI("/api/work-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-lists : Updates an existing workList.
     *
     * @param workListDTO the workListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workListDTO,
     * or with status 400 (Bad Request) if the workListDTO is not valid,
     * or with status 500 (Internal Server Error) if the workListDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListDTO> updateWorkList(@Valid @RequestBody WorkListDTO workListDTO) throws URISyntaxException {
        log.debug("REST request to update WorkList : {}", workListDTO);
        if (workListDTO.getId() == null) {
            return createWorkList(workListDTO);
        }
        WorkListDTO result = workListService.save(workListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workList", workListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-lists : get all the workLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WorkListDTO>> getAllWorkLists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkLists");
        Page<WorkList> page = workListService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-lists");
        return new ResponseEntity<>(workListMapper.workListsToWorkListDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-lists/:id : get the "id" workList.
     *
     * @param id the id of the workListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workListDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-lists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListDTO> getWorkList(@PathVariable Long id) {
        log.debug("REST request to get WorkList : {}", id);
        WorkListDTO workListDTO = workListService.findOne(id);
        return Optional.ofNullable(workListDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-lists/:id : delete the "id" workList.
     *
     * @param id the id of the workListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkList(@PathVariable Long id) {
        log.debug("REST request to delete WorkList : {}", id);
        workListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workList", id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-lists?query=:query : search for the workList corresponding
     * to the query.
     *
     * @param query the query of the workList search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/work-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WorkListDTO>> searchWorkLists(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkLists for query {}", query);
        Page<WorkList> page = workListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-lists");
        return new ResponseEntity<>(workListMapper.workListsToWorkListDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
