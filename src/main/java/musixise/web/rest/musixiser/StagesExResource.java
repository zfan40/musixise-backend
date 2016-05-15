package musixise.web.rest.musixiser;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Stages;
import musixise.repository.StagesRepository;
import musixise.repository.search.StagesSearchRepository;
import musixise.web.rest.StagesResource;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhaowei on 16/5/15.
 */

@RestController
@RequestMapping("/api/musixiser")
public class StagesExResource {


    private final Logger log = LoggerFactory.getLogger(StagesExResource.class);

    @Inject
    private StagesRepository stagesRepository;

    @Inject
    private StagesSearchRepository stagesSearchRepository;


    @RequestMapping(value = "/onStages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stages> onStages(@Valid @RequestBody Stages stages) throws URISyntaxException {
        log.debug("REST request to on Stages : {}", stages);
        if (stages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stages", "idexists", "A new stages cannot already have an ID")).body(null);
        }
        Stages result = stagesRepository.save(stages);
        stagesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stages", result.getId().toString()))
            .body(result);
    }


    @RequestMapping(value = "/offStages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> offStages(@PathVariable Long id) {
        log.debug("REST request to off Stages : {}", id);
        stagesRepository.delete(id);
        stagesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stages", id.toString())).build();
    }
}
