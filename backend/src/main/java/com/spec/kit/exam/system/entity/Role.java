package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("roles")
public class Role {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id; // UUID (Primary Key, auto-generated)
    
    private String name; // String (Required, unique, 3-100 chars)
    
    private String description; // String (Optional, up to 500 chars)
    
    private String permissions; // String (JSON format - Required, contains permission matrix)
    
    private Boolean isSuperAdminRole; // Boolean (Default: false, indicates if this is the super admin role)
    
    private Boolean isActive; // Boolean (Default: true)
    
    private LocalDateTime createdAt; // LocalDateTime (Auto-generated)
    
    private LocalDateTime updatedAt; // LocalDateTime (Auto-generated)

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPermissions() {
        return permissions;
    }

    public Boolean getIsSuperAdminRole() {
        return isSuperAdminRole;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setIsSuperAdminRole(Boolean isSuperAdminRole) {
        this.isSuperAdminRole = isSuperAdminRole;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}