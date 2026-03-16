package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserShouldSuccessfullyCreateUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("Password123!");

        when(userMapper.selectOne(any())).thenReturn(null);
        when(passwordUtil.isValidPassword("Password123!")).thenReturn(true);
        when(passwordUtil.hashPassword("Password123!")).thenReturn("hashed_password");

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("hashed_password", createdUser.getPasswordHash());
        assertEquals("ACTIVE", createdUser.getStatus());
        assertEquals(0, createdUser.getFailedLoginAttempts());
        assertNotNull(createdUser.getCreatedAt());
        assertNotNull(createdUser.getUpdatedAt());
        verify(userMapper).insert(createdUser);
    }

    @Test
    void createUserShouldThrowExceptionForDuplicateUsername() {
        // Arrange
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");
        user.setPasswordHash("Password123!");

        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setUsername("existinguser");

        when(userMapper.selectOne(any())).thenReturn(existingUser);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void createUserShouldThrowExceptionForInvalidPassword() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("weak");

        when(userMapper.selectOne(any())).thenReturn(null);
        when(passwordUtil.isValidPassword("weak")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void getUserByIdShouldReturnUserWhenFound() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("testuser");

        when(userMapper.selectById("1")).thenReturn(user);

        // Act
        Optional<User> result = userService.getUserById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getUserByIdShouldReturnEmptyWhenNotFound() {
        // Arrange
        when(userMapper.selectById("999")).thenReturn(null);

        // Act
        Optional<User> result = userService.getUserById("999");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateUserShouldSuccessfullyUpdateUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setUsername("olduser");
        existingUser.setEmail("old@example.com");
        existingUser.setPasswordHash("old_hashed_password");
        existingUser.setCreatedAt(LocalDateTime.now().minusDays(1));

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUsername("newuser");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPasswordHash("NewPassword123!");

        when(userMapper.selectById("1")).thenReturn(existingUser);
        when(passwordUtil.isValidPassword("NewPassword123!")).thenReturn(true);
        when(passwordUtil.hashPassword("NewPassword123!")).thenReturn("new_hashed_password");

        // Act
        User result = userService.updateUser(updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("new_hashed_password", result.getPasswordHash());
        assertNotNull(result.getPasswordChangedAt());
        assertNotNull(result.getUpdatedAt());
        verify(userMapper).updateById(result);
    }

    @Test
    void deleteUserShouldSuccessfullyDeleteUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setIsSuperAdmin(false);

        when(userMapper.selectById("1")).thenReturn(user);

        // Act
        userService.deleteUser("1");

        // Assert
        verify(userMapper).deleteById("1");
    }

    @Test
    void deleteUserShouldThrowExceptionForSuperAdmin() {
        // Arrange
        User superAdmin = new User();
        superAdmin.setId("1");
        superAdmin.setUsername("admin");
        superAdmin.setIsSuperAdmin(true);

        when(userMapper.selectById("1")).thenReturn(superAdmin);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser("1"));
    }

    @Test
    void unlockUserAccountShouldResetLockoutStatus() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("testuser");
        user.setLockedUntil(LocalDateTime.now().plusHours(1));
        user.setFailedLoginAttempts(3);

        when(userMapper.selectById("1")).thenReturn(user);

        // Act
        userService.unlockUserAccount("1");

        // Assert
        assertNull(user.getLockedUntil());
        assertEquals(0, user.getFailedLoginAttempts());
        verify(userMapper).updateById(user);
    }
}