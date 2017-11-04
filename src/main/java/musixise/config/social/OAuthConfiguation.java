package musixise.config.social;

import musixise.config.OAuthTypesConstants;
import musixise.security.social.WeixinApi;
import musixise.service.weixin.CustomOAuthService;
import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhaowei on 2017/11/4.
 */
@Configuration
public class OAuthConfiguation {

    private static final String CALLBACK_URL = "http://tianmaying.com/oauth/%s/callback";

    @Value("${spring.social.wechat.clientId}") String weixinAppId;
    @Value("${spring.social.wechat.clientSecret}") String weixinAppSecret;

    @Bean
    public CustomOAuthService getSinaOAuthService(){
        return (CustomOAuthService) new ServiceBuilder()
            .provider(WeixinApi.class)
            .apiKey(weixinAppId)
            .apiSecret(weixinAppSecret)
            .scope("snsapi_login")
            .callback(String.format(CALLBACK_URL, OAuthTypesConstants.WECHAT))
            .build();
    }


}
