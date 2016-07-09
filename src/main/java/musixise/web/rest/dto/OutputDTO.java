package musixise.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhaowei on 16/7/3.
 */
public class OutputDTO<T> {

    @JsonProperty("errcode")
    private Integer errCode;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("data")
    private T data;

    public OutputDTO(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public OutputDTO(String errMsg) {
        this.errMsg = errMsg;
        this.errCode = 0;
    }

    public OutputDTO(Integer errCode, String errMsg, T data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
