package musixise.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Stages entity.
 */
public class StagesDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer status;


    @NotNull
    private LocalDate createtime;


    private Long userId;


    private Integer audienceNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    public Integer getAudienceNum() {
        return audienceNum;
    }

    public void setAudienceNum(Integer audienceNum) {
        this.audienceNum = audienceNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StagesDTO stagesDTO = (StagesDTO) o;

        if ( ! Objects.equals(id, stagesDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StagesDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", createtime='" + createtime + "'" +
            ", userId='" + userId + "'" +
            ", audienceNum='" + audienceNum + "'" +
            '}';
    }
}
