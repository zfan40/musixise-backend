package musixise.web.rest.dto;

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by zhaowei on 16/5/15.
 */
public class PageDTO<T> {

    private List<T> content;
    private long total;
    private  int totalPages;
    private int size;
    private int page;
    private Sort sort;

    public PageDTO(List<T> content, long total, int totalPages, int size, int page, Sort sort) {
        this.content = content;
        this.total = total;
        this.totalPages = totalPages;
        this.size = size;
        this.page = page;
        this.sort = sort;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
