package com.spec.kit.exam.system.integration;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.service.AuthService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.JwtUtil;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthIntegrationTest {

    @Mock
    private UserService userService;

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

    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        authService = new AuthService();
        
        // 使用反射注入mock依赖
        setField(authService, "userService", userService);
        setField(authService, "userMapper", userMapper);
        setField(authService, "passwordUtil", passwordUtil);
        setField(authService, "jwtUtil", jwtUtil);
        setField(authService, "redisTemplate", redisTemplate);
        
        // Mock RedisTemplate的opsForValue方法
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPasswordHash("hashed_password");
        testUser.setEmail("test@example.com");
        testUser.setRole("TEACHER");
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testAuthenticationSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordUtil.validatePassword("password123", "hashed_password")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("test_token");

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("password123");

        AuthService.AuthResult result = authService.authenticate(loginRequest);
        
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Login successful", result.getMessage());
        assertEquals("test_token", result.getToken());
        assertEquals(testUser, result.getUser());

        verify(userService).findByUsername("testuser");
        verify(passwordUtil).validatePassword("password123", "hashed_password");
        verify(jwtUtil).generateToken("testuser");
    }

    @Test
    void testAuthenticationFailure() {
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("nonexistent");
        loginRequest.setPassword("wrongpassword");

        AuthService.AuthResult result = authService.authenticate(loginRequest);
        
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Invalid credentials", result.getMessage());
        assertNull(result.getToken());
        assertNull(result.getUser());

        verify(userService).findByUsername("nonexistent");
    }

    @Test
    void testLogout() {
        when(jwtUtil.getUsernameFromToken("test_token")).thenReturn("testuser");
        
        authService.logout("test_token");
        
        verify(jwtUtil).getUsernameFromToken("test_token");
    }

    @Test
    void testAccountUnlock() {
        when(userMapper.selectById("user123")).thenReturn(testUser);
        
        authService.unlockAccount("user123");
        
        verify(userMapper).selectById("user123");
        verify(userMapper).updateById(testUser);
    }
}