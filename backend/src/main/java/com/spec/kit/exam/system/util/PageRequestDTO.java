package com.spec.kit.exam.system.util;

/**
 * DTO for pagination request parameters
 */
public class PageRequestDTO {
    private int page = 1;           // Current page number (starting from 1)
    private int size = 10;          // Number of items per page
    private String sortField;       // Field to sort by
    private String sortOrder;       // Sort order: asc or desc
    private Object filters;         // Additional filters as an object

    // Constructors
    public PageRequestDTO() {}

    public PageRequestDTO(int page, int size) {
        this.page = page > 0 ? page : 1;
        this.size = size > 0 ? size : 10;
    }

    // Getters and setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page > 0 ? page : 1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size > 0 ? size : 10;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Object getFilters() {
        return filters;
    }

    public void setFilters(Object filters) {
        this.filters = filters;
    }
}