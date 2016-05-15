package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.WorkList;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    private WorkListRepository workListRepository;
    
    @Inject
    private WorkListSearchRepository workListSearchRepository;
    
    /**
     * POST  /work-lists : Create a new workList.
     *
     * @param workList the workList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workList, or with status 400 (Bad Request) if the workList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkList> createWorkList(@Valid @RequestBody WorkList workList) throws URISyntaxException {
        log.debug("REST request to save WorkList : {}", workList);
        if (workList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workList", "idexists", "A new workList cannot already have an ID")).body(null);
        }
        WorkList result = workListRepository.save(workList);
        workListSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/work-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-lists : Updates an existing workList.
     *
     * @param workList the workList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workList,
     * or with status 400 (Bad Request) if the workList is not valid,
     * or with status 500 (Internal Server Error) if the workList couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkList> updateWorkList(@Valid @RequestBody WorkList workList) throws URISyntaxException {
        log.debug("REST request to update WorkList : {}", workList);
        if (workList.getId() == null) {
            return createWorkList(workList);
        }
        WorkList result = workListRepository.save(workList);
        workListSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workList", workList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-lists : get all the workLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workLists in body
     */
    @RequestMapping(value = "/work-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkList> getAllWorkLists() {
        log.debug("REST request to get all WorkLists");
        List<WorkList> workLists = workListRepository.findAll();
        return workLists;
    }

    /**
     * GET  /work-lists/:id : get the "id" workList.
     *
     * @param id the id of the workList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workList, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-lists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkList> getWorkList(@PathVariable Long id) {
        log.debug("REST request to get WorkList : {}", id);
        WorkList workList = workListRepository.findOne(id);
        return Optional.ofNullable(workList)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-lists/:id : delete the "id" workList.
     *
     * @param id the id of the workList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkList(@PathVariable Long id) {
        log.debug("REST request to delete WorkList : {}", id);
        workListRepository.delete(id);
        workListSearchRepository.delete(id);
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
    public List<WorkList> searchWorkLists(@RequestParam String query) {
        log.debug("REST request to search WorkLists for query {}", query);
        return StreamSupport
            .stream(workListSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
