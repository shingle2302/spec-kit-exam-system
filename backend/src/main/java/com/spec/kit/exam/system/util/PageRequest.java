package com.spec.kit.exam.system.util;

/**
 * 分页查询请求对象
 */
public class PageRequest {
    private int page = 1;        // 页码，默认为1
    private int size = 10;       // 每页大小，默认为10
    private String sortBy;       // 排序字段
    private String sortOrder;    // 排序方向：ASC/DESC，默认ASC

    // Getters and Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 1 ? 1 : page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size < 1 ? 10 : Math.min(size, 100); // 限制最大页面大小为100
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 计算偏移量，用于数据库查询
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}