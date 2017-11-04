package musixise.manager;

import musixise.config.OAuthTypesConstants;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.security.jwt.TokenProvider;
import musixise.service.MusixiserService;
import musixise.service.UserService;
import musixise.web.rest.JWTToken;
import musixise.web.rest.dto.ManagedUserDTO;
import musixise.web.rest.dto.SocialInfoDTO;
import musixise.web.rest.dto.user.RegisterDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by zhaowei on 2017/11/4.
 */
@Component("userManager")
public class UserManager {

    @Inject private UserService userService;
    @Inject private MusixiserService musixiserService;
    @Inject private UserDetailsService userDetailsService;
    @Inject private TokenProvider tokenProvider;

    public Boolean createByOauth(SocialInfoDTO socialInfoDTO) {

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        String openId = socialInfoDTO.getOpenId();
        String provider = socialInfoDTO.getProvider();

        String login =String.format(OAuthTypesConstants.USERNAME, provider, openId);
        managedUserDTO.setLogin(login);
        managedUserDTO.setPassword("www.musixise.com@@#@#@111121~~!!!!@@@@"+openId);
        String email = String.format("%s@musixise.com", login);
        managedUserDTO.setEmail(email);
        //创建账号
        User user = userService.createUser(managedUserDTO);

        if (user != null) {
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setRealname(socialInfoDTO.getNickName());
            //保存社交图片
            if (socialInfoDTO.getAvatar() != null) {
                registerDTO.setLargeAvatar(socialInfoDTO.getAvatar());
                registerDTO.setSmallAvatar(socialInfoDTO.getAvatar());
            }

            Musixiser musixiser = musixiserService.registerMusixiser(user.getId(), registerDTO);
            //绑定第三方
            if (musixiser != null) {
                return userService.bindThird(openId, login, provider,
                    socialInfoDTO.getAccessToken(), socialInfoDTO.getRefreshToken(), socialInfoDTO.getExpiresIn());

            }
        }

        return false;
    }


    /**
     * 根据用户信息获取TOKEN
     * @param login 用户名
     * @return
     */
    public JWTToken getTokenByLogin(String login) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);

        //get jwt
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, false);

        return new JWTToken(jwt);
    }

}
