package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.MusixiserFollow;
import musixise.repository.MusixiserFollowRepository;
import musixise.repository.UserRepository;
import musixise.security.SecurityUtils;
import musixise.service.MusixiserFollowService;
import musixise.service.MusixiserService;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.MusixiserFollowerDTO;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.dto.follow.AddMyFollowDTO;
import musixise.web.rest.mapper.MusixiserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * Created by zhaowei on 16/11/19.
 */
@Api(value = "follow", description = "关注接口", position = 1)
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final Logger log = LoggerFactory.getLogger(FollowController.class);

    @Inject private UserRepository userRepository;

    @Inject private MusixiserFollowService musixiserFollowService;

    @Inject private MusixiserFollowRepository musixiserFollowRepository;

    @Inject private MusixiserMapper musixiserMapper;

    @Inject MusixiserService musixiserService;

    @RequestMapping(value = "/getList",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我关注用户列表", notes = "", response = MusixiserFollow.class, position = 3)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllMusixisers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get my follow list");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {
                PageDTO<MusixiserFollowDTO> page = musixiserFollowService.findFollowingByUserId(pageable, u.getId());
                return ResponseEntity.ok(new OutputDTO<>(0, "success", page));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));
    }

    @RequestMapping(value = "/add",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "关注用户", notes = "", response = OutputDTO.class, position = 3)
    @Timed
    public ResponseEntity<?> add(@Valid @RequestBody AddMyFollowDTO addMyFollowDTO) throws URISyntaxException {
        log.debug("REST request to save add : {}", addMyFollowDTO);

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                try {

                    if (addMyFollowDTO.getStatus() == 0) {
                        //检查是否存在
                        if (musixiserFollowRepository.findOneByUserIdAndFollowId(u.getId(), addMyFollowDTO.getFollowId()).isPresent()) {
                            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_USER_ALREADY_FOLLOW, "已关注过"));
                        }

                        MusixiserFollowDTO musixiserFollowDTO = new MusixiserFollowDTO();
                        musixiserFollowDTO.setUserId(addMyFollowDTO.getFollowId());

                        MusixiserFollowDTO result = musixiserFollowService.save(musixiserFollowDTO);
                    } else if (addMyFollowDTO.getStatus() == 1) {

                        musixiserFollowRepository.deleteByUserIdAndFollowUid(u.getId(), addMyFollowDTO.getFollowId());

                    } else {
                        return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, "参数错误"));
                    }
                    //update follow num
                    musixiserService.updateFollowCount(u.getId(), addMyFollowDTO.getFollowId());
                    return ResponseEntity.ok(new OutputDTO<>(0, "success"));
                } catch (Exception e) {

                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_APPLICATION, "应用程序错误"));
                }


            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }

    @RequestMapping(value = "/followings/{uid}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取用户的关注列表", notes = "", response = MusixiserFollow.class, position = 3)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowings(Pageable pageable, @PathVariable Long uid)
        throws URISyntaxException {
        log.debug("REST request to get followings with give uid");

        if (uid > 0) {
            PageDTO<MusixiserFollowDTO> page = musixiserFollowService.findFollowingByUserId(pageable, uid);
            return ResponseEntity.ok(new OutputDTO<>(0, "success", page));
        } else {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, "参数错误"));
        }
    }

    @RequestMapping(value = "/followers/{uid}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取用户的粉丝列表", notes = "", response = MusixiserFollow.class, position = 3)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowers(Pageable pageable, @PathVariable Long uid)
        throws URISyntaxException {
        log.debug("REST request to get followers with give uid");

        if (uid > 0) {
            PageDTO<MusixiserFollowerDTO> page = musixiserFollowService.findFollowerByUserId(pageable, uid);
            return ResponseEntity.ok(new OutputDTO<>(0, "success", page));
        } else {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, "参数错误"));
        }
    }
}
