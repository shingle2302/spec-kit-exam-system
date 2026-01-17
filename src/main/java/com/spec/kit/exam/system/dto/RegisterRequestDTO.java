package com.spec.kit.exam.system.dto;

public class RegisterRequestDTO {
    private String username; // String (Required, 3-50 chars)
    private String password; // String (Required, min 8 chars with complexity)
    private String email;    // String (Required, valid email format)
    private String phone;    // String (Required, validated format)

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}