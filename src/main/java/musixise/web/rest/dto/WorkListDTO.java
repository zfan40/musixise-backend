package musixise.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the WorkList entity.
 */
public class WorkListDTO implements Serializable {

    private Long id;

    @NotNull
    @Lob
    private String content;


    private String url;


    @NotNull
    private LocalDate createtime;


    private Long userId;


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
    public LocalDate getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDate createtime) {
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
