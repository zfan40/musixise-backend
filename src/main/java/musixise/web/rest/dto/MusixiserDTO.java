package musixise.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Musixiser entity.
 */
public class MusixiserDTO implements Serializable {

    private static final long serialVersionUID = -3399810298701329130L;
    private Long id;

    private String username;

    private String realname;

    private String tel;

    private String email;

    private String birth;

    private String gender;

    private String smallAvatar;

    private String largeAvatar;

    private String nation;

    private Integer isMaster;

    private String brief;

    private Integer followStatus;

    private Integer followNum;

    private Integer fansNum;

    private Integer songNum;

    @NotNull
    private Long userId;

    private String createdDate;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getSongNum() {
        return songNum;
    }

    public void setSongNum(Integer songNum) {
        this.songNum = songNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    @Override
    public String toString() {
        return "MusixiserDTO{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", realname='" + realname + '\'' +
            ", tel='" + tel + '\'' +
            ", email='" + email + '\'' +
            ", birth='" + birth + '\'' +
            ", gender='" + gender + '\'' +
            ", smallAvatar='" + smallAvatar + '\'' +
            ", largeAvatar='" + largeAvatar + '\'' +
            ", nation='" + nation + '\'' +
            ", isMaster=" + isMaster +
            ", brief='" + brief + '\'' +
            ", followStatus=" + followStatus +
            ", followNum=" + followNum +
            ", fansNum=" + fansNum +
            ", songNum=" + songNum +
            ", userId=" + userId +
            ", createdDate='" + createdDate + '\'' +
            '}';
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MusixiserDTO musixiserDTO = (MusixiserDTO) o;

        if ( ! Objects.equals(id, musixiserDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
