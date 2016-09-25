package musixise.web.rest.dto;

import org.springframework.social.oauth2.AccessGrant;

/**
 * Created by zhaowei on 16/9/24.
 */
public class AccessGrantDTO {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
