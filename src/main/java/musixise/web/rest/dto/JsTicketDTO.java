package musixise.web.rest.dto;

/**
 * Created by zhaowei on 2018/3/1.
 */
public class JsTicketDTO {

    private String nonceStr;

    private String jsapiTicket;

    private String timestamp;

    private String url;

    private String signature;

    private String appId;

    public JsTicketDTO() {
    }

    public JsTicketDTO(String nonceStr, String jsapiTicket, String timestamp, String url, String signature) {
        this.nonceStr = nonceStr;
        this.jsapiTicket = jsapiTicket;
        this.timestamp = timestamp;
        this.url = url;
        this.signature = signature;
    }

    public JsTicketDTO(String nonceStr, String jsapiTicket, String timestamp, String url, String signature, String appId) {
        this.nonceStr = nonceStr;
        this.jsapiTicket = jsapiTicket;
        this.timestamp = timestamp;
        this.url = url;
        this.signature = signature;
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNoncestr() {
        return nonceStr;
    }

    public void setNoncestr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
