package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.Permission;
import com.spec.kit.exam.system.entity.RolePermission;
import com.spec.kit.exam.system.mapper.PermissionMapper;
import com.spec.kit.exam.system.mapper.RolePermissionMapper;
import com.spec.kit.exam.system.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {

    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private RolePermissionMapper rolePermissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Test
    void getPermissionsByRoleShouldReturnPermissions() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setId("1");
        permission1.setButtonName("Read");

        Permission permission2 = new Permission();
        permission2.setId("2");
        permission2.setButtonName("Write");

        List<Permission> permissions = List.of(permission1, permission2);

        when(permissionMapper.selectByRoleId("role1")).thenReturn(permissions);

        // Act
        List<Permission> result = permissionService.getPermissionsByRole("role1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Read", result.get(0).getButtonName());
        assertEquals("Write", result.get(1).getButtonName());
    }

    @Test
    void getPermissionsByRoleShouldReturnEmptyListForNullRoleId() {
        // Act
        List<Permission> result = permissionService.getPermissionsByRole(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void assignPermissionsToRoleShouldSuccessfullyAssignPermissions() {
        // Arrange
        when(rolePermissionMapper.countByRoleAndPermission("role1", "perm1")).thenReturn(0);
        when(rolePermissionMapper.countByRoleAndPermission("role1", "perm2")).thenReturn(0);

        // Act
        boolean result = permissionService.assignPermissionsToRole("role1", List.of("perm1", "perm2"));

        // Assert
        assertTrue(result);
        verify(rolePermissionMapper, times(2)).insert(any(RolePermission.class));
    }

    @Test
    void assignPermissionsToRoleShouldReturnFalseForNullRoleId() {
        // Act
        boolean result = permissionService.assignPermissionsToRole(null, List.of("perm1"));

        // Assert
        assertFalse(result);
    }

    @Test
    void removePermissionsFromRoleShouldSuccessfullyRemovePermissions() {
        // Act
        boolean result = permissionService.removePermissionsFromRole("role1", List.of("perm1", "perm2"));

        // Assert
        assertTrue(result);
        verify(rolePermissionMapper, times(2)).delete(any());
    }

    @Test
    void removePermissionsFromRoleShouldReturnFalseForNullRoleId() {
        // Act
        boolean result = permissionService.removePermissionsFromRole(null, List.of("perm1"));

        // Assert
        assertFalse(result);
    }

    @Test
    void hasPermissionShouldReturnTrueWhenUserHasPermission() {
        // Arrange
        when(permissionMapper.countUserPermission("user1", "MENU1:READ")).thenReturn(1);

        // Act
        boolean result = permissionService.hasPermission("user1", "menu1", "read");

        // Assert
        assertTrue(result);
    }

    @Test
    void hasPermissionShouldReturnFalseWhenUserDoesNotHavePermission() {
        // Arrange
        when(permissionMapper.countUserPermission("user1", "MENU1:READ")).thenReturn(0);

        // Act
        boolean result = permissionService.hasPermission("user1", "menu1", "read");

        // Assert
        assertFalse(result);
    }

    @Test
    void createPermissionShouldSuccessfullyCreatePermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setButtonName("Read");
        permission.setPermissionCode("MENU1:READ");

        // Act
        Permission result = permissionService.createPermission(permission);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Read", result.getButtonName());
        assertEquals("MENU1:READ", result.getPermissionCode());
        assertEquals("ACTIVE", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(permissionMapper).insert(result);
    }

    @Test
    void updatePermissionShouldSuccessfullyUpdatePermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setId("1");
        permission.setButtonName("Updated Read");

        when(permissionMapper.updateById(permission)).thenReturn(1);

        // Act
        boolean result = permissionService.updatePermission(permission);

        // Assert
        assertTrue(result);
        verify(permissionMapper).updateById(permission);
    }

    @Test
    void deletePermissionShouldSuccessfullyDeletePermission() {
        // Arrange
        when(permissionMapper.deleteById("1")).thenReturn(1);

        // Act
        boolean result = permissionService.deletePermission("1");

        // Assert
        assertTrue(result);
        verify(rolePermissionMapper).deleteByPermissionId("1");
        verify(permissionMapper).deleteById("1");
    }

    @Test
    void getAllPermissionsShouldReturnAllPermissions() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setId("1");
        permission1.setButtonName("Read");

        Permission permission2 = new Permission();
        permission2.setId("2");
        permission2.setButtonName("Write");

        List<Permission> permissions = List.of(permission1, permission2);

        when(permissionMapper.selectList(null)).thenReturn(permissions);

        // Act
        List<Permission> result = permissionService.getAllPermissions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPermissionCodesByUserIdShouldReturnPermissionCodes() {
        // Arrange
        List<String> rawCodes = List.of("MENU1:READ", "MENU2:WRITE");
        when(permissionMapper.selectPermissionCodesByUserId("user1")).thenReturn(rawCodes);

        // Act
        List<String> result = permissionService.getPermissionCodesByUserId("user1");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("MENU1:READ"));
        assertTrue(result.contains("MENU2:WRITE"));
    }
}