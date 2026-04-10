package com.zhike.common.util;

import java.util.List;

public class PageResult<T> {

    private long total;
    private List<T> records;
    private int current;
    private int size;
    private int pages;

    private PageResult(long total, List<T> records, int current, int size, int pages) {
        this.total = total;
        this.records = records;
        this.current = current;
        this.size = size;
        this.pages = pages;
    }

    public static <T> PageResult<T> build(long total, List<T> records, int current, int size) {
        int pages = (int) Math.ceil((double) total / size);
        return new PageResult<>(total, records, current, size, pages);
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

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

}