package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.entity.Session;
import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.util.JwtUtil;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserService userService;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 30; // 30 minutes lockout

    /**
     * Authenticates user with username, email, or phone and password
     * @param loginRequest containing identifier and password
     * @return authentication result with JWT token
     */
    public AuthResult authenticate(LoginRequestDTO loginRequest) {
        String identifier = loginRequest.getIdentifier();
        String password = loginRequest.getPassword();

        // Find user by identifier (username, email, or phone)
        User user = findUserByIdentifier(identifier);
        if (user == null) {
            // Still increment failed attempts in Redis to prevent enumeration attacks
            incrementFailedAttemptsInRedis(identifier);
            return new AuthResult(false, "Invalid credentials", null, null);
        }

        // Check if account is locked
        if (isAccountLocked(user)) {
            return new AuthResult(false, "ACCOUNT_LOCKED: Account is locked due to multiple failed login attempts", null, null);
        }

        // Validate password
        if (passwordUtil.validatePassword(password, user.getPasswordHash())) {
            // Reset failed attempts on successful login
            resetFailedAttempts(user);
            
            // Update last login time
            user.setLastLoginAt(LocalDateTime.now());
            userMapper.updateById(user);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());
            
            return new AuthResult(true, "Login successful", token, user);
        } else {
            // Increment failed attempts (both Redis and DB)
            incrementFailedAttempts(user);
            
            // Check if this makes it the 3rd failed attempt
            int failedAttempts = user.getFailedLoginAttempts();
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                // Lock the account
                lockAccount(user);
                return new AuthResult(false, "ACCOUNT_LOCKED: Account locked due to multiple failed login attempts", null, null);
            }
            
            return new AuthResult(false, "Invalid credentials. Attempts: " + failedAttempts + "/" + MAX_FAILED_ATTEMPTS, null, null);
        }
    }

    /**
     * Logs out the user by invalidating the session
     * @param token the JWT token to invalidate
     */
    public void logout(String token) {
        // In a real implementation, you might want to store active tokens in Redis
        // and invalidate them upon logout
        // For now, we'll just return as the token will expire naturally
        // You could extract the username from the token and add it to a blacklist
        String username = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        String logoutKey = "logout_token:" + token;
        // Store the token in Redis with expiration time matching the token's TTL
        // We'll use the default expiration time from configuration for now
        redisTemplate.opsForValue().set(logoutKey, true, LOCKOUT_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Finds user by identifier (username, email, or phone)
     * @param identifier the identifier to search for
     * @return the user if found, null otherwise
     */
    private User findUserByIdentifier(String identifier) {
        // Try to find by username first
        User user = new User();
        user.setUsername(identifier);
        // In a real implementation, you'd use proper queries
        // For now, we'll use the existing methods from UserService
        java.util.Optional<User> userOpt = userService.findByUsername(identifier);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // Try to find by email
        userOpt = userService.findByEmail(identifier);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // Try to find by phone
        userOpt = userService.findByPhone(identifier);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        return null;
    }

    /**
     * Increments the failed login attempts for the user (both Redis and DB)
     * @param user the user to track
     */
    private void incrementFailedAttempts(User user) {
        // Update Redis
        incrementFailedAttemptsInRedis(user.getUsername());
        
        // Update database
        Integer dbAttempts = user.getFailedLoginAttempts();
        if (dbAttempts == null) {
            dbAttempts = 0;
        }
        user.setFailedLoginAttempts(dbAttempts + 1);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /**
     * Increments failed attempts only in Redis (for non-existent users)
     * @param identifier the identifier to track
     */
    private void incrementFailedAttemptsInRedis(String identifier) {
        String key = "login_failed_attempts:" + identifier;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        redisTemplate.opsForValue().set(key, attempts, LOCKOUT_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Gets the current failed login attempts for the given identifier from Redis
     * @param identifier the identifier to check
     * @return the number of failed attempts
     */
    private int getFailedAttempts(String identifier) {
        String key = "login_failed_attempts:" + identifier;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts != null ? attempts : 0;
    }

    /**
     * Resets the failed login attempts for the user (both Redis and DB)
     * @param user the user to reset
     */
    private void resetFailedAttempts(User user) {
        // Clear Redis
        redisTemplate.delete("login_failed_attempts:" + user.getUsername());
        if (user.getEmail() != null) {
            redisTemplate.delete("login_failed_attempts:" + user.getEmail());
        }
        if (user.getPhone() != null) {
            redisTemplate.delete("login_failed_attempts:" + user.getPhone());
        }
        
        // Reset database field
        user.setFailedLoginAttempts(0);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /**
     * Checks if the account is locked
     * @param user the user to check
     * @return true if locked, false otherwise
     */
    private boolean isAccountLocked(User user) {
        // Check if the lockedUntil time is in the future
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            return true;
        }

        // Check Redis for failed attempts
        int failedAttempts = getFailedAttempts(user.getUsername());
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            return true;
        }

        // Also check by email and phone
        failedAttempts = getFailedAttempts(user.getEmail());
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            return true;
        }

        if (user.getPhone() != null) {
            failedAttempts = getFailedAttempts(user.getPhone());
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                return true;
            }
        }

        return false;
    }

    /**
     * Locks the account by setting lockedUntil time
     * @param user the user to lock
     */
    private void lockAccount(User user) {
        user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
        userMapper.updateById(user);
    }

    /**
     * Unlocks an account
     * @param userId the ID of the user to unlock
     */
    public void unlockAccount(String userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setLockedUntil(null);
            user.setFailedLoginAttempts(0);
            userMapper.updateById(user);

            // Clear any Redis entries for this user
            clearFailedAttemptsInRedis(user);
        }
    }

    /**
     * Clears failed attempts in Redis for a user
     * @param user the user to clear
     */
    private void clearFailedAttemptsInRedis(User user) {
        redisTemplate.delete("login_failed_attempts:" + user.getUsername());
        if (user.getEmail() != null) {
            redisTemplate.delete("login_failed_attempts:" + user.getEmail());
        }
        if (user.getPhone() != null) {
            redisTemplate.delete("login_failed_attempts:" + user.getPhone());
        }
    }

    /**
     * Result class for authentication operations
     */
    public static class AuthResult {
        private boolean success;
        private String message;
        private String token;
        private User user;

        public AuthResult(boolean success, String message, String token, User user) {
            this.success = success;
            this.message = message;
            this.token = token;
            this.user = user;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getToken() { return token; }
        public User getUser() { return user; }
    }
}