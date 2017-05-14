package musixise.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Config;
import musixise.repository.ConfigRepository;
import musixise.web.rest.util.HeaderUtil;
import musixise.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by zhaowei on 17/5/14.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigResource {

    @Resource ConfigRepository configRepository;

    @RequestMapping(value = "/config-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<Config>> getAll(Pageable pageable) throws URISyntaxException {

        Page<Config> all = configRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(all, "/api/config-lists");
        return new ResponseEntity<>(all.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/config-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Config> create(@Valid @RequestBody Config config) throws URISyntaxException {

        Config result = configRepository.save(config);
        return ResponseEntity.created(new URI("/api/config-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("config", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/config-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Config> update(@Valid @RequestBody Config config) throws URISyntaxException {

        Config result = configRepository.save(config);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("config", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/config-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("config", id.toString())).build();
    }
}
