package musixise.web.rest.audience;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import musixise.domain.Musixiser;
import musixise.domain.Stages;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.repository.MusixiserRepository;
import musixise.repository.UserRepository;
import musixise.security.SecurityUtils;
import musixise.service.AudienceService;
import musixise.service.StagesFollowService;
import musixise.service.StagesService;
import musixise.service.UserService;
import musixise.web.rest.dto.*;
import musixise.web.rest.dto.response.GetState;
import musixise.web.rest.dto.response.GetStateList;
import musixise.web.rest.dto.user.RegisterAudienceDTO;
import musixise.web.rest.mapper.AudienceMapper;
import musixise.web.rest.mapper.StagesMapper;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.node.BooleanNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 16/5/15.
 */
@RestController
@RequestMapping("/api/audiences")
@Api(value = "audience", description = "观众相关接口", position = 2)
public class AudienceExtResource {

    private final Logger log = LoggerFactory.getLogger(AudienceExtResource.class);

    @Inject
    private StagesService stagesService;

    @Inject
    private StagesMapper stagesMapper;
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private AudienceService audienceService;

    @Inject
    private AudienceMapper audienceMapper;

    @Inject
    private MusixiserRepository musixiserRepository;

    @Inject
    private StagesFollowService stagesFollowService;



    @RequestMapping(value = "/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation(value = "普通用户注册", notes = "返回用户实体对象", response = Musixiser.class, position = 2)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "注册成功", response = Musixiser.class),
        @ApiResponse(code = 404, message = "找不到页面"),
        @ApiResponse(code = 500, message = "内部报错")}
    )
    @Timed
    public ResponseEntity<?> register(@Valid @RequestBody RegisterAudienceDTO registerDTO, HttpServletRequest request) throws URISyntaxException {

        //注册普通用户账号 auditor
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(registerDTO.getUsername());
        managedUserDTO.setPassword(registerDTO.getPassword());
        managedUserDTO.setEmail(registerDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "username already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {

            //  User newUser = userService.createUser(managedUserDTO);
            User newUser = userService.createUserBySite(
                managedUserDTO.getLogin(),
                managedUserDTO.getPassword(),
                managedUserDTO.getFirstName(),
                managedUserDTO.getLastName(),
                managedUserDTO.getEmail()
            );
            //保存个人信息
            Musixiser musixiser = new Musixiser();

            musixiser.setUserId(newUser.getId());
            musixiser.setRealname(registerDTO.getRealname());
            musixiser.setTel(registerDTO.getTel());
            musixiser.setEmail(registerDTO.getEmail());

            AudienceDTO audienceDTO = new AudienceDTO();
            audienceDTO.setUserId(newUser.getId());
            audienceDTO.setRealname(registerDTO.getRealname());
            audienceDTO.setTel(registerDTO.getTel());
            audienceDTO.setEmail(registerDTO.getEmail());

            AudienceDTO result = audienceService.save(audienceDTO);

            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert("musixiser", result.getId().toString()))
                .body(result);
        }
    }

    @RequestMapping(value = "/getOnStageList",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取演出列表", notes = "获取正在演出的音乐人列表", response = Musixiser.class, position = 2)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<GetStateList> getOnStagesList(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of on Stages List");
        Page<Stages> page = stagesService.findAll(pageable);
        List<StagesDTO> stagesList = stagesMapper.stagesToStagesDTOs(page.getContent());
        List<GetState> getStateLists = new ArrayList<>();

        for (StagesDTO stagesDTO : stagesList) {
            //查找音乐人信息
            GetState getState = new GetState();
            Musixiser musixiser = musixiserRepository.findOneByUserId(stagesDTO.getUserId());
            getState.setStateId(stagesDTO.getId());
            getState.setUserId(stagesDTO.getUserId());
            getState.setGender(musixiser.getGender());
            getState.setSmallAvatar(musixiser.getSmallAvatar());
            getState.setLargeAvatar(musixiser.getLargeAvatar());
            getState.setIsMaster(musixiser.getIsMaster());
            getState.setNation(musixiser.getNation());
            getState.setAudienceNum(100);
            getStateLists.add(getState);
        }
        //显示列表加分页信息
        GetStateList getStateList = new GetStateList();
        getStateList.setList(getStateLists);
        getStateList.setCurrentPage(page.getNumber());
        getStateList.setPageSize(page.getSize());
        getStateList.setTotal(page.getTotalElements());

        return ResponseEntity.ok().body(getStateList);
    }

    @RequestMapping(value = "/joinStage",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "观看演出", notes = "用户进入某演出", response = StagesFollowDTO.class, position = 2)
    @Timed
    public ResponseEntity<StagesFollowDTO> joinStage(@RequestBody JoinStageDTO joinStageDTO) throws URISyntaxException {
        log.debug("REST request to join Stages : {}", joinStageDTO);


        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                StagesFollowDTO stagesFollowDTO = new StagesFollowDTO();
                stagesFollowDTO.setAudienceUid(u.getId());
                stagesFollowDTO.setMusixiserUid(joinStageDTO.getMusixiserUid());
                stagesFollowDTO.setStagesId(joinStageDTO.getStagesId());
                stagesFollowDTO.setTimestamp(ZonedDateTime.now());
                stagesFollowDTO.setUpdatetime(ZonedDateTime.now());

                StagesFollowDTO result = stagesFollowService.save(stagesFollowDTO);

                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityCreationAlert("stagesFollow", result.getId().toString()))
                    .body(result);

            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/leaveStage/{musixiserUid}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "离开演出", notes = "用户离开某演出", response = Boolean.class, position = 2)
    @Timed
    public ResponseEntity<Boolean> leaveStage(@PathVariable Long musixiserUid) {
        log.debug("REST request to leaveStage Stages : {}", musixiserUid);
        //stagesService.delete(id);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

}
