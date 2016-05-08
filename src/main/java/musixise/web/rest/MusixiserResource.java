package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Musixiser;
import musixise.repository.MusixiserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
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
    private MusixiserRepository musixiserRepository;
    
    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;
    
    /**
     * POST  /musixisers : Create a new musixiser.
     *
     * @param musixiser the musixiser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new musixiser, or with status 400 (Bad Request) if the musixiser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Musixiser> createMusixiser(@RequestBody Musixiser musixiser) throws URISyntaxException {
        log.debug("REST request to save Musixiser : {}", musixiser);
        if (musixiser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("musixiser", "idexists", "A new musixiser cannot already have an ID")).body(null);
        }
        Musixiser result = musixiserRepository.save(musixiser);
        musixiserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/musixisers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("musixiser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musixisers : Updates an existing musixiser.
     *
     * @param musixiser the musixiser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated musixiser,
     * or with status 400 (Bad Request) if the musixiser is not valid,
     * or with status 500 (Internal Server Error) if the musixiser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Musixiser> updateMusixiser(@RequestBody Musixiser musixiser) throws URISyntaxException {
        log.debug("REST request to update Musixiser : {}", musixiser);
        if (musixiser.getId() == null) {
            return createMusixiser(musixiser);
        }
        Musixiser result = musixiserRepository.save(musixiser);
        musixiserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("musixiser", musixiser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musixisers : get all the musixisers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of musixisers in body
     */
    @RequestMapping(value = "/musixisers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Musixiser> getAllMusixisers() {
        log.debug("REST request to get all Musixisers");
        List<Musixiser> musixisers = musixiserRepository.findAll();
        return musixisers;
    }

    /**
     * GET  /musixisers/:id : get the "id" musixiser.
     *
     * @param id the id of the musixiser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the musixiser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/musixisers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Musixiser> getMusixiser(@PathVariable Long id) {
        log.debug("REST request to get Musixiser : {}", id);
        Musixiser musixiser = musixiserRepository.findOne(id);
        return Optional.ofNullable(musixiser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /musixisers/:id : delete the "id" musixiser.
     *
     * @param id the id of the musixiser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/musixisers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMusixiser(@PathVariable Long id) {
        log.debug("REST request to delete Musixiser : {}", id);
        musixiserRepository.delete(id);
        musixiserSearchRepository.delete(id);
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
    public List<Musixiser> searchMusixisers(@RequestParam String query) {
        log.debug("REST request to search Musixisers for query {}", query);
        return StreamSupport
            .stream(musixiserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
