package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a menu in the system
 */
@TableName("menus")
public class Menu {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    private String name;           // menu display name
    private String parentId;       // parent menu id, null for root menus
    private String path;          // route path for navigation
    private String component;     // component name to render
    private String icon;          // menu icon identifier
    private Integer orderNum;     // display order
    private String status;        // ACTIVE, INACTIVE
    
    @TableField(exist = false)
    private List<Permission> permissions; // associated permissions (not in DB, populated via query)
    
    @TableField(exist = false)
    private List<Menu> children;  // child menu items (not in DB, built from hierarchy)
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Menu() {}

    public Menu(String id, String name, String parentId, String path, String component,
                String icon, Integer orderNum, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.path = path;
        this.component = component;
        this.icon = icon;
        this.orderNum = orderNum;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
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