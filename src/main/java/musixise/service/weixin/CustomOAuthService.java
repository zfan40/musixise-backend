package musixise.service.weixin;

import musixise.web.rest.dto.SocialInfoDTO;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * Created by zhaowei on 2017/11/4.
 */
public interface CustomOAuthService extends OAuthService {

    String getoAuthType();
    String getAuthorizationUrl();
    SocialInfoDTO getOAuthUser(Token accessToken);

}
