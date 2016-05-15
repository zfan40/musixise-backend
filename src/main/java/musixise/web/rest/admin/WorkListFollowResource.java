package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.WorkListFollow;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.search.WorkListFollowSearchRepository;
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
 * REST controller for managing WorkListFollow.
 */
@RestController
@RequestMapping("/api")
public class WorkListFollowResource {

    private final Logger log = LoggerFactory.getLogger(WorkListFollowResource.class);
        
    @Inject
    private WorkListFollowRepository workListFollowRepository;
    
    @Inject
    private WorkListFollowSearchRepository workListFollowSearchRepository;
    
    /**
     * POST  /work-list-follows : Create a new workListFollow.
     *
     * @param workListFollow the workListFollow to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workListFollow, or with status 400 (Bad Request) if the workListFollow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-list-follows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListFollow> createWorkListFollow(@Valid @RequestBody WorkListFollow workListFollow) throws URISyntaxException {
        log.debug("REST request to save WorkListFollow : {}", workListFollow);
        if (workListFollow.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workListFollow", "idexists", "A new workListFollow cannot already have an ID")).body(null);
        }
        WorkListFollow result = workListFollowRepository.save(workListFollow);
        workListFollowSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/work-list-follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workListFollow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-list-follows : Updates an existing workListFollow.
     *
     * @param workListFollow the workListFollow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workListFollow,
     * or with status 400 (Bad Request) if the workListFollow is not valid,
     * or with status 500 (Internal Server Error) if the workListFollow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-list-follows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListFollow> updateWorkListFollow(@Valid @RequestBody WorkListFollow workListFollow) throws URISyntaxException {
        log.debug("REST request to update WorkListFollow : {}", workListFollow);
        if (workListFollow.getId() == null) {
            return createWorkListFollow(workListFollow);
        }
        WorkListFollow result = workListFollowRepository.save(workListFollow);
        workListFollowSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workListFollow", workListFollow.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-list-follows : get all the workListFollows.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workListFollows in body
     */
    @RequestMapping(value = "/work-list-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkListFollow> getAllWorkListFollows() {
        log.debug("REST request to get all WorkListFollows");
        List<WorkListFollow> workListFollows = workListFollowRepository.findAll();
        return workListFollows;
    }

    /**
     * GET  /work-list-follows/:id : get the "id" workListFollow.
     *
     * @param id the id of the workListFollow to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workListFollow, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-list-follows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkListFollow> getWorkListFollow(@PathVariable Long id) {
        log.debug("REST request to get WorkListFollow : {}", id);
        WorkListFollow workListFollow = workListFollowRepository.findOne(id);
        return Optional.ofNullable(workListFollow)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-list-follows/:id : delete the "id" workListFollow.
     *
     * @param id the id of the workListFollow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-list-follows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkListFollow(@PathVariable Long id) {
        log.debug("REST request to delete WorkListFollow : {}", id);
        workListFollowRepository.delete(id);
        workListFollowSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workListFollow", id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-list-follows?query=:query : search for the workListFollow corresponding
     * to the query.
     *
     * @param query the query of the workListFollow search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/work-list-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkListFollow> searchWorkListFollows(@RequestParam String query) {
        log.debug("REST request to search WorkListFollows for query {}", query);
        return StreamSupport
            .stream(workListFollowSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
