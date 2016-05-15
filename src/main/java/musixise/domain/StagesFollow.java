package musixise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StagesFollow.
 */
@Entity
@Table(name = "stages_follow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stagesfollow")
public class StagesFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "musixiser_uid")
    private Long musixiserUid;

    @Column(name = "audience_uid")
    private Long audienceUid;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "stages_id")
    private Long stagesId;

    @Column(name = "updtetime")
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
        StagesFollow stagesFollow = (StagesFollow) o;
        if(stagesFollow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stagesFollow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StagesFollow{" +
            "id=" + id +
            ", musixiserUid='" + musixiserUid + "'" +
            ", audienceUid='" + audienceUid + "'" +
            ", timestamp='" + timestamp + "'" +
            ", stagesId='" + stagesId + "'" +
            ", updtetime='" + updtetime + "'" +
            '}';
    }
}
