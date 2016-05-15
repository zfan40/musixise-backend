package musixise.web.rest.dto;

import java.time.LocalDate;

/**
 * Created by zhaowei on 16/5/15.
 */
public class JoinStageDTO {

    private Long musixiserUid;

    private Long stagesId;

    public Long getMusixiserUid() {
        return musixiserUid;
    }

    public void setMusixiserUid(Long musixiserUid) {
        this.musixiserUid = musixiserUid;
    }

    public Long getStagesId() {
        return stagesId;
    }

    public void setStagesId(Long stagesId) {
        this.stagesId = stagesId;
    }

    @Override
    public String toString() {
        return "JoinStageDTO{" +
            "musixiserUid=" + musixiserUid +
            ", stagesId=" + stagesId +
            '}';
    }
}
