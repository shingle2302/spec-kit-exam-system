package com.spec.kit.exam.system.dto;

public class LoginRequestDTO {
    private String identifier; // User identifier - can be username, email, or phone number
    private String password;   // Password for authentication

    // Getters
    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}