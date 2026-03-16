package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void createRoleShouldSuccessfullyCreateRole() {
        // Arrange
        Role role = new Role();
        role.setName("Test Role");
        role.setDescription("Test role description");
        role.setPermissions("{\"menu1\":[\"read\",\"write\"]}");

        when(roleMapper.selectList(null)).thenReturn(new ArrayList<>());

        // Act
        Role createdRole = roleService.createRole(role);

        // Assert
        assertNotNull(createdRole);
        assertEquals("Test Role", createdRole.getName());
        assertEquals("Test role description", createdRole.getDescription());
        assertEquals("{\"menu1\":[\"read\",\"write\"]}", createdRole.getPermissions());
        assertTrue(createdRole.getIsActive());
        assertNotNull(createdRole.getCreatedAt());
        assertNotNull(createdRole.getUpdatedAt());
        verify(roleMapper).insert(createdRole);
    }

    @Test
    void createRoleShouldThrowExceptionForDuplicateName() {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId("1");
        existingRole.setName("Test Role");

        Role newRole = new Role();
        newRole.setName("Test Role");

        when(roleMapper.selectList(null)).thenReturn(List.of(existingRole));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(newRole));
    }

    @Test
    void createRoleShouldThrowExceptionForInvalidPermissionFormat() {
        // Arrange
        Role role = new Role();
        role.setName("Test Role");
        role.setPermissions("invalid-json");

        when(roleMapper.selectList(null)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
    }

    @Test
    void createRoleShouldThrowExceptionForMultipleSuperAdminRoles() {
        // Arrange
        Role existingSuperAdmin = new Role();
        existingSuperAdmin.setId("1");
        existingSuperAdmin.setName("Super Admin");
        existingSuperAdmin.setIsSuperAdminRole(true);

        Role newSuperAdmin = new Role();
        newSuperAdmin.setName("Another Super Admin");
        newSuperAdmin.setIsSuperAdminRole(true);

        when(roleMapper.selectList(null)).thenReturn(List.of(existingSuperAdmin));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(newSuperAdmin));
    }

    @Test
    void getRoleByIdShouldReturnRoleWhenFound() {
        // Arrange
        Role role = new Role();
        role.setId("1");
        role.setName("Test Role");

        when(roleMapper.selectById("1")).thenReturn(role);

        // Act
        Optional<Role> result = roleService.getRoleById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Role", result.get().getName());
    }

    @Test
    void getRoleByIdShouldReturnEmptyWhenNotFound() {
        // Arrange
        when(roleMapper.selectById("999")).thenReturn(null);

        // Act
        Optional<Role> result = roleService.getRoleById("999");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateRoleShouldSuccessfullyUpdateRole() {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId("1");
        existingRole.setName("Old Role");
        existingRole.setDescription("Old description");

        Role updatedRole = new Role();
        updatedRole.setId("1");
        updatedRole.setName("New Role");
        updatedRole.setDescription("New description");
        updatedRole.setPermissions("{\"menu1\":[\"read\"]}");

        when(roleMapper.selectById("1")).thenReturn(existingRole);
        when(roleMapper.selectList(null)).thenReturn(List.of(existingRole));

        // Act
        Role result = roleService.updateRole(updatedRole);

        // Assert
        assertNotNull(result);
        assertEquals("New Role", result.getName());
        assertEquals("New description", result.getDescription());
        assertEquals("{\"menu1\":[\"read\"]}", result.getPermissions());
        assertNotNull(result.getUpdatedAt());
        verify(roleMapper).updateById(result);
    }

    @Test
    void deleteRoleShouldSuccessfullyDeleteRole() {
        // Act
        roleService.deleteRole("1");

        // Assert
        verify(roleMapper).deleteById("1");
    }

    @Test
    void isSuperAdminRoleShouldReturnTrueForSuperAdminRole() {
        // Arrange
        Role superAdminRole = new Role();
        superAdminRole.setId("1");
        superAdminRole.setIsSuperAdminRole(true);

        when(roleMapper.selectById("1")).thenReturn(superAdminRole);

        // Act
        boolean result = roleService.isSuperAdminRole("1");

        // Assert
        assertTrue(result);
    }

    @Test
    void isSuperAdminRoleShouldReturnFalseForNonSuperAdminRole() {
        // Arrange
        Role regularRole = new Role();
        regularRole.setId("1");
        regularRole.setIsSuperAdminRole(false);

        when(roleMapper.selectById("1")).thenReturn(regularRole);

        // Act
        boolean result = roleService.isSuperAdminRole("1");

        // Assert
        assertFalse(result);
    }
}