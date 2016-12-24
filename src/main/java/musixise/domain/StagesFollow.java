package musixise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StagesFollow.
 */
@Entity
@Table(name = "mu_stages_follow")
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

    @Column(name = "stages_id")
    private Long stagesId;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @Column(name = "updatetime")
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
            ", stagesId='" + stagesId + "'" +
            ", timestamp='" + timestamp + "'" +
            ", updatetime='" + updatetime + "'" +
            '}';
    }
}
