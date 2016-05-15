package musixise.web.rest.dto;

/**
 * Created by zhaowei on 16/5/15.
 */
public class PageDTO {
    private int currentPage;
    private Long total;
    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
