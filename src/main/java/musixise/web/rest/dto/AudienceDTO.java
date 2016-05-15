package musixise.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Audience entity.
 */
public class AudienceDTO implements Serializable {

    private Long id;

    private String nickname;


    private String realname;


    private String email;


    private String tel;


    private Long userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AudienceDTO audienceDTO = (AudienceDTO) o;

        if ( ! Objects.equals(id, audienceDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AudienceDTO{" +
            "id=" + id +
            ", nickname='" + nickname + "'" +
            ", realname='" + realname + "'" +
            ", email='" + email + "'" +
            ", tel='" + tel + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
