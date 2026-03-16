package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.dto.RegisterRequestDTO;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.AuthService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerShouldReturnSuccess() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Password123!");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPhone("1234567890");

        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userService.registerUser("testuser", "Password123!", "test@example.com", "1234567890")).thenReturn(user);

        // Act
        Result<?> result = authController.register(registerRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User registered successfully", result.getMsg());
        assertEquals(user, result.getData());
    }

    @Test
    void registerShouldReturnErrorWhenRegistrationFails() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Password123!");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPhone("1234567890");

        when(userService.registerUser("testuser", "Password123!", "test@example.com", "1234567890"))
                .thenThrow(new RuntimeException("Registration failed"));

        // Act
        Result<?> result = authController.register(registerRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("Registration failed"));
    }

    @Test
    void loginShouldReturnSuccessWithToken() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("Password123!");

        User user = new User();
        user.setId("1");
        user.setUsername("testuser");

        AuthService.AuthResult authResult = new AuthService.AuthResult(true, "Login successful", "test-token", user);
        when(authService.authenticate(loginRequest)).thenReturn(authResult);

        // Act
        Result<Map<String, Object>> result = authController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Login successful", result.getMsg());
        assertNotNull(result.getData());
        assertEquals("test-token", result.getData().get("accessToken"));
        assertEquals(user, result.getData().get("user"));
    }

    @Test
    void loginShouldReturnErrorForLockedAccount() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("Password123!");

        AuthService.AuthResult authResult = new AuthService.AuthResult(false, "ACCOUNT_LOCKED: Account is locked", null, null);
        when(authService.authenticate(loginRequest)).thenReturn(authResult);

        // Act
        Result<Map<String, Object>> result = authController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("ACCOUNT_LOCKED"));
    }

    @Test
    void loginShouldReturnUnauthorizedForInvalidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("testuser");
        loginRequest.setPassword("wrongpassword");

        AuthService.AuthResult authResult = new AuthService.AuthResult(false, "Invalid credentials", null, null);
        when(authService.authenticate(loginRequest)).thenReturn(authResult);

        // Act
        Result<Map<String, Object>> result = authController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("Invalid credentials"));
    }

    @Test
    void logoutShouldReturnSuccess() {
        // Arrange
        String token = "Bearer test-token";

        // Act
        Result<?> result = authController.logout(token);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Logged out successfully", result.getMsg());
        verify(authService).logout(token);
    }

    @Test
    void logoutShouldReturnErrorWhenLogoutFails() {
        // Arrange
        String token = "Bearer test-token";

        doThrow(new RuntimeException("Logout failed")).when(authService).logout(token);

        // Act
        Result<?> result = authController.logout(token);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("Logout failed"));
    }
}