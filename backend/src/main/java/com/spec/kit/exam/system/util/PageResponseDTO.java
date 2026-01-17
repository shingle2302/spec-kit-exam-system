package com.spec.kit.exam.system.util;

import java.util.List;

/**
 * Response wrapper for paginated data
 */
public class PageResponseDTO<T> {
    private List<T> records;        // The data records for the current page
    private long total;             // Total number of records across all pages
    private int current;            // Current page number
    private int size;               // Size of the current page
    private int pages;              // Total number of pages

    // Constructors
    public PageResponseDTO() {}

    public PageResponseDTO(List<T> records, long total, int current, int size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }

    // Static factory method to create a PageResponseDTO
    public static <T> PageResponseDTO<T> of(List<T> records, long total, int current, int size) {
        return new PageResponseDTO<>(records, total, current, size);
    }

    // Getters and setters
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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