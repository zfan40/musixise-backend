package musixise.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.MusixiserFollow;
import musixise.service.MusixiserFollowService;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.mapper.MusixiserFollowMapper;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MusixiserFollow.
 */
@RestController
@RequestMapping("/api")
public class MusixiserFollowResource {

    private final Logger log = LoggerFactory.getLogger(MusixiserFollowResource.class);

    @Inject
    private MusixiserFollowService musixiserFollowService;

    @Inject
    private MusixiserFollowMapper musixiserFollowMapper;

    /**
     * POST  /musixiser-follows : Create a new musixiserFollow.
     *
     * @param musixiserFollowDTO the musixiserFollowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new musixiserFollowDTO, or with status 400 (Bad Request) if the musixiserFollow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixiser-follows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserFollowDTO> createMusixiserFollow(@Valid @RequestBody MusixiserFollowDTO musixiserFollowDTO) throws URISyntaxException {
        log.debug("REST request to save MusixiserFollow : {}", musixiserFollowDTO);
        if (musixiserFollowDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("musixiserFollow", "idexists", "A new musixiserFollow cannot already have an ID")).body(null);
        }
        MusixiserFollowDTO result = musixiserFollowService.save(musixiserFollowDTO);
        return ResponseEntity.created(new URI("/api/musixiser-follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("musixiserFollow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musixiser-follows : Updates an existing musixiserFollow.
     *
     * @param musixiserFollowDTO the musixiserFollowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated musixiserFollowDTO,
     * or with status 400 (Bad Request) if the musixiserFollowDTO is not valid,
     * or with status 500 (Internal Server Error) if the musixiserFollowDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixiser-follows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserFollowDTO> updateMusixiserFollow(@Valid @RequestBody MusixiserFollowDTO musixiserFollowDTO) throws URISyntaxException {
        log.debug("REST request to update MusixiserFollow : {}", musixiserFollowDTO);
        if (musixiserFollowDTO.getId() == null) {
            return createMusixiserFollow(musixiserFollowDTO);
        }
        MusixiserFollowDTO result = musixiserFollowService.save(musixiserFollowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("musixiserFollow", musixiserFollowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musixiser-follows : get all the musixiserFollows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of musixiserFollows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/musixiser-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MusixiserFollowDTO>> getAllMusixiserFollows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MusixiserFollows");
        Page<MusixiserFollow> page = musixiserFollowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/musixiser-follows");
        return new ResponseEntity<>(musixiserFollowMapper.musixiserFollowsToMusixiserFollowDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /musixiser-follows/:id : get the "id" musixiserFollow.
     *
     * @param id the id of the musixiserFollowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the musixiserFollowDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/musixiser-follows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MusixiserFollowDTO> getMusixiserFollow(@PathVariable Long id) {
        log.debug("REST request to get MusixiserFollow : {}", id);
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowService.findOne(id);
        return Optional.ofNullable(musixiserFollowDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /musixiser-follows/:id : delete the "id" musixiserFollow.
     *
     * @param id the id of the musixiserFollowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/musixiser-follows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMusixiserFollow(@PathVariable Long id) {
        log.debug("REST request to delete MusixiserFollow : {}", id);
        musixiserFollowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("musixiserFollow", id.toString())).build();
    }

    /**
     * SEARCH  /_search/musixiser-follows?query=:query : search for the musixiserFollow corresponding
     * to the query.
     *
     * @param query the query of the musixiserFollow search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/musixiser-follows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MusixiserFollowDTO>> searchMusixiserFollows(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MusixiserFollows for query {}", query);
        Page<MusixiserFollow> page = musixiserFollowService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/musixiser-follows");
        return new ResponseEntity<>(musixiserFollowMapper.musixiserFollowsToMusixiserFollowDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
