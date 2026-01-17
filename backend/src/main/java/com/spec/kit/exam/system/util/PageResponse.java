package com.spec.kit.exam.system.util;

import java.util.List;

/**
 * 分页查询响应体
 */
public class PageResponse<T> {
    private List<T> data;          // 当前页数据
    private long total;            // 总记录数
    private int page;              // 当前页码
    private int size;              // 每页大小
    private int totalPage;         // 总页数
    private boolean hasNext;       // 是否有下一页
    private boolean hasPrevious;   // 是否有上一页

    // 构造函数
    public PageResponse() {}

    public PageResponse(List<T> data, long total, int page, int size) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPage = (int) Math.ceil((double) total / size);
        this.hasNext = page < totalPage;
        this.hasPrevious = page > 1;
    }

    // 静态工厂方法
    public static <T> PageResponse<T> of(List<T> data, long total, int page, int size) {
        return new PageResponse<>(data, total, page, size);
    }

    // Getters and Setters
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.totalPage = size > 0 ? (int) Math.ceil((double) total / size) : 0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}