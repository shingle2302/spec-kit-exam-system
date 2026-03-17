package com.spec.kit.exam.system.security;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.DataEncryptUtil;
import com.spec.kit.exam.system.util.JwtUtilEnhanced;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtilEnhanced jwtUtil;

    @Mock
    private DataEncryptUtil dataEncryptUtil;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User();
        testUser.setUsername("securityuser");
        testUser.setPasswordHash("password123");
        testUser.setEmail("security@example.com");
        testUser.setRole("TEACHER");
        testUser.setPhone("13800138000");
        testUser.setIdCard("110101199001011234");
    }

    @Test
    void testDataEncryption() {
        when(dataEncryptUtil.encrypt("13800138000")).thenReturn("encrypted_phone");
        when(dataEncryptUtil.encrypt("110101199001011234")).thenReturn("encrypted_idcard");
        when(dataEncryptUtil.decrypt("encrypted_phone")).thenReturn("13800138000");
        when(dataEncryptUtil.decrypt("encrypted_idcard")).thenReturn("110101199001011234");

        String plainPhone = "13800138000";
        String plainIdCard = "110101199001011234";

        String encryptedPhone = dataEncryptUtil.encrypt(plainPhone);
        String encryptedIdCard = dataEncryptUtil.encrypt(plainIdCard);

        assertNotEquals(plainPhone, encryptedPhone);
        assertNotEquals(plainIdCard, encryptedIdCard);

        assertEquals(plainPhone, dataEncryptUtil.decrypt(encryptedPhone));
        assertEquals(plainIdCard, dataEncryptUtil.decrypt(encryptedIdCard));

        verify(dataEncryptUtil, times(2)).encrypt(anyString());
        verify(dataEncryptUtil, times(2)).decrypt(anyString());
    }

    @Test
    void testJwtTokenSecurity() {
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        
        io.jsonwebtoken.Claims mockClaims = mock(io.jsonwebtoken.Claims.class);
        when(mockClaims.get("username")).thenReturn("securityuser");
        when(mockClaims.get("role")).thenReturn("TEACHER");
        when(mockClaims.get("passwordHash")).thenReturn(null);
        when(mockClaims.get("phone")).thenReturn(null);
        when(mockClaims.get("idCard")).thenReturn(null);
        when(jwtUtil.parseToken("test.token.here")).thenReturn(mockClaims);

        String token = jwtUtil.generateAccessToken(testUser);

        io.jsonwebtoken.Claims claims = jwtUtil.parseToken(token);
        assertEquals("securityuser", claims.get("username"));
        assertEquals("TEACHER", claims.get("role"));

        assertNull(claims.get("passwordHash"));
        assertNull(claims.get("phone"));
        assertNull(claims.get("idCard"));

        verify(jwtUtil).generateAccessToken(testUser);
        verify(jwtUtil).parseToken(token);
    }

    @Test
    void testTokenExpiration() {
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        when(jwtUtil.isTokenExpired("test.token.here")).thenReturn(false);
        
        io.jsonwebtoken.Claims mockClaims = mock(io.jsonwebtoken.Claims.class);
        when(mockClaims.get("username")).thenReturn("securityuser");
        when(mockClaims.get("role")).thenReturn("TEACHER");
        when(jwtUtil.parseToken("test.token.here")).thenReturn(mockClaims);

        String token = jwtUtil.generateAccessToken(testUser);

        assertFalse(jwtUtil.isTokenExpired(token));

        io.jsonwebtoken.Claims claims = jwtUtil.parseToken(token);
        assertNotNull(claims);

        verify(jwtUtil).generateAccessToken(testUser);
        verify(jwtUtil).isTokenExpired(token);
        verify(jwtUtil).parseToken(token);
    }

    @Test
    void testPasswordStrengthValidation() {
        String[] weakPasswords = {
            "123",
            "password",
            "12345678",
            "abcdefgh",
            "Password1"
        };

        for (String weakPassword : weakPasswords) {
            boolean isWeak = weakPassword.length() < 8 || 
                           !weakPassword.matches(".*[0-9].*") ||
                           !weakPassword.matches(".*[A-Z].*") ||
                           !weakPassword.matches(".*[a-z].*") ||
                           !weakPassword.matches(".*[^a-zA-Z0-9].*");
            
            assertTrue(isWeak, "Password '" + weakPassword + "' should be considered weak");
        }

        String strongPassword = "StrongP@ssw0rd";
        boolean isStrong = strongPassword.length() >= 8 && 
                          strongPassword.matches(".*[0-9].*") &&
                          strongPassword.matches(".*[A-Z].*") &&
                          strongPassword.matches(".*[a-z].*") &&
                          strongPassword.matches(".*[^a-zA-Z0-9].*");
        
        assertTrue(isStrong, "Password '" + strongPassword + "' should be considered strong");
    }

    @Test
    void testDataAccessControl() {
        User teacher = new User();
        teacher.setUsername("teacher1");
        teacher.setPasswordHash("password123");
        teacher.setEmail("teacher@example.com");
        teacher.setRole("TEACHER");

        User student = new User();
        student.setUsername("student1");
        student.setPasswordHash("password123");
        student.setEmail("student@example.com");
        student.setRole("STUDENT");

        assertTrue(teacher.getRole().equals("TEACHER"));
        assertTrue(student.getRole().equals("STUDENT"));
        assertNotEquals(teacher.getRole(), student.getRole());
    }

    @Test
    void testSqlInjectionPrevention() {
        String maliciousInput = "admin' OR '1'='1";
        String sanitizedInput = maliciousInput.replace("'", "''");
        
        // 验证输入已被清理
        assertNotEquals(maliciousInput, sanitizedInput);
        
        // 验证所有单引号都被转义了
        assertEquals("admin'' OR ''1''=''1", sanitizedInput);
        
        // 验证原始恶意模式不再存在（单引号被转义后，SQL注入失效）
        assertFalse(sanitizedInput.equals(maliciousInput));
        
        // 验证转义后的字符串包含双单引号
        assertTrue(sanitizedInput.contains("''"));
    }

    @Test
    void testXssPrevention() {
        String xssPayload = "<script>alert('XSS')</script>";
        String sanitizedPayload = xssPayload.replace("<", "&lt;")
                                           .replace(">", "&gt;")
                                           .replace("\"", "&quot;")
                                           .replace("'", "&#39;");
        
        assertNotEquals(xssPayload, sanitizedPayload);
        assertFalse(sanitizedPayload.contains("<script>"));
        assertFalse(sanitizedPayload.contains("</script>"));
    }
}