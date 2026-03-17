package com.spec.kit.exam.system.util;

import com.spec.kit.exam.system.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilEnhancedTest {

    @InjectMocks
    private JwtUtilEnhanced jwtUtilEnhanced;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User();
        testUser.setId("123");
        testUser.setUsername("testuser");
        testUser.setRole("TEACHER");
        testUser.setEmail("test@example.com");
        testUser.setCreatedAt(LocalDateTime.now());
        
        ReflectionTestUtils.setField(jwtUtilEnhanced, "secret", "testSecretKeyForJWTThatIsLongEnoughToMeetHS512RequirementsOfAtLeast64Bytes1234567890123456789012345678901234");
        ReflectionTestUtils.setField(jwtUtilEnhanced, "expiration", 3600000L);
        ReflectionTestUtils.setField(jwtUtilEnhanced, "refreshExpiration", 604800000L);
    }

    @Test
    void testGenerateAccessToken() {
        String accessToken = jwtUtilEnhanced.generateAccessToken(testUser);
        
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        
        Claims claims = jwtUtilEnhanced.parseToken(accessToken);
        assertEquals("123", claims.get("userId"));
        assertEquals("testuser", claims.get("username"));
        assertEquals("TEACHER", claims.get("role"));
        assertEquals("access", claims.get("type"));
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtUtilEnhanced.generateRefreshToken(testUser);
        
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        
        Claims claims = jwtUtilEnhanced.parseToken(refreshToken);
        assertEquals("123", claims.get("userId"));
        assertEquals("refresh", claims.get("type"));
        assertNull(claims.get("username"));
        assertNull(claims.get("role"));
    }

    @Test
    void testParseToken() {
        String token = jwtUtilEnhanced.generateAccessToken(testUser);
        Claims claims = jwtUtilEnhanced.parseToken(token);
        
        assertNotNull(claims);
        assertEquals("123", claims.get("userId"));
        assertEquals("testuser", claims.get("username"));
        assertEquals("TEACHER", claims.get("role"));
    }

    @Test
    void testIsTokenExpired() {
        String token = jwtUtilEnhanced.generateAccessToken(testUser);
        assertFalse(jwtUtilEnhanced.isTokenExpired(token));
    }

    @Test
    void testGetUserIdFromToken() {
        String token = jwtUtilEnhanced.generateAccessToken(testUser);
        Long userId = jwtUtilEnhanced.getUserIdFromToken(token);
        
        assertEquals(123L, userId);
    }

    @Test
    void testDifferentTokenTypes() {
        String accessToken = jwtUtilEnhanced.generateAccessToken(testUser);
        String refreshToken = jwtUtilEnhanced.generateRefreshToken(testUser);
        
        Claims accessClaims = jwtUtilEnhanced.parseToken(accessToken);
        Claims refreshClaims = jwtUtilEnhanced.parseToken(refreshToken);
        
        assertEquals("access", accessClaims.get("type"));
        assertEquals("refresh", refreshClaims.get("type"));
        
        assertNotNull(accessClaims.get("username"));
        assertNotNull(accessClaims.get("role"));
        assertNull(refreshClaims.get("username"));
        assertNull(refreshClaims.get("role"));
    }
}