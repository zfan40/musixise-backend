package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Stages;
import musixise.service.StagesService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.StagesDTO;
import musixise.web.rest.mapper.StagesMapper;
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
 * REST controller for managing Stages.
 */
@RestController
@RequestMapping("/api")
public class StagesResource {

    private final Logger log = LoggerFactory.getLogger(StagesResource.class);
        
    @Inject
    private StagesService stagesService;
    
    @Inject
    private StagesMapper stagesMapper;
    
    /**
     * POST  /stages : Create a new stages.
     *
     * @param stagesDTO the stagesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stagesDTO, or with status 400 (Bad Request) if the stages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesDTO> createStages(@Valid @RequestBody StagesDTO stagesDTO) throws URISyntaxException {
        log.debug("REST request to save Stages : {}", stagesDTO);
        if (stagesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stages", "idexists", "A new stages cannot already have an ID")).body(null);
        }
        StagesDTO result = stagesService.save(stagesDTO);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stages : Updates an existing stages.
     *
     * @param stagesDTO the stagesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stagesDTO,
     * or with status 400 (Bad Request) if the stagesDTO is not valid,
     * or with status 500 (Internal Server Error) if the stagesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesDTO> updateStages(@Valid @RequestBody StagesDTO stagesDTO) throws URISyntaxException {
        log.debug("REST request to update Stages : {}", stagesDTO);
        if (stagesDTO.getId() == null) {
            return createStages(stagesDTO);
        }
        StagesDTO result = stagesService.save(stagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stages", stagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stages : get all the stages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StagesDTO>> getAllStages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stages");
        Page<Stages> page = stagesService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stages");
        return new ResponseEntity<>(stagesMapper.stagesToStagesDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /stages/:id : get the "id" stages.
     *
     * @param id the id of the stagesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stagesDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StagesDTO> getStages(@PathVariable Long id) {
        log.debug("REST request to get Stages : {}", id);
        StagesDTO stagesDTO = stagesService.findOne(id);
        return Optional.ofNullable(stagesDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stages/:id : delete the "id" stages.
     *
     * @param id the id of the stagesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStages(@PathVariable Long id) {
        log.debug("REST request to delete Stages : {}", id);
        stagesService.delete(id);
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
    @Transactional(readOnly = true)
    public ResponseEntity<List<StagesDTO>> searchStages(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Stages for query {}", query);
        Page<Stages> page = stagesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stages");
        return new ResponseEntity<>(stagesMapper.stagesToStagesDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
