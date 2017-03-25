package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.domain.WorkListFollow;
import musixise.repository.UserRepository;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.search.WorkListFollowSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.WorkListService;
import musixise.service.impl.WorkListFollowServiceImpl;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.dto.favorite.AddToMyfavoriteWorksDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * Created by zhaowei on 16/11/12.
 */

@Api(value = "favorite", description = "收藏作品", position = 1)
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final Logger log = LoggerFactory.getLogger(FavoriteController.class);

    @Inject private WorkListFollowRepository workListFollowRepository;

    @Inject private UserRepository userRepository;

    @Inject private WorkListFollowSearchRepository workListFollowSearchRepository;

    @Inject private WorkListService workListService;

    @Inject  WorkListFollowServiceImpl workListFollowService;

    @RequestMapping(value = "/addWork",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加作品到我的收藏", notes = "", response = WorkListFollow.class, position = 2)
    @Timed
    public ResponseEntity<?> addWork(@Valid @RequestBody AddToMyfavoriteWorksDTO addToMyfavoriteWorksDTO) throws URISyntaxException {
        log.debug("REST request to save addToMyFavoriteWorks : {}", addToMyfavoriteWorksDTO);

        Long id = addToMyfavoriteWorksDTO.getWorkId();
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {
                //检查收藏的作品是否存在

                WorkListDTO workListDTO = workListService.findOne(id);

                if (workListDTO == null || workListDTO.getId() == null) {

                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_FAVORITE_WORK_NOT_EXIST, "该作品不存在或已删除"));
                }


                if (addToMyfavoriteWorksDTO.getStatus() == 0) {//添加收藏
                    //检查是否存在
                    if (workListFollowRepository.findOneByUserIdAndWorkId(u.getId(), addToMyfavoriteWorksDTO.getWorkId()).isPresent()) {
                        return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_FAVORITE_ALREADY_ADD, "已收藏过"));
                    }

                    WorkListFollow workListFollow = new WorkListFollow();
                    User user = new User();
                    user.setId(u.getId());
                    workListFollow.setUser(user);
                    WorkList workList = new WorkList();
                    workList.setId(id);
                    workListFollow.setWork(workList);
                    workListFollow.setId(0l);

                    WorkListFollow result = workListFollowRepository.save(workListFollow);

                    workListFollowSearchRepository.save(result);
                } else if (addToMyfavoriteWorksDTO.getStatus() == 1) {//取消收藏
                    workListFollowRepository.deleteByUserIdAndWorkId(u.getId(), addToMyfavoriteWorksDTO.getWorkId());

                } else {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, "参数错误"));
                }

                //更新收藏数
                workListFollowService.updateFavoriteCount(id);

                return ResponseEntity.ok(new OutputDTO<>(0, "success"));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }


    @RequestMapping(value = "/getMyFavoriteWorks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我的作品收藏列表", notes = "", response = WorkListFollow.class, position = 3)
    @Timed
    public ResponseEntity<?> getWorkList(Pageable pageable) {
        log.debug("REST request to get all getMyFavoriteWorks");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                PageDTO<WorkListDTO> page = workListService.findAllByUserIdOrderByIdDesc(u.getId(), pageable);

                return ResponseEntity.ok(new OutputDTO<>(0, "success", page));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));
    }



}
