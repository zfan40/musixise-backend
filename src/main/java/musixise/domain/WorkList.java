package musixise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkList.
 */
@Entity
@Table(name = "mu_work_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "worklist")
public class WorkList extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty(message="标题不能为空")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "cover")
    private String cover;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "url")
    private String url;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "collect_num")
    private Integer collectNum;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable=false, updatable=false)
    private Musixiser musixiser;

    public Musixiser getMusixiser() {
        return musixiser;
    }

    public void setMusixiser(Musixiser musixiser) {
        this.musixiser = musixiser;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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
        WorkList workList = (WorkList) o;
        if(workList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workList.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WorkList{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", cover='" + cover + '\'' +
            ", content='" + content + '\'' +
            ", url='" + url + '\'' +
            ", userId=" + userId +
            ", status=" + status +
            '}';
    }
}
