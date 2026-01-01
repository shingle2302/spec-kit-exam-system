package com.exam.system.dto;

import lombok.Data;

@Data
public class GradeDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer minAge;
    private Integer maxAge;
    private Boolean isActive;
}