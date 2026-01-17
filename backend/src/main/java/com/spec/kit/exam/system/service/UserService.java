package com.spec.kit.exam.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.dto.UserDTO;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * Creates a new user with the provided details
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        // Validate input data
        validateUserData(user);

        // Check for duplicate username or email
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Validate password complexity
        if (!passwordUtil.isValidPassword(user.getPasswordHash())) {
            throw new IllegalArgumentException("Password does not meet complexity requirements");
        }

        // Hash the password before storing
        user.setPasswordHash(passwordUtil.hashPassword(user.getPasswordHash()));

        // Set default values
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }

        userMapper.insert(user);
        return user;
    }

    /**
     * Validates user data according to requirements
     * @param user the user to validate
     */
    private void validateUserData(User user) {
        if (user.getUsername() == null || user.getUsername().trim().length() < 3 || user.getUsername().trim().length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }

        if (!isValidUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username can only contain alphanumeric characters, underscores, and hyphens");
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (user.getPhone() != null && !isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone format");
        }

        if (user.getPasswordHash() == null) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    /**
     * Validates username format (alphanumeric, underscore, hyphen only)
     * @param username the username to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Validates email format
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Validates phone number format
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPhone(String phone) {
        if (phone == null) return false;
        // Simple validation for international format: +XX XXXXXXXXXX
        return phone.matches("^\\+[1-9]\\d{1,14}$") || phone.matches("^\\d{10,15}$");
    }

    /**
     * Retrieves all users with optional pagination and status filtering
     * @param page the page number (starting from 1)
     * @param limit the number of records per page
     * @param status optional status filter
     * @return list of users
     */
    public List<User> getUsers(Integer page, Integer limit, String status) {
        // For now, a simple implementation - in a full implementation, we'd use MyBatisPlus pagination
        List<User> allUsers = userMapper.selectList(null);
        
        // Filter by status if provided
        if (status != null && !status.trim().isEmpty()) {
            allUsers = allUsers.stream()
                .filter(user -> user.getStatus().equals(status))
                .collect(Collectors.toList());
        }
        
        // Simple pagination
        int startIndex = (page != null && page > 0) ? (page - 1) * (limit != null ? limit : Integer.MAX_VALUE) : 0;
        int endIndex = Math.min(startIndex + (limit != null ? limit : allUsers.size()), allUsers.size());
        
        if (startIndex >= allUsers.size()) {
            return new ArrayList<>(); // Return empty list if start index is out of bounds
        }
        
        return allUsers.subList(startIndex, endIndex);
    }

    /**
     * Retrieves a user by ID
     * @param id the user ID
     * @return the user if found
     */
    public Optional<User> getUserById(String id) {
        User user = userMapper.selectById(id);
        return Optional.ofNullable(user);
    }

    /**
     * Updates an existing user
     * @param user the user with updated information
     * @return the updated user
     */
    public User updateUser(User user) {
        // Get the existing user to validate changes
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }

        // Validate updated data
        validateUserData(user);

        // Prevent changing certain critical fields for super admin
        if (existingUser.getIsSuperAdmin() != null && existingUser.getIsSuperAdmin()) {
            // Don't allow changing username or email for super admin
            user.setUsername(existingUser.getUsername());
            user.setEmail(existingUser.getEmail());
        }

        // Handle password update separately if needed
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            if (!passwordUtil.isValidPassword(user.getPasswordHash())) {
                throw new IllegalArgumentException("Password does not meet complexity requirements");
            }
            user.setPasswordHash(passwordUtil.hashPassword(user.getPasswordHash()));
            user.setPasswordChangedAt(LocalDateTime.now());
        } else {
            // Keep the old password hash if not updating
            user.setPasswordHash(existingUser.getPasswordHash());
        }

        // Don't allow changing ID or creation date
        user.setId(existingUser.getId());
        user.setCreatedAt(existingUser.getCreatedAt());

        user.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(user);
        return user;
    }

    /**
     * Deletes a user by ID
     * @param id the user ID to delete
     */
    public void deleteUser(String id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        // Prevent deletion of super admin account
        if (user.getIsSuperAdmin() != null && user.getIsSuperAdmin()) {
            throw new IllegalArgumentException("Cannot delete super admin account");
        }

        // In a real implementation, you might want to soft delete or clean up related data
        // For example, removing sessions, notifications, etc.
        userMapper.deleteById(id);
    }

    /**
     * Updates user status (activate/deactivate accounts)
     * @param id the user ID
     * @param status the new status
     * @return the updated user
     */
    public User updateUserStatus(String id, String status) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setStatus(status);
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
        }
        return user;
    }

    /**
     * Finds user by username
     * @param username the username to search for
     * @return the user if found
     */
    public Optional<User> findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    /**
     * Finds user by email
     * @param email the email to search for
     * @return the user if found
     */
    public Optional<User> findByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    /**
     * Finds user by phone
     * @param phone the phone number to search for
     * @return the user if found
     */
    public Optional<User> findByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    /**
     * Assigns a role to a user
     * @param userId the ID of the user
     * @param roleId the ID of the role
     * @return the updated user
     */
    public User assignRoleToUser(String userId, String roleId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Verify that the role exists
        // In a full implementation, you'd call RoleService to verify
        // For now, we'll assume the role exists

        user.setRoleId(roleId);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return user;
    }

    /**
     * Registers a new user with validation
     * @param username the username
     * @param password the password
     * @param email the email
     * @param phone the phone number
     * @return the created user
     */
    public User registerUser(String username, String password, String email, String phone) {
        // Create a new user object
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password); // This will be hashed in createUser
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus("ACTIVE"); // Default to active for registration
        
        return createUser(user);
    }

    /**
     * Removes a role from a user
     * @param userId the ID of the user
     * @return the updated user
     */
    public User removeRoleFromUser(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Prevent removing role from super admin user
        if (user.getIsSuperAdmin() != null && user.getIsSuperAdmin()) {
            throw new IllegalArgumentException("Cannot remove role from super admin user");
        }

        user.setRoleId(null);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return user;
    }

    /**
     * Unlocks a user account (admin functionality)
     * @param userId the ID of the user to unlock
     */
    public void unlockUserAccount(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Reset the lockout status
        user.setLockedUntil(null);
        user.setFailedLoginAttempts(0);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }
}