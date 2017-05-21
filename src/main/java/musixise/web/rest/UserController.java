package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.swagger.annotations.*;
import musixise.config.Constants;
import musixise.config.JHipsterProperties;
import musixise.config.social.SocialConfiguration;
import musixise.domain.Musixiser;
import musixise.domain.MusixiserFollow;
import musixise.domain.User;
import musixise.domain.UserBind;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) throws URISyntaxException {

        //注册账号
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(registerDTO.getUsername());
        managedUserDTO.setPassword(registerDTO.getPassword());
        managedUserDTO.setEmail(registerDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_USERNAME_ALREADY_USED, "用户名已存在"));

        } else if (userRepository.findOneByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_EMAIL_ALREADY_USED, "邮箱已存在"));
        } else {

            //  User newUser = userService.createUser(managedUserDTO);
            User newUser = userService.createUserBySite(
                managedUserDTO.getLogin(),
                managedUserDTO.getPassword(),
                managedUserDTO.getFirstName(),
                managedUserDTO.getLastName(),
                managedUserDTO.getEmail()
            );
            //注册个人信息
            Musixiser result = musixiserService.registerMusixiser(newUser.getId(), registerDTO);

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
            .orElseGet(() -> ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_NO_LOGIN, "用户未登陆")));
    }

    @RequestMapping(value = "/updateInfo",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改当前用户信息", notes = "修改当前用户信息", response = Musixiser.class, position = 4)
    @Timed
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateMusixiserDTO musixiser) {
        log.debug("REST request to update MusixiserEx : {}", musixiser);

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {

                Musixiser result = musixiserService.updateInfo(u.getId(), musixiser);

                return ResponseEntity.ok(new OutputDTO<>(0, "success", result));

            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/authByAccessToken/{platform}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "社交登录", notes = "使用社交平台access_token登录, 平台标识 platform=weibo, wechat, qq", response = OutputDTO.class, position = 5)
    @Timed
    public ResponseEntity<?> authenticateBySocialToken(@ApiParam(value = "platform", required = true, defaultValue = "weibo") @PathVariable String platform, @RequestBody AccessGrantDTO accessGrantDTO) {

        log.debug("REST request to auth social : {}", platform);

        AccessGrant accessGrant = new AccessGrant(accessGrantDTO.getAccessToken());

        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();

        if (oAuth2ConnectionFactoryMap.containsKey(platform)) {
            UserProfile userProfile = null;
            try {
                Connection<?> connection  = oAuth2ConnectionFactoryMap.get(platform).createConnection(accessGrant);
                userProfile = connection.fetchUserProfile();

                //初始化社交账号
                socialService.createSocialConnection(userProfile.getUsername(), connection);

                UserDetails user = null;

                //check user bind info
                String login = userService.isUserBindThis(userProfile.getUsername(), platform);
                if (login == null) {
                    //未绑定任何账号
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_USER_NOT_BIND, "未绑定任何账号", platform));
                } else {
                    user = userDetailsService.loadUserByUsername(login);
                }

                //get jwt
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                String jwt = tokenProvider.createToken(authenticationToken, false);

                return ResponseEntity.ok(new OutputDTO<>(0, "success", new JWTToken(jwt)));
            } catch (Exception e) {
                log.error("Exception creating social user: ", e);
                return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_CREATE_USER_ACCOUNT_FAIL, String.format("创建用户信息失败 %s", e.getMessage()), userProfile));
            }
        } else {

            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_SOCIAL_PLATFORM_NOT_EXIST, "不存在的平台标识", platform));
        }

    }

    @RequestMapping(value = "/bindThird/{openId}/{platform}",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "绑定第三方账号信息", notes = "传入第三方账号账号信息", response = OutputDTO.class, position = 5)
    public ResponseEntity<?> bindUser(@PathVariable String openId,
                                      @PathVariable String platform,
                                      @Valid @RequestBody LoginDTO loginDTO) {

        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();

        if (!oAuth2ConnectionFactoryMap.containsKey(platform)) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_PARAMS, String.format("参数错误 (%s)", platform)));
        }

        String checkOpenId = userService.isUserBindThis(openId, platform);

        if (checkOpenId != null) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_THIRD_ALREADY_BIND, "账号已绑定 "));
        }

        //检测账号是否被绑定
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {

            String jwt = userService.auth(authenticationToken);

            //验证成功开始进行绑定
            List<UserBind> userBindList = userService.getUserBind(loginDTO.getUsername());

            for (UserBind userBind : userBindList) {

                if (userBind.getOpenId().equals(openId) && userBind.getProvider().equals(platform)) {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_THIRD_ALREADY_BIND, "账号已绑定 "));
                }

                //是否绑定过另一个同平台账号
                if (userBind.getProvider().equals(platform)) {
                    return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_THIRD_BIND_CONFLICT, "同一个平台只能绑定一个账号"));
                }

            }

            userService.bindThird(openId, loginDTO.getUsername(), platform);

            return ResponseEntity.ok(new OutputDTO<>(0, "success", new JWTToken(jwt)));

        } catch (AuthenticationException exception) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_USER_AUTH_FAIL, "账号或密码不对"));
        }

    }

    @RequestMapping(value = "/autoBindThird/{platform}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "自动绑定第三方账号信息", notes = "帮助用户自动创建一个账号并和当前第三方账号进行绑定。", response = OutputDTO.class, position = 5)
    public ResponseEntity<?> autoBindUser(@PathVariable String platform, @RequestBody AccessGrantDTO accessGrantDTO) {

        log.debug("REST request to auto bind third : {}", platform);

        AccessGrant accessGrant = new AccessGrant(accessGrantDTO.getAccessToken());

            Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();

        if (oAuth2ConnectionFactoryMap.containsKey(platform)) {
            UserProfile userProfile = null;
            try {
                Connection<?> connection  = oAuth2ConnectionFactoryMap.get(platform).createConnection(accessGrant);
                userProfile = connection.fetchUserProfile();

                UserDetails user = null;

                socialService.createSocialUser(connection, "zh-cn", userProfile);

                user = userDetailsService.loadUserByUsername(userProfile.getUsername());

                //建立绑定信息
                userService.bindThird(userProfile.getUsername(), userProfile.getUsername(), platform);

                //get jwt
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                String jwt = tokenProvider.createToken(authenticationToken, false);

                return ResponseEntity.ok(new OutputDTO<>(0, "success", new JWTToken(jwt)));
            } catch (Exception e) {
                log.error("Exception creating social user: ", e);
                return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_CREATE_USER_ACCOUNT_FAIL, String.format("创建用户信息失败 %s", e.getMessage()), userProfile));
            }
        } else {

            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_SOCIAL_PLATFORM_NOT_EXIST, "不存在的平台标识", platform));
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
                Optional<MusixiserFollow> musixiserFollow = userService.getFollowInfo(musixiserDTO.getUserId());
                if (musixiserFollow.isPresent()) {
                   musixiserDTO.setFollowStatus(1);
                } else {
                    musixiserDTO.setFollowStatus(0);
                }
                return ResponseEntity.ok(new OutputDTO<>(0, "success", musixiserDTO));
            }
        }

        return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_USER_NOT_FOUND, "用户不存在"));

    }
}
