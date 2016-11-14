package musixise.web.rest.dto.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 16/11/14. sss
 */
public class AddToMyFavoriteMusixisersDTO implements Serializable {


    private static final long serialVersionUID = -2078413137699999437L;

    @NotNull
    private Long followId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }
}
