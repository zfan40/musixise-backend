package musixise.web.rest.dto.favorite;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 16/11/13.
 */
public class UpdateMyWorkStatusDTO implements Serializable {

    private static final long serialVersionUID = -1812607670925312809L;

    @NotNull
    private Long workId;

    @NotNull
    private Integer status;//0remove 1public 2 private

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
