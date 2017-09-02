package musixise.web.rest.dto.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by zhaowei on 16/11/19.
 */
@ApiModel(value = "AddMyFollowDTO", description = "关注对象")
public class AddMyFollowDTO implements Serializable {

    @ApiModelProperty( value = "状态 0=关注,1=取消关注", required = true )
    private Integer status;

    @ApiModelProperty( value = "被关注用户ID", required = true )
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
