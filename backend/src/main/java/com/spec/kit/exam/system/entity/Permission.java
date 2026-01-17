package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a permission in the system
 */
@TableName("permissions")
public class Permission {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;                    // primary key
    
    private String menuId;               // associated menu id
    private String buttonName;           // display name of button/function
    private String operationType;        // QUERY, DELETE, MODIFY, UNLOCK, LOCK, CREATE, READ, UPDATE
    private String permissionCode;       // unique permission identifier
    private String description;          // permission description
    private String status;               // ACTIVE, INACTIVE
    
    @TableField(exist = false)
    private List<Role> roles;            // roles with this permission (not in DB, populated via query)
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Permission() {}

    public Permission(String id, String menuId, String buttonName, String operationType,
                      String permissionCode, String description, String status,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.menuId = menuId;
        this.buttonName = buttonName;
        this.operationType = operationType;
        this.permissionCode = permissionCode;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}