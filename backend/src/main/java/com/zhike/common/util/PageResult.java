package com.zhike.common.util;

import java.util.List;

public class PageResult<T> {

    private long total;
    private List<T> records;
    private int page;
    private int pageSize;
    private int totalPages;

    private PageResult(long total, List<T> records, int page, int pageSize, int totalPages) {
        this.total = total;
        this.records = records;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public static <T> PageResult<T> build(long total, List<T> records, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return new PageResult<>(total, records, page, pageSize, totalPages);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}