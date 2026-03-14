package com.spec.kit.exam.system.util;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for pagination request parameters.
 *
 * <p>Unified request field names: page, size, filters.</p>
 */
public class PageRequestDTO {
    private int page = 1;           // Current page number (starting from 1)
    private int size = 10;          // Number of items per page
    private Map<String, Object> filters = new HashMap<>();

    // Constructors
    public PageRequestDTO() {}

    public PageRequestDTO(int page, int size) {
        setPage(page);
        setSize(size);
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
        this.size = size > 0 ? Math.min(size, 100) : 10;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters == null ? new HashMap<>() : filters;
    }
}
