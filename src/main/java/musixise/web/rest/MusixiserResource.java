package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.repository.MusixiserRepository;
import musixise.repository.UserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.UserService;
import musixise.web.rest.dto.ManagedUserDTO;
import musixise.web.rest.dto.MusixiseDTO;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;


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
    public ResponseEntity<Musixiser> createMusixiser(@Valid @RequestBody Musixiser musixiser) throws URISyntaxException {
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
    public ResponseEntity<Musixiser> updateMusixiser(@Valid @RequestBody Musixiser musixiser) throws URISyntaxException {
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


    @RequestMapping(value = "/musixisers/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> registerMusixiser(@RequestBody MusixiseDTO musixiseDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to register Musixiser : {}", musixiseDTO);
        if (musixiseDTO.getId() != null || musixiseDTO.getUserId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("musixiser", "idexists", "A new musixiser cannot already have an ID")).body(null);
        }

        //注册账号
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(musixiseDTO.getNickname());
        managedUserDTO.setPassword(musixiseDTO.getPassword());
        managedUserDTO.setEmail(musixiseDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "username already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(musixiseDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserDTO);
            //保存个人信息
            Musixiser musixiser = new Musixiser();

            musixiser.setUserId(newUser.getId());
            musixiser.setRealname(musixiseDTO.getRealname());
            musixiser.setTel(musixiseDTO.getTel());
            musixiser.setEmail(musixiseDTO.getEmail());
            musixiser.setBirth(musixiseDTO.getBirth());
            musixiser.setGender(musixiseDTO.getGender());
            musixiser.setSmallAvatar(musixiseDTO.getSmallAvatar());
            musixiser.setLargeAvatar(musixiseDTO.getLargeAvatar());
            musixiser.setNation(musixiseDTO.getNation());

            Musixiser result = musixiserRepository.save(musixiser);

            //搜索索引
            musixiserSearchRepository.save(result);

            return ResponseEntity.created(new URI("/api/musixisers/" + newUser.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("musixiser", result.getId().toString()))
                .body(result);
        }

    }


    @RequestMapping(value = "/musixisers/getInfo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Musixiser> getMusixiserInfo() {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                Musixiser musixiser = musixiserRepository.findOneByUserId(u.getId());
                return new ResponseEntity<>(musixiser, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
