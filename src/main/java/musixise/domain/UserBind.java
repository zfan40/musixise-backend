package musixise.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 16/12/20.
 */
@Entity
@Table(name = "jhi_user_bind")

public class UserBind extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bid;

    @NotNull
    @Column(name = "open_id", length = 100)
    private String openId;

    @NotNull
    @Column(name = "login", length = 100)
    private String login;

    @NotNull
    @Column(name = "provider", length = 100)
    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
