package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.WorkList;
import musixise.domain.WorkListFollow;
import musixise.repository.UserRepository;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.MusixiserService;
import musixise.service.WorkListService;
import musixise.service.impl.WorkListFollowServiceImpl;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.UpdateMyWorkDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.dto.favorite.UpdateMyWorkStatusDTO;
import musixise.web.rest.mapper.WorkListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Created by zhaowei on 16/11/12.
 */
@Api(value = "work", description = "作品接口", position = 1)
@RestController
@RequestMapping("/api/work")
public class WorkController {

    private final Logger log = LoggerFactory.getLogger(WorkController.class);

    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_CREATETIME = LocalDateTime.now();

    @Inject WorkListFollowServiceImpl workListFollowService;

    @Inject private UserRepository userRepository;

    @Inject private WorkListRepository workListRepository;

    @Inject private WorkListSearchRepository workListSearchRepository;

    @Inject private WorkListService workListService;

    @Inject private WorkListMapper workListMapper;

    @Inject MusixiserService musixiserService;

    @RequestMapping(value = "/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存作品信息", notes = "储存音乐人表演的作品信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> create(@Valid @RequestBody WorkList workList) throws URISyntaxException {
        log.debug("REST request to add WorkList  : {}", workList);

        workList.setId(null);

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                workList.setUserId(u.getId());
                if (workList.getStatus() == null) {
                    workList.setStatus(workList.getStatus());
                } else {
                    workList.setStatus(workList.getStatus());
                }
                WorkList result = workListRepository.save(workList);
                workListSearchRepository.save(result);
                //更新作品数量
                musixiserService.updateWorkCount(u.getId());

                return ResponseEntity.ok(new OutputDTO<>(0, "success", result));
            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));
    }


    @RequestMapping(value = "/getList",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我的作品列表", notes = "获取自己的演出作品列表", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> getList(Pageable pageable) {
        log.debug("REST request to get all getMyWorks");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                PageDTO<WorkListDTO> page = workListService.findAllByUserIdOrderByIdDesc(u.getId(), pageable);
                return ResponseEntity.ok(new OutputDTO<>(0, "success", page));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }

    @RequestMapping(value = "/getListByUid/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " 获取指定用户作品列表", notes = "获取指定用户作品列表", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> getListByUid(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get getListByUid : {}", id);

        PageDTO<WorkListDTO> page = workListService.findAllByUserIdOrderByIdDesc(id, pageable);

        return ResponseEntity.ok(new OutputDTO<>(0, "success", page));

    }

    @RequestMapping(value = "/updateStatus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新作品状态", notes = "更新作品状态", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateMyWorkStatusDTO updateMyWorkStatusDTO) {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                int update = workListRepository.updateStatusByUserIdAndWorkId(
                    updateMyWorkStatusDTO.getStatus(), updateMyWorkStatusDTO.getWorkId(), u.getId());

                return ResponseEntity.ok(new OutputDTO<>(0, "success"));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }

    @RequestMapping(value = "/detail/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取作品详细信息", notes = "获取作品详细信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.debug("REST request to get detail : {}", id);

        WorkListDTO workListDTO = workListService.findOne(id);

        //TODO: 私有作品只有自己查看
        if (workListDTO.getId() > 0) {

            Optional<WorkListFollow> workListFollow = workListFollowService.getFollowWorkInfo(id);
            if (workListFollow.isPresent()) {
                workListDTO.setFavStatus(1);
            } else {
                workListDTO.setFavStatus(0);
            }
            return ResponseEntity.ok(new OutputDTO<>(0, "success", workListDTO));
        } else {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_WORK_NOT_EXIST, "该作品不存在"));
        }

    }

    @RequestMapping(value = "/updateWork/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新作品信息", notes = "更新作品信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> updateWork(@PathVariable Long id, @Valid @RequestBody UpdateMyWorkDTO updateMyWorkDTO) {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                WorkListDTO oldWorkList = workListService.findOne(id);

                if (oldWorkList != null && oldWorkList.getUserId().equals(u.getId())) {

                    if (updateMyWorkDTO.getTitle() != null) {
                        oldWorkList.setTitle(updateMyWorkDTO.getTitle());
                    }

                    if (updateMyWorkDTO.getCover() != null) {

                        oldWorkList.setCover(updateMyWorkDTO.getCover());
                    }

                    if (updateMyWorkDTO.getContent() != null) {
                        oldWorkList.setContent(updateMyWorkDTO.getContent());

                    }
                    workListService.save(oldWorkList);
                    return ResponseEntity.ok(new OutputDTO<>(0, "success"));

                } else {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_WORK_NOT_EXIST, "该作品不存在"));
                }

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }

}
