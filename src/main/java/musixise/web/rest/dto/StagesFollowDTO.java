package musixise.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the StagesFollow entity.
 */
public class StagesFollowDTO implements Serializable {

    private Long id;

    private Long musixiserUid;


    private Long audienceUid;


    private LocalDate timestamp;


    private Long stagesId;


    private LocalDate updtetime;


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
    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }
    public Long getStagesId() {
        return stagesId;
    }

    public void setStagesId(Long stagesId) {
        this.stagesId = stagesId;
    }
    public LocalDate getUpdtetime() {
        return updtetime;
    }

    public void setUpdtetime(LocalDate updtetime) {
        this.updtetime = updtetime;
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
            ", timestamp='" + timestamp + "'" +
            ", stagesId='" + stagesId + "'" +
            ", updtetime='" + updtetime + "'" +
            '}';
    }
}
