package musixise.web.rest.dto.user;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 16/5/15.
 */
public class RegisterAudienceDTO {

    @NotNull
    @Size(min = 5, max = 50)
    @ApiModelProperty(value = "账号名称", required = true)
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    @ApiModelProperty(value = "账号密码", required = true)
    private String password;

    private String nickname;


    private String realname;


    private String email;


    private String tel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "RegisterAudienceDTO{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", nickname='" + nickname + '\'' +
            ", realname='" + realname + '\'' +
            ", email='" + email + '\'' +
            ", tel='" + tel + '\'' +
            '}';
    }
}
