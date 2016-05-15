package musixise.web.rest.dto.response;

import musixise.web.rest.dto.PageDTO;

import java.util.List;

/**
 * Created by zhaowei on 16/5/15.
 */
public class GetStateList extends PageDTO{

    private List<GetState> list;

    public List<GetState> getList() {
        return list;
    }

    public void setList(List<GetState> list) {
        this.list = list;
    }
}
