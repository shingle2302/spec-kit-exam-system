package com.exam.system.dto;

import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean isActive;
}