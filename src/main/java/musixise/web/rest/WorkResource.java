package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.domain.WorkList;
import musixise.repository.UserRepository;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.WorkListService;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 16/11/12.
 */
@Api(value = "work", description = "作品接口", position = 1)
@RestController
@RequestMapping("/api/musixisers")
public class WorkResource {

    private final Logger log = LoggerFactory.getLogger(WorkResource.class);

    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_CREATETIME = LocalDateTime.now();

    @Inject
    private UserRepository userRepository;

    @Inject
    private WorkListRepository workListRepository;

    @Inject
    private WorkListSearchRepository workListSearchRepository;

    @Inject
    private WorkListService workListService;


    @RequestMapping(value = "/saveWork",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存作品信息", notes = "储存音乐人表演的作品信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> saveWork(@Valid @RequestBody WorkList workList) throws URISyntaxException {
        log.debug("REST request to save WorkList  : {}", workList);
        if (workList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workList", "idexists", "A new workList cannot already have an ID")).body(null);
        }

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                workList.setUserId(u.getId());
                if (workList.getStatus() == null) {
                    workList.setStatus(0);
                } else {
                    workList.setStatus(workList.getStatus());
                }
                WorkList result = workListRepository.save(workList);
                workListSearchRepository.save(result);
                return ResponseEntity.ok(new OutputDTO<>(0, "success", result));
            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));
    }


    @RequestMapping(value = "/getMyWorks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我的作品列表", notes = "获取自己的演出作品列表", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> getMyWorks(Pageable pageable) {
        log.debug("REST request to get all getMyWorks");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {
                List<WorkList> workLists = workListRepository.findAllByUserIdOrderByIdDesc(u.getId());

                return ResponseEntity.ok(new OutputDTO<>(0, "success", workLists));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));

    }

    @RequestMapping(value = "/getWorksById/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取作品详细信息", notes = "获取作品详细信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> getWorksById(@PathVariable Long id) {
        log.debug("REST request to get getWorksById : {}", id);

        WorkListDTO workListDTO = workListService.findOne(id);
        //TODO: 私有作品只有自己查看
        return Optional.ofNullable(workListDTO)
            .map(result -> ResponseEntity.ok(new OutputDTO<>(0, "success", workListDTO)))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
