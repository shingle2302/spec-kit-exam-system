package com.spec.kit.exam.system.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Static instance for static method access
    private static PasswordUtil instance;
    
    public PasswordUtil() {
        instance = this;
    }

    /**
     * Hashes the provided plaintext password
     * @param plainPassword the plaintext password to hash
     * @return the hashed password
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    
    /**
     * Static method to hash password
     * @param plainPassword the plaintext password to hash
     * @return the hashed password
     */
    public static String hashPasswordStatic(String plainPassword) {
        return instance.passwordEncoder.encode(plainPassword);
    }

    /**
     * Validates a plaintext password against a hashed password
     * @param plainPassword the plaintext password to validate
     * @param hashedPassword the hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean validatePassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    /**
     * Checks if the password meets the required complexity:
     * - Minimum 8 characters
     * - Contains uppercase letter
     * - Contains lowercase letter
     * - Contains number
     * - Contains special character
     *
     * @param password the password to validate
     * @return true if password meets complexity requirements, false otherwise
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}