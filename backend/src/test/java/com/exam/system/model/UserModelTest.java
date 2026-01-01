package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
    }

    @Test
    void testIdAccessors() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void testUsernameAccessors() {
        String username = "testuser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void testEmailAccessors() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void testPasswordHashAccessors() {
        String passwordHash = "hashedPassword";
        user.setPasswordHash(passwordHash);
        assertEquals(passwordHash, user.getPasswordHash());
    }

    @Test
    void testFirstNameAccessors() {
        String firstName = "John";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void testLastNameAccessors() {
        String lastName = "Doe";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void testRoleAccessors() {
        User.Role role = User.Role.STUDENT;
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testIsActiveAccessors() {
        user.setIsActive(true);
        assertTrue(user.getIsActive());
        
        user.setIsActive(false);
        assertFalse(user.getIsActive());
    }

    @Test
    void testCreatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void testUpdatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testUserConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
        assertTrue(newUser.getIsActive());
        assertNotNull(newUser.getCreatedAt());
    }

    @Test
    void testRoleEnumValues() {
        User.Role[] roles = User.Role.values();
        assertEquals(3, roles.length);
        assertTrue(java.util.Arrays.asList(roles).contains(User.Role.STUDENT));
        assertTrue(java.util.Arrays.asList(roles).contains(User.Role.TEACHER));
        assertTrue(java.util.Arrays.asList(roles).contains(User.Role.ADMIN));
    }
}