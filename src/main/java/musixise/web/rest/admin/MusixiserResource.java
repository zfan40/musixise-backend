package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Musixiser;
import musixise.service.MusixiserService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.MusixiserDTO;
import musixise.web.rest.mapper.MusixiserMapper;
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
 * REST controller for managing Musixiser.
 */
@RestController
@RequestMapping("/api")
public class MusixiserResource {

    private final Logger log = LoggerFactory.getLogger(MusixiserResource.class);
        
    @Inject
    private MusixiserService musixiserService;
    
    @Inject
    private MusixiserMapper musixiserMapper;
    
    /**
     * POST  /musixisers : Create a new musixiser.
     *
     * @param musixiserDTO the musixiserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new musixiserDTO, or with status 400 (Bad Request) if the musixiser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserDTO> createMusixiser(@Valid @RequestBody MusixiserDTO musixiserDTO) throws URISyntaxException {
        log.debug("REST request to save Musixiser : {}", musixiserDTO);
        if (musixiserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("musixiser", "idexists", "A new musixiser cannot already have an ID")).body(null);
        }
        MusixiserDTO result = musixiserService.save(musixiserDTO);
        return ResponseEntity.created(new URI("/api/musixisers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("musixiser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musixisers : Updates an existing musixiser.
     *
     * @param musixiserDTO the musixiserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated musixiserDTO,
     * or with status 400 (Bad Request) if the musixiserDTO is not valid,
     * or with status 500 (Internal Server Error) if the musixiserDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserDTO> updateMusixiser(@Valid @RequestBody MusixiserDTO musixiserDTO) throws URISyntaxException {
        log.debug("REST request to update Musixiser : {}", musixiserDTO);
        if (musixiserDTO.getId() == null) {
            return createMusixiser(musixiserDTO);
        }
        MusixiserDTO result = musixiserService.save(musixiserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("musixiser", musixiserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musixisers : get all the musixisers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of musixisers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MusixiserDTO>> getAllMusixisers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Musixisers");
        Page<Musixiser> page = musixiserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/musixisers");
        return new ResponseEntity<>(musixiserMapper.musixisersToMusixiserDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /musixisers/:id : get the "id" musixiser.
     *
     * @param id the id of the musixiserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the musixiserDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/musixisers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserDTO> getMusixiser(@PathVariable Long id) {
        log.debug("REST request to get Musixiser : {}", id);
        MusixiserDTO musixiserDTO = musixiserService.findOne(id);
        return Optional.ofNullable(musixiserDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /musixisers/:id : delete the "id" musixiser.
     *
     * @param id the id of the musixiserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/musixisers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMusixiser(@PathVariable Long id) {
        log.debug("REST request to delete Musixiser : {}", id);
        musixiserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("musixiser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/musixisers?query=:query : search for the musixiser corresponding
     * to the query.
     *
     * @param query the query of the musixiser search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/musixisers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MusixiserDTO>> searchMusixisers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Musixisers for query {}", query);
        Page<Musixiser> page = musixiserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/musixisers");
        return new ResponseEntity<>(musixiserMapper.musixisersToMusixiserDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
