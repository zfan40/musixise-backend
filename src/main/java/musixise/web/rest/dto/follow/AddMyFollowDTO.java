package musixise.web.rest.dto.follow;

import java.io.Serializable;

/**
 * Created by zhaowei on 16/11/19.
 */
public class AddMyFollowDTO implements Serializable {

    private Integer status;

    private Long followId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }
}
