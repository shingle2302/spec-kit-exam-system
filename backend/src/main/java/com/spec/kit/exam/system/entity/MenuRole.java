package com.spec.kit.exam.system.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing the junction table between Menu and Role
 */
public class MenuRole {
    private String id;                    // primary key
    private String menuId;               // menu id
    private String roleId;               // role id
    private List<String> permissions;    // specific permission codes allowed
    private LocalDateTime assignedTime;  // when the assignment was made

    // Constructors
    public MenuRole() {}

    public MenuRole(String id, String menuId, String roleId, List<String> permissions, LocalDateTime assignedTime) {
        this.id = id;
        this.menuId = menuId;
        this.roleId = roleId;
        this.permissions = permissions;
        this.assignedTime = assignedTime;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(LocalDateTime assignedTime) {
        this.assignedTime = assignedTime;
    }
}