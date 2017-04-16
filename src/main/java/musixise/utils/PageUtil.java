package musixise.utils;

import musixise.web.rest.dto.PageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by zhaowei on 17/4/16.
 */
public class PageUtil<S, T> {

    public static <S, T> PageDTO  getPage(List<S> l, Page<T> page) {

        return new PageDTO(l, page.getTotalElements(),
            page.hasNext(), page.getTotalPages(), page.getSize(), page.getNumber()+1,
            page.getSort(), page.isFirst(), page.getNumberOfElements());

    }
}
