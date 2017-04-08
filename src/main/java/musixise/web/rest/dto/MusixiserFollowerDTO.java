package musixise.web.rest.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhaowei on 17/4/8.
 */
public class MusixiserFollowerDTO {

    @NotNull
    private Long userId;

    private String realname;

    private String smallAvatar;

    private String largeAvatar;

    private String createdDate;

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "MusixiserFollowerDTO{" +
            "userId=" + userId +
            ", realname='" + realname + '\'' +
            ", smallAvatar='" + smallAvatar + '\'' +
            ", largeAvatar='" + largeAvatar + '\'' +
            ", createdDate='" + createdDate + '\'' +
            '}';
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
    }

    public String getLargeAvatar() {
        return largeAvatar;
    }

    public void setLargeAvatar(String largeAvatar) {
        this.largeAvatar = largeAvatar;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
