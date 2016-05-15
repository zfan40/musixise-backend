package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.StagesFollow;
import musixise.service.StagesFollowService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.StagesFollowDTO;
import musixise.web.rest.mapper.StagesFollowMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StagesFollow.
 */
@RestController
@RequestMapping("/api")
public class StagesFollowResource {

    private final Logger log = LoggerFactory.getLogger(StagesFollowResource.class);
        
    @Inject
    private StagesFollowService stagesFollowService;
    
    @Inject
    private StagesFollowMapper stagesFollowMapper;
    
    /**
     * POST  /stages-follows : Create a new stagesFollow.
     *
     * @param stagesFollowDTO the stagesFollowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stagesFollowDTO, or with status 400 (Bad Request) if the stagesFollow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages-follows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesFollowDTO> createStagesFollow(@RequestBody StagesFollowDTO stagesFollowDTO) throws URISyntaxException {
        log.debug("REST request to save StagesFollow : {}", stagesFollowDTO);
        if (stagesFollowDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stagesFollow", "idexists", "A new stagesFollow cannot already have an ID")).body(null);
        }
        StagesFollowDTO result = stagesFollowService.save(stagesFollowDTO);
        return ResponseEntity.created(new URI("/api/stages-follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stagesFollow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stages-follows : Updates an existing stagesFollow.
     *
     * @param stagesFollowDTO the stagesFollowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stagesFollowDTO,
     * or with status 400 (Bad Request) if the stagesFollowDTO is not valid,
     * or with status 500 (Internal Server Error) if the stagesFollowDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages-follows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesFollowDTO> updateStagesFollow(@RequestBody StagesFollowDTO stagesFollowDTO) throws URISyntaxException {
        log.debug("REST request to update StagesFollow : {}", stagesFollowDTO);
        if (stagesFollowDTO.getId() == null) {
            return createStagesFollow(stagesFollowDTO);
        }
        StagesFollowDTO result = stagesFollowService.save(stagesFollowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stagesFollow", stagesFollowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stages-follows : get all the stagesFollows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stagesFollows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stages-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StagesFollowDTO>> getAllStagesFollows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StagesFollows");
        Page<StagesFollow> page = stagesFollowService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stages-follows");
        return new ResponseEntity<>(stagesFollowMapper.stagesFollowsToStagesFollowDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /stages-follows/:id : get the "id" stagesFollow.
     *
     * @param id the id of the stagesFollowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stagesFollowDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stages-follows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesFollowDTO> getStagesFollow(@PathVariable Long id) {
        log.debug("REST request to get StagesFollow : {}", id);
        StagesFollowDTO stagesFollowDTO = stagesFollowService.findOne(id);
        return Optional.ofNullable(stagesFollowDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stages-follows/:id : delete the "id" stagesFollow.
     *
     * @param id the id of the stagesFollowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stages-follows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStagesFollow(@PathVariable Long id) {
        log.debug("REST request to delete StagesFollow : {}", id);
        stagesFollowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stagesFollow", id.toString())).build();
    }

    /**
     * SEARCH  /_search/stages-follows?query=:query : search for the stagesFollow corresponding
     * to the query.
     *
     * @param query the query of the stagesFollow search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/stages-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StagesFollowDTO>> searchStagesFollows(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of StagesFollows for query {}", query);
        Page<StagesFollow> page = stagesFollowService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stages-follows");
        return new ResponseEntity<>(stagesFollowMapper.stagesFollowsToStagesFollowDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
