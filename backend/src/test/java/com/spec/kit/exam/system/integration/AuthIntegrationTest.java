package com.spec.kit.exam.system.integration;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.dto.RefreshTokenRequest;
import com.spec.kit.exam.system.dto.TokenResponse;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.AuthService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.JwtUtilEnhanced;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilEnhanced jwtUtil;

    private String baseUrl;
    private User testUser;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPasswordHash("password123");
        testUser.setEmail("test@example.com");
        testUser.setRole("TEACHER");
    }

    @Test
    void testCompleteAuthFlow() {
        // 1. 注册用户
        ResponseEntity<?> registerResponse = restTemplate.postForEntity(
            baseUrl + "/register",
            testUser,
            Object.class
        );
        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());

        // 2. 登录获取token
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        ResponseEntity<?> loginResponse = restTemplate.postForEntity(
            baseUrl + "/login",
            loginRequest,
            Object.class
        );
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        // 3. 使用token访问受保护资源
        String token = extractTokenFromResponse(loginResponse.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> protectedResponse = restTemplate.exchange(
            "http://localhost:" + port + "/api/users/current",
            HttpMethod.GET,
            entity,
            Object.class
        );
        assertEquals(HttpStatus.OK, protectedResponse.getStatusCode());

        // 4. 刷新token
        String refreshToken = extractRefreshTokenFromResponse(loginResponse.getBody());
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken(refreshToken);

        ResponseEntity<?> refreshResponse = restTemplate.postForEntity(
            baseUrl + "/refresh",
            refreshRequest,
            Object.class
        );
        assertEquals(HttpStatus.OK, refreshResponse.getStatusCode());

        // 5. 登出
        HttpEntity<String> logoutEntity = new HttpEntity<>(headers);
        ResponseEntity<?> logoutResponse = restTemplate.exchange(
            baseUrl + "/logout",
            HttpMethod.POST,
            logoutEntity,
            Object.class
        );
        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());
    }

    @Test
    void testTokenRefreshFlow() {
        // 1. 登录
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        ResponseEntity<?> loginResponse = restTemplate.postForEntity(
            baseUrl + "/login",
            loginRequest,
            Object.class
        );
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        String refreshToken = extractRefreshTokenFromResponse(loginResponse.getBody());

        // 2. 刷新token
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken(refreshToken);

        ResponseEntity<?> refreshResponse = restTemplate.postForEntity(
            baseUrl + "/refresh",
            refreshRequest,
            Object.class
        );
        assertEquals(HttpStatus.OK, refreshResponse.getStatusCode());

        // 3. 验证新token有效
        String newAccessToken = extractTokenFromResponse(refreshResponse.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(newAccessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> protectedResponse = restTemplate.exchange(
            "http://localhost:" + port + "/api/users/current",
            HttpMethod.GET,
            entity,
            Object.class
        );
        assertEquals(HttpStatus.OK, protectedResponse.getStatusCode());
    }

    @Test
    void testInvalidTokenAccess() {
        // 使用无效token访问受保护资源
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid.token.here");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/users/current",
            HttpMethod.GET,
            entity,
            Object.class
        );
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testExpiredTokenRefresh() {
        // 模拟过期token刷新
        String expiredRefreshToken = jwtUtil.generateRefreshToken(testUser);
        
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken(expiredRefreshToken);

        ResponseEntity<?> response = restTemplate.postForEntity(
            baseUrl + "/refresh",
            refreshRequest,
            Object.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private String extractTokenFromResponse(Object responseBody) {
        // 简化的token提取逻辑
        if (responseBody instanceof java.util.Map) {
            java.util.Map<?, ?> map = (java.util.Map<?, ?>) responseBody;
            Object data = map.get("data");
            if (data instanceof java.util.Map) {
                return (String) ((java.util.Map<?, ?>) data).get("accessToken");
            }
        }
        return null;
    }

    private String extractRefreshTokenFromResponse(Object responseBody) {
        if (responseBody instanceof java.util.Map) {
            java.util.Map<?, ?> map = (java.util.Map<?, ?>) responseBody;
            Object data = map.get("data");
            if (data instanceof java.util.Map) {
                return (String) ((java.util.Map<?, ?>) data).get("refreshToken");
            }
        }
        return null;
    }
}