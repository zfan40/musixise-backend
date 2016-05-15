package musixise.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Stages;
import musixise.repository.StagesRepository;
import musixise.repository.search.StagesSearchRepository;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Stages.
 */
@RestController
@RequestMapping("/api")
public class StagesResource {

    private final Logger log = LoggerFactory.getLogger(StagesResource.class);

    @Inject
    private StagesRepository stagesRepository;

    @Inject
    private StagesSearchRepository stagesSearchRepository;

    /**
     * POST  /stages : Create a new stages.
     *
     * @param stages the stages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stages, or with status 400 (Bad Request) if the stages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stages> createStages(@Valid @RequestBody Stages stages) throws URISyntaxException {
        log.debug("REST request to save Stages : {}", stages);
        if (stages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stages", "idexists", "A new stages cannot already have an ID")).body(null);
        }
        Stages result = stagesRepository.save(stages);
        stagesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stages : Updates an existing stages.
     *
     * @param stages the stages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stages,
     * or with status 400 (Bad Request) if the stages is not valid,
     * or with status 500 (Internal Server Error) if the stages couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stages> updateStages(@Valid @RequestBody Stages stages) throws URISyntaxException {
        log.debug("REST request to update Stages : {}", stages);
        if (stages.getId() == null) {
            return createStages(stages);
        }
        Stages result = stagesRepository.save(stages);
        stagesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stages", stages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stages : get all the stages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stages in body
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Stages> getAllStages() {
        log.debug("REST request to get all Stages");
        List<Stages> stages = stagesRepository.findAll();
        return stages;
    }

    /**
     * GET  /stages/:id : get the "id" stages.
     *
     * @param id the id of the stages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stages, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stages> getStages(@PathVariable Long id) {
        log.debug("REST request to get Stages : {}", id);
        Stages stages = stagesRepository.findOne(id);
        return Optional.ofNullable(stages)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stages/:id : delete the "id" stages.
     *
     * @param id the id of the stages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStages(@PathVariable Long id) {
        log.debug("REST request to delete Stages : {}", id);
        stagesRepository.delete(id);
        stagesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stages", id.toString())).build();
    }

    /**
     * SEARCH  /_search/stages?query=:query : search for the stages corresponding
     * to the query.
     *
     * @param query the query of the stages search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/stages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Stages> searchStages(@RequestParam String query) {
        log.debug("REST request to search Stages for query {}", query);
        return StreamSupport
            .stream(stagesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
