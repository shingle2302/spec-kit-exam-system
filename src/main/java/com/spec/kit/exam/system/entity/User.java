package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("users")
public class User {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id; // UUID (Primary Key, auto-generated)
    
    private String username; // String (Unique, required, 3-50 chars, alphanumeric + underscore/hyphen)
    
    private String passwordHash; // String (Required, BCrypt hashed, min 60 chars)
    
    private String phone; // String (Optional, validated format, unique if provided)
    
    private String email; // String (Required, unique, valid email format)
    
    private String status; // String (ACTIVE, INACTIVE, SUSPENDED, LOCKED) - default: ACTIVE
    
    private String roleId; // String (Foreign Key to Role, nullable)
    
    private LocalDateTime createdAt; // LocalDateTime (Auto-generated)
    
    private LocalDateTime updatedAt; // LocalDateTime (Auto-generated)
    
    private LocalDateTime lastLoginAt; // LocalDateTime (Nullable)
    
    private LocalDateTime passwordChangedAt; // LocalDateTime (Auto-generated, updates with password change)
    
    private Integer failedLoginAttempts; // Integer (Default: 0, tracks consecutive failed login attempts)
    
    private LocalDateTime lockedUntil; // LocalDateTime (Nullable, timestamp until which account is locked)
    
    private Boolean isSuperAdmin; // Boolean (Default: false, indicates if user has super admin privileges)

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getRoleId() {
        return roleId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}