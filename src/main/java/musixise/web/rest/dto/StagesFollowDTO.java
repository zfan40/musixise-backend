package musixise.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the StagesFollow entity.
 */
public class StagesFollowDTO implements Serializable {

    private Long id;

    private Long musixiserUid;


    private Long audienceUid;


    private Long stagesId;


    private ZonedDateTime timestamp;


    private ZonedDateTime updatetime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMusixiserUid() {
        return musixiserUid;
    }

    public void setMusixiserUid(Long musixiserUid) {
        this.musixiserUid = musixiserUid;
    }
    public Long getAudienceUid() {
        return audienceUid;
    }

    public void setAudienceUid(Long audienceUid) {
        this.audienceUid = audienceUid;
    }
    public Long getStagesId() {
        return stagesId;
    }

    public void setStagesId(Long stagesId) {
        this.stagesId = stagesId;
    }
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public ZonedDateTime getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(ZonedDateTime updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StagesFollowDTO stagesFollowDTO = (StagesFollowDTO) o;

        if ( ! Objects.equals(id, stagesFollowDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StagesFollowDTO{" +
            "id=" + id +
            ", musixiserUid='" + musixiserUid + "'" +
            ", audienceUid='" + audienceUid + "'" +
            ", stagesId='" + stagesId + "'" +
            ", timestamp='" + timestamp + "'" +
            ", updatetime='" + updatetime + "'" +
            '}';
    }
}
