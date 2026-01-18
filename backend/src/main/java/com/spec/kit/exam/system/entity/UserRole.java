package com.spec.kit.exam.system.entity;

import java.time.LocalDateTime;

/**
 * Entity representing the junction table between User and Role
 */
public class UserRole {
    private String id;           // primary key
    private String userId;       // user id
    private String roleId;       // role id
    private String assignedBy;   // admin who assigned
    private LocalDateTime assignedTime; // when the assignment was made

    // Constructors
    public UserRole() {}

    public UserRole(String id, String userId, String roleId, String assignedBy, LocalDateTime assignedTime) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.assignedBy = assignedBy;
        this.assignedTime = assignedTime;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(LocalDateTime assignedTime) {
        this.assignedTime = assignedTime;
    }
}