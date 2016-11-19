package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.swagger.annotations.*;
import musixise.config.JHipsterProperties;
import musixise.config.social.SocialConfiguration;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.repository.MusixiserRepository;
import musixise.repository.StagesRepository;
import musixise.repository.UserRepository;
import musixise.repository.WorkListRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.repository.search.StagesSearchRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.security.SecurityUtils;
import musixise.security.jwt.TokenProvider;
import musixise.service.MusixiserService;
import musixise.service.SocialService;
import musixise.service.UserService;
import musixise.web.rest.dto.*;
import musixise.web.rest.dto.user.LoginDTO;
import musixise.web.rest.dto.user.RegisterDTO;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by zhaowei on 16/11/19.
 */
@Api(value = "user", description = "账号管理", position = 0)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private MusixiserRepository musixiserRepository;

    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private StagesRepository stagesRepository;

    @Inject
    private StagesSearchRepository stagesSearchRepository;

    @Inject
    private WorkListRepository workListRepository;

    @Inject
    private WorkListSearchRepository workListSearchRepository;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    @Autowired
    SocialConfiguration socialConfiguration;

    @Inject
    private SocialService socialService;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private MusixiserService musixiserService;

    @Inject
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation(value = "注册", notes = "返回用户实体对象", response = Musixiser.class, position = 1)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "注册成功", response = Musixiser.class),
        @ApiResponse(code = 404, message = "找不到页面"),
        @ApiResponse(code = 500, message = "内部报错")}
    )
    @Timed
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) throws URISyntaxException {

        //注册账号
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(registerDTO.getUsername());
        managedUserDTO.setPassword(registerDTO.getPassword());
        managedUserDTO.setEmail(registerDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(20000, "用户名已存在"));

        } else if (userRepository.findOneByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(20000, "邮箱已存在"));
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
            musixiser.setBirth(registerDTO.getBirth());
            musixiser.setGender(registerDTO.getGender());

            //判断图片是否为空,为空则设置默认图片
            if (registerDTO.getLargeAvatar() == null || registerDTO.getLargeAvatar().equals("")) {
                String defalutAvatar = musixiserService.getDefaultAvatar();
                registerDTO.setLargeAvatar(defalutAvatar);
                registerDTO.setSmallAvatar(defalutAvatar);
            }

            musixiser.setSmallAvatar(registerDTO.getSmallAvatar());
            musixiser.setLargeAvatar(registerDTO.getLargeAvatar());
            musixiser.setNation(registerDTO.getNation());

            Musixiser result = musixiserRepository.save(musixiser);

            //搜索索引
            musixiserSearchRepository.save(result);

            return ResponseEntity.ok(new OutputDTO<>(0, "success", result));
        }
    }

    @ApiOperation(value = "登录", notes = "用户认证并获取秘钥,后续接口调用都依赖此秘钥", position = 2)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            String jwt = userService.auth(authenticationToken);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/getInfo",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取我的个人信息", notes = "返回当前用户信息", response = Musixiser.class, position = 3)
    @Timed
    public ResponseEntity<?> getMusixiserInfo() {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {

                MusixiserDTO musixiserDTO = musixiserService.getInfoByUid(u.getId());

                return ResponseEntity.ok(new OutputDTO<>(0, "success", musixiserDTO));
            })
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆")));
    }

    @RequestMapping(value = "/updateInfo",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改当前用户信息", notes = "修改当前用户信息", response = Musixiser.class, position = 4)
    @Timed
    public ResponseEntity<Musixiser> updateInfo(@Valid @RequestBody Musixiser musixiser) {
        log.debug("REST request to update MusixiserEx : {}", musixiser);

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                Musixiser musixiserCmp = musixiserRepository.findOneByUserId(u.getId());
                musixiser.setId(0l);
                if (musixiser.getLargeAvatar() != null) {
                    musixiserCmp.setLargeAvatar(musixiser.getLargeAvatar());
                }
                if (musixiser.getSmallAvatar() != null) {
                    musixiserCmp.setSmallAvatar(musixiser.getSmallAvatar());
                }

                if (musixiser.getRealname() != null) {
                    musixiserCmp.setRealname(musixiser.getRealname());
                }

                Musixiser result = musixiserRepository.save(musixiserCmp);
                musixiserSearchRepository.save(result);

                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("musixiser", musixiser.getId().toString()))
                    .body(result);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/authByAccessToken/{platform}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "社交登录", notes = "使用社交平台access_token登录, 平台标识 platform=weibo, wechat, qq", response = WorkList.class, position = 5)
    @Timed
    public ResponseEntity<?> authenticateBySocialToken(@ApiParam(value = "platform", required = true, defaultValue = "weibo") @PathVariable String platform, @RequestBody AccessGrantDTO accessGrantDTO) {

        log.debug("REST request to auth social : {}", platform);

        AccessGrant accessGrant = new AccessGrant(accessGrantDTO.getAccessToken());

        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();

        if (oAuth2ConnectionFactoryMap.containsKey(platform)) {
            Connection<?> connection  = oAuth2ConnectionFactoryMap.get(platform).createConnection(accessGrant);
            UserProfile userProfile = connection.fetchUserProfile();

            try {
                //init user account
                socialService.createSocialUser(connection, "en");
                //get jwt
                UserDetails user = userDetailsService.loadUserByUsername(userProfile.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                String jwt = tokenProvider.createToken(authenticationToken, false);
                return ResponseEntity.ok(new OutputDTO<>(0, "success", new JWTToken(jwt)));
            } catch (Exception e) {
                log.error("Exception creating social user: ", e);
                return ResponseEntity.ok(new OutputDTO<>(20000, "创建用户信息失败", userProfile));
            }
        } else {

            return ResponseEntity.ok(new OutputDTO<>(20000, "不存在的平台标识", platform));
        }

    }


    @RequestMapping(value = "/detail/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "通过 UID 获取用户信息", notes = "通过 UID 获取用户信息", response = MusixiserDTO.class, position = 5)
    @Timed
    public ResponseEntity<?> getMusixiser(@PathVariable Long id) {

        if (id > 0) {
            MusixiserDTO musixiserDTO = musixiserService.getInfoByUid(id);
            if (musixiserDTO != null) {
                return ResponseEntity.ok(new OutputDTO<>(0, "success", musixiserDTO));
            }
        }

        return ResponseEntity.ok(new OutputDTO<>(20000, "用户未登陆"));

    }
}
