package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.config.Constants;
import musixise.domain.*;
import musixise.repository.UserRepository;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.search.WorkListFollowSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.MusixiserFollowService;
import musixise.service.MusixiserService;
import musixise.service.WorkListService;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.dto.request.AddToMyFavoriteMusixisersDTO;
import musixise.web.rest.dto.request.AddToMyfavoriteWorksDTO;
import musixise.web.rest.mapper.MusixiserMapper;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

    @Inject private MusixiserService musixiserService;

    @Inject private MusixiserMapper musixiserMapper;

    @Inject private MusixiserFollowService musixiserFollowService;

    @RequestMapping(value = "/addToMyFavoriteWorks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加作品到我的收藏", notes = "", response = WorkListFollow.class, position = 2)
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
    @ApiOperation(value = "删除我收藏的作品", notes = "", response = Integer.class, position = 2)
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
    @ApiOperation(value = "获取我的作品收藏列表", notes = "", response = WorkListFollow.class, position = 3)
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

    @RequestMapping(value = "/getFavoriteMusixisers",
        method = RequestMethod.GET,
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
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));
    }

    @RequestMapping(value = "/addToMyFavoriteMusixisers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "关注用户", notes = "", response = MusixiserFollow.class, position = 3)
    @Timed
    public ResponseEntity<?> createMusixiserFollow(@Valid @RequestBody AddToMyFavoriteMusixisersDTO addToMyFavoriteMusixisersDTO) throws URISyntaxException {
        log.debug("REST request to save addToMyFavoriteMusixisers : {}", addToMyFavoriteMusixisersDTO);

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map( u -> {

                MusixiserFollowDTO musixiserFollowDTO = new MusixiserFollowDTO();
                musixiserFollowDTO.setUserId(u.getId());
                User user = new User();
                user.setId(addToMyFavoriteMusixisersDTO.getFollowId());
                musixiserFollowDTO.setUser(user);

                MusixiserFollowDTO result = musixiserFollowService.save(musixiserFollowDTO);
                return ResponseEntity.ok(new OutputDTO<>(0, "success", result));

            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));

    }

}
