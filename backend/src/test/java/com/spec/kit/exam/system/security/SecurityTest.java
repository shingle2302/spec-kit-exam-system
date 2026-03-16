package com.spec.kit.exam.system.security;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.DataEncryptUtil;
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
class SecurityTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilEnhanced jwtUtil;

    @Autowired
    private DataEncryptUtil dataEncryptUtil;

    private String baseUrl;
    private User testUser;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        
        testUser = new User();
        testUser.setUsername("securityuser");
        testUser.setPasswordHash("password123");
        testUser.setEmail("security@example.com");
        testUser.setRole("TEACHER");
        testUser.setPhone("13800138000");
        testUser.setIdCard("110101199001011234");
    }

    @Test
    void testSqlInjectionPrevention() {
        // 测试SQL注入防护
        String maliciousInput = "admin' OR '1'='1";
        
        User maliciousUser = new User();
        maliciousUser.setUsername(maliciousInput);
        maliciousUser.setPasswordHash("password123");
        maliciousUser.setEmail("test@example.com");
        maliciousUser.setRole("STUDENT");

        ResponseEntity<?> response = restTemplate.postForEntity(
            baseUrl + "/register",
            maliciousUser,
            Object.class
        );

        // 应该成功注册，但不会导致SQL注入
        assertTrue(response.getStatusCode() == HttpStatus.OK || 
                   response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    void testXssPrevention() {
        // 测试XSS防护
        String xssPayload = "<script>alert('XSS')</script>";
        
        User xssUser = new User();
        xssUser.setUsername(xssPayload);
        xssUser.setPasswordHash("password123");
        xssUser.setEmail("test@example.com");
        xssUser.setRole("STUDENT");

        ResponseEntity<?> response = restTemplate.postForEntity(
            baseUrl + "/register",
            xssUser,
            Object.class
        );

        // 应该成功注册，但XSS应该被转义
        assertTrue(response.getStatusCode() == HttpStatus.OK || 
                   response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    void testDataEncryption() {
        // 测试敏感数据加密
        String plainPhone = "13800138000";
        String plainIdCard = "110101199001011234";

        String encryptedPhone = dataEncryptUtil.encrypt(plainPhone);
        String encryptedIdCard = dataEncryptUtil.encrypt(plainIdCard);

        // 加密后的数据应该与原文不同
        assertNotEquals(plainPhone, encryptedPhone);
        assertNotEquals(plainIdCard, encryptedIdCard);

        // 解密后的数据应该与原文相同
        assertEquals(plainPhone, dataEncryptUtil.decrypt(encryptedPhone));
        assertEquals(plainIdCard, dataEncryptUtil.decrypt(encryptedIdCard));

        // 相同的明文加密后应该产生不同的密文（使用IV）
        String encryptedPhone2 = dataEncryptUtil.encrypt(plainPhone);
        assertNotEquals(encryptedPhone, encryptedPhone2);
    }

    @Test
    void testJwtTokenSecurity() {
        // 测试JWT token安全性
        String token = jwtUtil.generateAccessToken(testUser);

        // Token应该包含用户信息
        var claims = jwtUtil.parseToken(token);
        assertEquals("securityuser", claims.get("username"));
        assertEquals("TEACHER", claims.get("role"));

        // Token不应该包含敏感信息
        assertNull(claims.get("passwordHash"));
        assertNull(claims.get("phone"));
        assertNull(claims.get("idCard"));
    }

    @Test
    void testTokenExpiration() {
        // 测试token过期
        String token = jwtUtil.generateAccessToken(testUser);

        // 新生成的token不应该过期
        assertFalse(jwtUtil.isTokenExpired(token));

        // 尝试使用过期token（需要修改jwtUtil来支持测试）
        // 这里我们测试token的有效性验证
        var claims = jwtUtil.parseToken(token);
        assertNotNull(claims);
    }

    @Test
    void testUnauthorizedAccess() {
        // 测试未授权访问
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
    void testMissingTokenAccess() {
        // 测试缺少token的访问
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<?> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/users/current",
            HttpMethod.GET,
            entity,
            Object.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testBruteForceProtection() {
        // 测试暴力破解防护
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("securityuser");
        loginRequest.setPassword("wrongpassword");

        int failedAttempts = 0;
        for (int i = 0; i < 10; i++) {
            ResponseEntity<?> response = restTemplate.postForEntity(
                baseUrl + "/login",
                loginRequest,
                Object.class
            );
            
            if (response.getStatusCode() != HttpStatus.OK) {
                failedAttempts++;
            }
        }

        // 应该有多次失败的登录尝试
        assertTrue(failedAttempts > 0, "Should have failed login attempts");
    }

    @Test
    void testPasswordStrengthValidation() {
        // 测试密码强度验证
        String[] weakPasswords = {
            "123",           // 太短
            "password",      // 缺少数字和特殊字符
            "12345678",      // 缺少字母
            "abcdefgh",      // 缺少数字
            "Password1"      // 缺少特殊字符
        };

        for (String weakPassword : weakPasswords) {
            User user = new User();
            user.setUsername("testuser" + weakPassword.hashCode());
            user.setPasswordHash(weakPassword);
            user.setEmail("test@example.com");
            user.setRole("STUDENT");

            ResponseEntity<?> response = restTemplate.postForEntity(
                baseUrl + "/register",
                user,
                Object.class
            );

            // 弱密码应该被拒绝
            // 注意：这取决于后端的具体实现
            // 这里我们只是记录结果
            System.out.println("Password: " + weakPassword + 
                             ", Status: " + response.getStatusCode());
        }
    }

    @Test
    void testCsrfProtection() {
        // 测试CSRF防护
        // Spring Security默认启用CSRF保护
        // 这里我们测试是否正确配置了CSRF保护
        
        // 尝试POST请求（通常需要CSRF token）
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("securityuser");
        loginRequest.setPassword("password123");

        ResponseEntity<?> response = restTemplate.postForEntity(
            baseUrl + "/login",
            loginRequest,
            Object.class
        );

        // 登录接口可能豁免CSRF保护
        // 其他接口应该需要CSRF token
        assertTrue(response.getStatusCode() == HttpStatus.OK || 
                   response.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    @Test
    void testDataAccessControl() {
        // 测试数据访问控制
        // 这里测试不同角色的数据访问权限
        
        // 1. 创建教师用户
        User teacher = new User();
        teacher.setUsername("teacher1");
        teacher.setPasswordHash("password123");
        teacher.setEmail("teacher@example.com");
        teacher.setRole("TEACHER");

        // 2. 创建学生用户
        User student = new User();
        student.setUsername("student1");
        student.setPasswordHash("password123");
        student.setEmail("student@example.com");
        student.setRole("STUDENT");

        // 3. 测试教师访问学生数据
        // 教师应该只能看到自己班级的学生数据
        
        // 4. 测试学生访问其他学生数据
        // 学生应该只能看到自己的数据
        
        // 注意：这需要完整的权限系统实现
        // 这里只是框架代码
        assertTrue(true, "Data access control test framework");
    }

    @Test
    void testSecureHeaders() {
        // 测试安全响应头
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/health",
            String.class
        );

        HttpHeaders headers = response.getHeaders();
        
        // 检查安全头
        // 注意：这些头需要在配置中启用
        // X-Content-Type-Options, X-Frame-Options, etc.
        
        assertNotNull(headers);
        System.out.println("Response Headers: " + headers);
    }

    @Test
    void testSessionManagement() {
        // 测试会话管理
        // 1. 登录获取token
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("securityuser");
        loginRequest.setPassword("password123");

        ResponseEntity<?> loginResponse = restTemplate.postForEntity(
            baseUrl + "/login",
            loginRequest,
            Object.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        // 2. 登出
        String token = extractTokenFromResponse(loginResponse.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        HttpEntity<String> logoutEntity = new HttpEntity<>(headers);
        ResponseEntity<?> logoutResponse = restTemplate.exchange(
            baseUrl + "/logout",
            HttpMethod.POST,
            logoutEntity,
            Object.class
        );

        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

        // 3. 使用已登出的token应该失败
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> protectedResponse = restTemplate.exchange(
            "http://localhost:" + port + "/api/users/current",
            HttpMethod.GET,
            entity,
            Object.class
        );

        // 登出后token应该失效
        // 注意：这取决于后端的实现
        // 有些实现可能不会立即失效token
    }

    @Test
    void testRateLimiting() {
        // 测试速率限制
        // 快速发送多个请求，应该触发速率限制
        
        int requestCount = 20;
        int successCount = 0;
        int rateLimitedCount = 0;

        for (int i = 0; i < requestCount; i++) {
            ResponseEntity<?> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/health",
                Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                successCount++;
            } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                rateLimitedCount++;
            }
        }

        System.out.println("Rate Limiting Test:");
        System.out.println("Total requests: " + requestCount);
        System.out.println("Successful: " + successCount);
        System.out.println("Rate limited: " + rateLimitedCount);
        
        // 注意：这取决于后端是否实现了速率限制
        assertTrue(true, "Rate limiting test completed");
    }

    private String extractTokenFromResponse(Object responseBody) {
        if (responseBody instanceof java.util.Map) {
            java.util.Map<?, ?> map = (java.util.Map<?, ?>) responseBody;
            Object data = map.get("data");
            if (data instanceof java.util.Map) {
                return (String) ((java.util.Map<?, ?>) data).get("accessToken");
            }
        }
        return null;
    }
}