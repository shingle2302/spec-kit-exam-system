package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("sessions")
public class Session {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id; // UUID (Primary Key, auto-generated)
    
    private String userId; // String (Foreign Key to User, required)
    
    private String sessionId; // String (Unique, randomly generated)
    
    private String jwtToken; // String (Encrypted JWT token)
    
    private String refreshToken; // String (Encrypted refresh token)
    
    private LocalDateTime expiresAt; // LocalDateTime (Calculated: 30 minutes from last activity)
    
    private LocalDateTime lastActivityAt; // LocalDateTime (Updated on each activity)
    
    private String ipAddress; // String (Recorded on creation)
    
    private String userAgent; // String (Browser/device info)
    
    private LocalDateTime createdAt; // LocalDateTime (Auto-generated)

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}