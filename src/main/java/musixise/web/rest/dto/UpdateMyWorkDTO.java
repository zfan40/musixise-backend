package musixise.web.rest.dto;

import java.io.Serializable;

/**
 * Created by zhaowei on 17/2/25.
 */
public class UpdateMyWorkDTO implements Serializable {

    private static final long serialVersionUID = -2412723726844447093L;
    private String title;

    private String cover;

    private String content;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
