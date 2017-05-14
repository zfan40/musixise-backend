package musixise.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhaowei on 17/5/14.
 */
@Entity
@Table(name = "mu_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "config")
@DynamicUpdate
@SelectBeforeUpdate
@ApiModel(value = "配置")
public class Config extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ckey")
    private String ckey;

    @Column(name = "cval")
    private String cval;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

    @Override
    public String toString() {
        return "Config{" +
            "id=" + id +
            ", ckey='" + ckey + '\'' +
            ", cval='" + cval + '\'' +
            '}';
    }
}
