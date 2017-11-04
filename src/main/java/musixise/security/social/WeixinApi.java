package musixise.security.social;

import musixise.service.weixin.WeixinOAuthService;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

/**
 * Created by zhaowei on 2017/11/4.
 */
public class WeixinApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&state=esfadsgsad34fwdef&scope=snsapi_login#wechat_redirect";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;

    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));

    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new WeixinOAuthService(this, config);
    }
};
