package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Audience;
import musixise.service.AudienceService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.AudienceDTO;
import musixise.web.rest.mapper.AudienceMapper;
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
 * REST controller for managing Audience.
 */
@RestController
@RequestMapping("/api")
public class AudienceResource {

    private final Logger log = LoggerFactory.getLogger(AudienceResource.class);
        
    @Inject
    private AudienceService audienceService;
    
    @Inject
    private AudienceMapper audienceMapper;
    
    /**
     * POST  /audiences : Create a new audience.
     *
     * @param audienceDTO the audienceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new audienceDTO, or with status 400 (Bad Request) if the audience has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audiences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AudienceDTO> createAudience(@RequestBody AudienceDTO audienceDTO) throws URISyntaxException {
        log.debug("REST request to save Audience : {}", audienceDTO);
        if (audienceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("audience", "idexists", "A new audience cannot already have an ID")).body(null);
        }
        AudienceDTO result = audienceService.save(audienceDTO);
        return ResponseEntity.created(new URI("/api/audiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("audience", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audiences : Updates an existing audience.
     *
     * @param audienceDTO the audienceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated audienceDTO,
     * or with status 400 (Bad Request) if the audienceDTO is not valid,
     * or with status 500 (Internal Server Error) if the audienceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audiences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AudienceDTO> updateAudience(@RequestBody AudienceDTO audienceDTO) throws URISyntaxException {
        log.debug("REST request to update Audience : {}", audienceDTO);
        if (audienceDTO.getId() == null) {
            return createAudience(audienceDTO);
        }
        AudienceDTO result = audienceService.save(audienceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("audience", audienceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audiences : get all the audiences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of audiences in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/audiences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AudienceDTO>> getAllAudiences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Audiences");
        Page<Audience> page = audienceService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audiences");
        return new ResponseEntity<>(audienceMapper.audiencesToAudienceDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /audiences/:id : get the "id" audience.
     *
     * @param id the id of the audienceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the audienceDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/audiences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AudienceDTO> getAudience(@PathVariable Long id) {
        log.debug("REST request to get Audience : {}", id);
        AudienceDTO audienceDTO = audienceService.findOne(id);
        return Optional.ofNullable(audienceDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audiences/:id : delete the "id" audience.
     *
     * @param id the id of the audienceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/audiences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAudience(@PathVariable Long id) {
        log.debug("REST request to delete Audience : {}", id);
        audienceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("audience", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audiences?query=:query : search for the audience corresponding
     * to the query.
     *
     * @param query the query of the audience search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/audiences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AudienceDTO>> searchAudiences(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Audiences for query {}", query);
        Page<Audience> page = audienceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/audiences");
        return new ResponseEntity<>(audienceMapper.audiencesToAudienceDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
