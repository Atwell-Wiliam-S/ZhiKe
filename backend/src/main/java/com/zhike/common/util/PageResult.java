package com.zhike.common.util;

import java.util.List;

/**
 * 分页结果类
 * 用于封装分页查询的结果
 * @param <T> 数据类型
 */
public class PageResult<T> {

    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 当前页数据
     */
    private List<T> records;
    
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 每页条数
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 私有构造函数
     * @param total 总记录数
     * @param records 当前页数据
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param totalPages 总页数
     */
    private PageResult(long total, List<T> records, int page, int pageSize, int totalPages) {
        this.total = total;
        this.records = records;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    /**
     * 构建分页结果
     * @param total 总记录数
     * @param records 当前页数据
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param <T> 数据类型
     * @return 分页结果对象
     */
    public static <T> PageResult<T> build(long total, List<T> records, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return new PageResult<>(total, records, page, pageSize, totalPages);
    }

    /**
     * 获取总记录数
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     * @param total 总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 获取当前页数据
     * @return 当前页数据
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * 设置当前页数据
     * @param records 当前页数据
     */
    public void setRecords(List<T> records) {
        this.records = records;
    }

    /**
     * 获取当前页码
     * @return 当前页码
     */
    public int getPage() {
        return page;
    }

    /**
     * 设置当前页码
     * @param page 当前页码
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 获取每页条数
     * @return 每页条数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页条数
     * @param pageSize 每页条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 设置总页数
     * @param totalPages 总页数
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}