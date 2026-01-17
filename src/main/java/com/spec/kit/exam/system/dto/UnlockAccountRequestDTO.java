package com.spec.kit.exam.system.dto;

public class UnlockAccountRequestDTO {
    private String userId; // ID of the user account to unlock
    private String unlockedBy; // ID of the admin user performing the unlock
    private String reason; // Optional reason for unlocking the account

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUnlockedBy() {
        return unlockedBy;
    }

    public String getReason() {
        return reason;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUnlockedBy(String unlockedBy) {
        this.unlockedBy = unlockedBy;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}