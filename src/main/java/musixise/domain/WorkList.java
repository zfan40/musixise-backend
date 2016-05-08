package musixise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A WorkList.
 */
@Entity
@Table(name = "work_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "worklist")
public class WorkList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "url")
    private String url;

    @NotNull
    @Column(name = "createtime", nullable = false)
    private LocalDate createtime;

    @ManyToOne
    private User user_id;

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

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user) {
        this.user_id = user;
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

    @Override
    public String toString() {
        return "WorkList{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", url='" + url + "'" +
            ", createtime='" + createtime + "'" +
            '}';
    }
}
