package musixise.web.rest.dto.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 16/11/12.
 */
public class AddToMyfavoriteWorksDTO implements Serializable {


    private static final long serialVersionUID = 4130998162320655350L;

    @NotNull
    private Long workId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }
}
