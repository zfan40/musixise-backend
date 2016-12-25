package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.MusixiserFollow;
import musixise.domain.User;
import musixise.repository.MusixiserFollowRepository;
import musixise.repository.UserRepository;
import musixise.security.SecurityUtils;
import musixise.service.MusixiserFollowService;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.follow.AddMyFollowDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/getList",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我关注用户列表", notes = "", response = MusixiserFollow.class, position = 3)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllMusixisers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Musixisers");

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {
                Page<MusixiserFollow> page = musixiserFollowService.findAllByUserId(pageable, u.getId());
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

                if (addMyFollowDTO.getStatus() == 0) {
                    MusixiserFollowDTO musixiserFollowDTO = new MusixiserFollowDTO();
                    musixiserFollowDTO.setUserId(u.getId());
                    User user = new User();
                    user.setId(addMyFollowDTO.getFollowId());
                    musixiserFollowDTO.setUser(user);

                    MusixiserFollowDTO result = musixiserFollowService.save(musixiserFollowDTO);
                } else if (addMyFollowDTO.getStatus() == 1) {

                    musixiserFollowRepository.deleteByUserIdAndFollowUid(u.getId(), addMyFollowDTO.getFollowId());

                } else {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, "参数错误"));
                }

                return ResponseEntity.ok(new OutputDTO<>(0, "success"));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));

    }
}
