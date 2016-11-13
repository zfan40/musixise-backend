package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.domain.WorkListFollow;
import musixise.repository.UserRepository;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.search.WorkListFollowSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.WorkListService;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.dto.request.AddToMyfavoriteWorksDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 16/11/12.
 */

@Api(value = "favorite", description = "收藏接口", position = 1)
@RestController
@RequestMapping("/api/musixisers")
public class FavoriteResource {

    private final Logger log = LoggerFactory.getLogger(WorkResource.class);

    @Inject private WorkListFollowRepository workListFollowRepository;

    @Inject private UserRepository userRepository;

    @Inject private WorkListFollowSearchRepository workListFollowSearchRepository;

    @Inject private WorkListService workListService;

    @RequestMapping(value = "/addToMyFavoriteWorks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加到我的收藏", notes = "", response = WorkListFollow.class, position = 2)
    @Timed
    public ResponseEntity<?> addToMyFavoriteWorks(@Valid @RequestBody AddToMyfavoriteWorksDTO addToMyfavoriteWorksDTO) throws URISyntaxException {
        log.debug("REST request to save addToMyFavoriteWorks : {}", addToMyfavoriteWorksDTO);

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {
                //检查收藏的作品是否存在

                WorkListDTO workListDTO = workListService.findOne(addToMyfavoriteWorksDTO.getWorkId());

                if (workListDTO == null || workListDTO.getId() == null) {

                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_FAVORITE_WORK_NOT_EXIST, "该作品不存在或已删除"));
                }


                //检查是否存在
                if (workListFollowRepository.findOneByUserIdAndWorkId(u.getId(), addToMyfavoriteWorksDTO.getWorkId()).isPresent()) {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_FAVORITE_ALREADY_ADD, "已收藏过"));
                }

                WorkListFollow workListFollow = new WorkListFollow();
                User user = new User();
                user.setId(u.getId());
                workListFollow.setUser(user);
                WorkList workList = new WorkList();
                workList.setId(addToMyfavoriteWorksDTO.getWorkId());
                workListFollow.setWork(workList);
                workListFollow.setId(0l);

                WorkListFollow result = workListFollowRepository.save(workListFollow);

                workListFollowSearchRepository.save(result);
                //
                return ResponseEntity.ok(new OutputDTO<>(0, "success", result));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));

    }


    @RequestMapping(value = "/delMyFavoriteWorks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除我的收藏", notes = "", response = Integer.class, position = 2)
    @Timed
    public ResponseEntity<?> delMyFavoriteWork(@PathVariable Long id) {
        log.debug("REST request to delete delMyFavoriteWork: {}", id);

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

               int del = workListFollowRepository.deleteByUserIdAndWorkId(u.getId(), id);

                if (del > 1) {
                    return ResponseEntity.ok(new OutputDTO<>(0, "success"));
                } else {
                    return ResponseEntity.ok(new OutputDTO<>(20000, "del fail"));
                }

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));

    }

    @RequestMapping(value = "/getMyFavoriteWorks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我的收藏列表", notes = "", response = WorkListFollow.class, position = 3)
    @Timed
    public ResponseEntity<?> getMyFavoriteWorks() {
        log.debug("REST request to get all getMyFavoriteWorks");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                List<WorkListFollow> workListFollows = workListFollowRepository.findAllByUserIdOrderByIdDesc(u.getId());
                //
                return ResponseEntity.ok(new OutputDTO<>(0, "success", workListFollows));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));
    }


}
