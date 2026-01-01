package com.exam.system.service;

import com.exam.system.model.User;
import com.exam.system.repository.UserElasticsearchRepository;
import com.exam.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private UserElasticsearchRepository userElasticsearchRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(User.Role.STUDENT);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // Given
        when(cacheService.get("user_username_testuser")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // When
        User result = userService.findByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(cacheService).get("user_username_testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnNull() {
        // Given
        when(cacheService.get("user_username_testuser")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // When
        User result = userService.findByUsername("testuser");

        // Then
        assertNull(result);
        verify(cacheService).get("user_username_testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(cacheService.get("user_id_1")).thenReturn(null);
        when(userRepository.findById("1")).thenReturn(user);

        // When
        User result = userService.findById("1");

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cacheService).get("user_id_1");
        verify(userRepository).findById("1");
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnNull() {
        // Given
        when(cacheService.get("user_id_1")).thenReturn(null);
        when(userRepository.findById("1")).thenReturn(null);

        // When
        User result = userService.findById("1");

        // Then
        assertNull(result);
        verify(cacheService).get("user_id_1");
        verify(userRepository).findById("1");
    }

    @Test
    void saveUser_WhenUserDoesNotExist_ShouldInsert() {
        // Given
        User newUser = new User();
        newUser.setUsername("newuser");
        when(userRepository.insert(any(User.class))).thenReturn(1);

        // When
        User result = userService.save(newUser);

        // Then
        assertNotNull(result);
        verify(userRepository).insert(any(User.class));
        // Don't verify selectById since when user.getId() is null, it doesn't call selectById
    }

    @Test
    void saveUser_WhenUserExists_ShouldUpdate() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        when(userRepository.selectById(1L)).thenReturn(existingUser);
        when(userRepository.updateById(any(User.class))).thenReturn(1);

        // When
        User result = userService.save(existingUser);

        // Then
        assertNotNull(result);
        verify(userRepository).updateById(any(User.class));
    }

    @Test
    void searchUsers_WhenElasticsearchAvailable_ShouldReturnUsers() {
        // Given
        when(userElasticsearchRepository.findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
                "test", "test", "test", "test")).thenReturn(java.util.Arrays.asList(user));

        // When
        java.util.List<User> result = userService.searchUsers("test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userElasticsearchRepository).findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
                "test", "test", "test", "test");
    }

    @Test
    void searchUsers_WhenElasticsearchNotAvailable_ShouldReturnEmptyList() {
        // Given - Elasticsearch is not available (Autowired with required=false)

        // When
        java.util.List<User> result = userService.searchUsers("test");

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}