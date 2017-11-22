package musixise.service.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import musixise.config.OAuthTypesConstants;
import musixise.web.rest.dto.SocialInfoDTO;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaowei on 2017/11/4.
 */
public class WeixinOAuthService extends OAuth20ServiceImpl implements CustomOAuthService {

    private final Logger log = LoggerFactory.getLogger(WeixinOAuthService.class);

    private final DefaultApi20 api;
    private final OAuthConfig config;
    private final String authorizationUrl;

    public WeixinOAuthService(DefaultApi20 api, OAuthConfig config, DefaultApi20 api1, OAuthConfig config1, String authorizationUrl) {
        super(api, config);
        this.api = api1;
        this.config = config1;
        this.authorizationUrl = authorizationUrl;
    }

    public WeixinOAuthService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.authorizationUrl = getAuthorizationUrl(null);
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier){
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter("appid", config.getApiKey());
        request.addQuerystringParameter("secret", config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        Response response = request.send();
        String responceBody = response.getBody();
        log.trace("get accesstoken fail {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            log.error("get accesstoken fail {}", responceBody);
            return null;
        } else {
            return new Token(JSONPath.eval(result, "$.access_token").toString(), "", responceBody);
        }
    }

    @Override
    public SocialInfoDTO getOAuthUser(Token accessToken) {
        //返回openID
        Object result = JSON.parse(accessToken.getRawResponse());
        String openId =  JSONPath.eval(result, "$.openid").toString();
        SocialInfoDTO socialInfoDTO = getUserInfo(accessToken.getToken(), openId);

        if (socialInfoDTO != null) {

            socialInfoDTO.setAccessToken(JSONPath.eval(result, "$.access_token").toString());
            socialInfoDTO.setRefreshToken(JSONPath.eval(result, "$.refresh_token").toString());
            int expiresIn = (int)System.currentTimeMillis()/1000 + Integer.valueOf(JSONPath.eval(result, "$.expires_in").toString());
            socialInfoDTO.setExpiresIn(expiresIn);
            return socialInfoDTO;
        }

        return null;
    }

    // https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    private SocialInfoDTO getUserInfo(String accessToken, String openId) {
        String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), String.format(USER_INFO_URL, accessToken, openId));
        Response response = request.send();
        String responceBody = response.getBody();
        log.trace("get getuserinfo fail {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            log.error("get getuserinfo fail {}", responceBody);
            return null;
        } else {
            SocialInfoDTO socialInfoDTO = new SocialInfoDTO();
            socialInfoDTO.setOpenId(JSONPath.eval(result, "$.openid").toString());
            socialInfoDTO.setProvider(getoAuthType());
            socialInfoDTO.setAvatar(JSONPath.eval(result, "$.headimgurl").toString());
            socialInfoDTO.setNickName(JSONPath.eval(result, "$.nickname").toString());
            socialInfoDTO.setSex(Integer.valueOf(JSONPath.eval(result, "$.sex").toString()));
            return socialInfoDTO;
        }
    }

    @Override
    public String getoAuthType() {
        return OAuthTypesConstants.WECHAT;
    }

    @Override
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

}
