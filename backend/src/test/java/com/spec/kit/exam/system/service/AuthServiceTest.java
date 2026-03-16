package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.util.JwtUtil;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // Set up mock behavior for RedisTemplate
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(valueOperations.get(anyString())).thenReturn(null);
    }

    @Test
    void authenticateShouldReturnSuccessForValidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("Password123!");

        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed_password");
        user.setFailedLoginAttempts(0);

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.validatePassword("Password123!", "hashed_password")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("test_token");

        // Act
        AuthService.AuthResult result = authService.authenticate(loginRequest);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Login successful", result.getMessage());
        assertEquals("test_token", result.getToken());
        assertNotNull(result.getUser());
    }

    @Test
    void authenticateShouldReturnFailureForInvalidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("wrongpassword");

        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setPasswordHash("hashed_password");
        user.setFailedLoginAttempts(0);

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.validatePassword("wrongpassword", "hashed_password")).thenReturn(false);

        // Act
        AuthService.AuthResult result = authService.authenticate(loginRequest);

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Invalid credentials"));
        assertNull(result.getToken());
        assertNull(result.getUser());
    }

    @Test
    void authenticateShouldReturnFailureForLockedAccount() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("Password123!");

        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setPasswordHash("hashed_password");
        user.setLockedUntil(LocalDateTime.now().plusHours(1));

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        AuthService.AuthResult result = authService.authenticate(loginRequest);

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("ACCOUNT_LOCKED"));
        assertNull(result.getToken());
        assertNull(result.getUser());
    }

    @Test
    void authenticateShouldReturnFailureForNonExistentUser() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("nonexistent");
        loginRequest.setPassword("Password123!");

        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());
        when(userService.findByEmail("nonexistent")).thenReturn(Optional.empty());
        when(userService.findByPhone("nonexistent")).thenReturn(Optional.empty());

        // Act
        AuthService.AuthResult result = authService.authenticate(loginRequest);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Invalid credentials", result.getMessage());
        assertNull(result.getToken());
        assertNull(result.getUser());
    }

    @Test
    void unlockAccountShouldResetLockoutStatus() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setLockedUntil(LocalDateTime.now().plusHours(1));
        user.setFailedLoginAttempts(3);

        when(userMapper.selectById("1")).thenReturn(user);

        // Act
        authService.unlockAccount("1");

        // Assert
        assertNull(user.getLockedUntil());
        assertEquals(0, user.getFailedLoginAttempts());
        verify(userMapper).updateById(user);
    }
}