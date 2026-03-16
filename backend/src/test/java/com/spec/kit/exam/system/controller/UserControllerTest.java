package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUsersShouldReturnPageResponse() {
        // Arrange
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(1);
        request.setSize(10);
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "ACTIVE");
        request.setFilters(filters);

        User user1 = new User();
        user1.setId("1");
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId("2");
        user2.setUsername("user2");

        List<User> users = List.of(user1, user2);

        when(userService.getUserCount(anyMap())).thenReturn(2);
        when(userService.getUsers(1, 10, "ACTIVE")).thenReturn(users);

        // Act
        Result<PageResponse<User>> result = userController.getUsers(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Users retrieved successfully", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getTotal());
        assertEquals(1, result.getData().getPage());
        assertEquals(10, result.getData().getSize());
        assertEquals(2, result.getData().getData().size());
    }

    @Test
    void getUsersShouldHandleNullRequest() {
        // Arrange
        User user1 = new User();
        user1.setId("1");
        user1.setUsername("user1");

        List<User> users = List.of(user1);

        when(userService.getUserCount(anyMap())).thenReturn(1);
        when(userService.getUsers(1, 10, null)).thenReturn(users);

        // Act
        Result<PageResponse<User>> result = userController.getUsers(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().getTotal());
    }

    @Test
    void createUserShouldReturnCreatedUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        User createdUser = new User();
        createdUser.setId("1");
        createdUser.setUsername("testuser");
        createdUser.setEmail("test@example.com");

        when(userService.createUser(user)).thenReturn(createdUser);

        // Act
        Result<User> result = userController.createUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User created successfully", result.getMsg());
        assertEquals(createdUser, result.getData());
    }

    @Test
    void getUserByIdShouldReturnUserWhenFound() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("testuser");

        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        // Act
        Result<User> result = userController.getUserById("1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User retrieved successfully", result.getMsg());
        assertEquals(user, result.getData());
    }

    @Test
    void getUserByIdShouldReturnErrorWhenUserNotFound() {
        // Arrange
        when(userService.getUserById("999")).thenReturn(Optional.empty());

        // Act
        Result<User> result = userController.getUserById("999");

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("User not found"));
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUsername("updateduser");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUsername("updateduser");

        when(userService.updateUser(user)).thenReturn(updatedUser);

        // Act
        Result<User> result = userController.updateUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User updated successfully", result.getMsg());
        assertEquals(updatedUser, result.getData());
    }

    @Test
    void deleteUserShouldReturnSuccess() {
        // Act
        Result<?> result = userController.deleteUser("1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User deleted successfully", result.getMsg());
        verify(userService).deleteUser("1");
    }

    @Test
    void unlockUserShouldReturnSuccess() {
        // Act
        Result<?> result = userController.unlockUser("1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("User account unlocked successfully", result.getMsg());
        verify(userService).unlockUserAccount("1");
    }

    @Test
    void unlockUserShouldReturnErrorWhenUnlockFails() {
        // Arrange
        doThrow(new RuntimeException("Unlock failed")).when(userService).unlockUserAccount("1");

        // Act
        Result<?> result = userController.unlockUser("1");

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("Failed to unlock user"));
    }
}