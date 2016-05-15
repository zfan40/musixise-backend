package musixise.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Musixiser.
 */
@Entity
@Table(name = "musixiser")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "musixiser")
public class Musixiser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户真实姓名", required = false)
    @Column(name = "realname")
    private String realname;

    @ApiModelProperty(value = "电话号码", required = false)
    @Column(name = "tel")
    private String tel;

    @Column(name = "email")
    private String email;

    @Column(name = "birth")
    private String birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "small_avatar")
    private String smallAvatar;

    @Column(name = "large_avatar")
    private String largeAvatar;

    @Column(name = "nation")
    private String nation;

    @Column(name = "is_master")
    @ApiModelProperty(value = "是否是大师", required = false, allowableValues = "0,1")
    private Integer isMaster;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Musixiser musixiser = (Musixiser) o;
        if(musixiser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, musixiser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Musixiser{" +
            "id=" + id +
            ", realname='" + realname + "'" +
            ", tel='" + tel + "'" +
            ", email='" + email + "'" +
            ", birth='" + birth + "'" +
            ", gender='" + gender + "'" +
            ", smallAvatar='" + smallAvatar + "'" +
            ", largeAvatar='" + largeAvatar + "'" +
            ", nation='" + nation + "'" +
            ", isMaster='" + isMaster + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
