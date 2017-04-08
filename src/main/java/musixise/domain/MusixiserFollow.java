package musixise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MusixiserFollow.
 */
@Entity
@Table(name = "mu_musixiser_follow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "musixiserfollow")
public class MusixiserFollow extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "follow_uid", nullable = false)
    private Long followId;

    @OneToOne
    @JoinColumn(name = "follow_uid", referencedColumnName = "user_id", insertable=false, updatable=false)
    private Musixiser musixiser;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable=false, updatable=false)
    private Musixiser follower;

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Musixiser getFollower() {
        return follower;
    }

    public void setFollower(Musixiser follower) {
        this.follower = follower;
    }

    public Musixiser getMusixiser() {
        return musixiser;
    }

    public void setMusixiser(Musixiser musixiser) {
        this.musixiser = musixiser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        MusixiserFollow musixiserFollow = (MusixiserFollow) o;
        if(musixiserFollow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, musixiserFollow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MusixiserFollow{" +
            "id=" + id +
            ", userId=" + userId +
            ", musixiser=" + musixiser +
            '}';
    }
}
