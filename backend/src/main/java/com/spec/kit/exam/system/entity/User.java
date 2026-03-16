package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spec.kit.exam.system.annotation.Encrypted;
import java.time.LocalDateTime;

@TableName("users")
public class User {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    private String username;
    
    private String passwordHash;
    
    @Encrypted
    private String phone;
    
    private String email;
    
    private String status;
    
    private String roleId;
    
    private String role;
    
    @Encrypted
    private String idCard;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLoginAt;
    
    private LocalDateTime passwordChangedAt;
    
    private Integer failedLoginAttempts;
    
    private LocalDateTime lockedUntil;
    
    private Boolean isSuperAdmin;

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

    public String getRole() {
        return role;
    }

    public String getIdCard() {
        return idCard;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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
    
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
    
    public boolean isTeacher() {
        return "TEACHER".equals(this.role);
    }
    
    public boolean isStudent() {
        return "STUDENT".equals(this.role);
    }
}