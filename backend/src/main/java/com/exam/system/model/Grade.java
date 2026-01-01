package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("grades")
public class Grade extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("name")
    private String name;
    
    @TableField("code")
    private String code;
    
    @TableField("description")
    private String description;
    
    @TableField("min_age")
    private Integer minAge;
    
    @TableField("max_age")
    private Integer maxAge;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("is_active")
    private Boolean isActive;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Grade() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}