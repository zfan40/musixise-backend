package musixise.web.rest.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * A DTO for the WorkList entity.
 */
public class WorkListDTO implements Serializable {

    private static final long serialVersionUID = 1870328135512535520L;
    private Long id;

    @NotNull
    @Lob
    private String content;


    private String url;

    private Integer followStatus;


    @NotNull
    private LocalDateTime createtime;


    private Long userId;

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    private Integer status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
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

        WorkListDTO workListDTO = (WorkListDTO) o;

        if ( ! Objects.equals(id, workListDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkListDTO{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", url='" + url + "'" +
            ", createtime='" + createtime + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
