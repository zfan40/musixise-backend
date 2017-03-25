package musixise.web.rest.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the WorkList entity.
 */
public class WorkListDTO implements Serializable {

    private static final long serialVersionUID = 1870328135512535520L;
    private Long id;

    private String title;

    private String cover;

    @NotNull
    @Lob
    private String content;


    private String url;

    private Integer followStatus;


    @NotNull
    private String createdDate;


    private Long userId;

    private Integer collectNum;

    private String lastModifiedDate;

    private String fileHash;

    private OwnerDTO owner;

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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
            ", title='" + title + '\'' +
            ", cover='" + cover + '\'' +
            ", content='" + content + '\'' +
            ", url='" + url + '\'' +
            ", followStatus=" + followStatus +
            ", createdDate=" + createdDate +
            ", userId=" + userId +
            ", status=" + status +
            '}';
    }
}
