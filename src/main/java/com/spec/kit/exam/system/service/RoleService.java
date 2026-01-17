package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Creates a new role with customizable permissions
     * @param role the role to create
     * @return the created role
     */
    public Role createRole(Role role) {
        // Validate role data
        validateRoleData(role);

        // Check for duplicate role name
        List<Role> allRoles = getAllRoles();
        for (Role existingRole : allRoles) {
            if (existingRole.getName().equalsIgnoreCase(role.getName())) {
                throw new IllegalArgumentException("Role with name '" + role.getName() + "' already exists");
            }
        }

        // Validate permissions JSON format if provided
        if (role.getPermissions() != null && !isValidPermissionJson(role.getPermissions())) {
            throw new IllegalArgumentException("Invalid permission format");
        }

        // For super admin role, ensure only one exists
        if (role.getIsSuperAdminRole() != null && role.getIsSuperAdminRole()) {
            ensureSingleSuperAdmin();
        }

        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        if (role.getIsActive() == null) {
            role.setIsActive(true);
        }
        
        roleMapper.insert(role);
        return role;
    }

    /**
     * Validates role data according to requirements
     * @param role the role to validate
     */
    private void validateRoleData(Role role) {
        if (role.getName() == null || role.getName().trim().length() < 3 || role.getName().trim().length() > 100) {
            throw new IllegalArgumentException("Role name must be between 3 and 100 characters");
        }

        if (role.getDescription() != null && role.getDescription().length() > 500) {
            throw new IllegalArgumentException("Role description cannot exceed 500 characters");
        }
    }

    /**
     * Validates if the permission string is valid JSON format
     * @param permissionJson the permission JSON string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPermissionJson(String permissionJson) {
        // Basic validation - in a real implementation, you'd use a JSON parser
        // For now, we'll just check if it looks like JSON
        return permissionJson != null && 
               permissionJson.startsWith("{") && 
               permissionJson.endsWith("}") &&
               permissionJson.contains(":");
    }

    /**
     * Ensures only one super admin role exists in the system
     */
    private void ensureSingleSuperAdmin() {
        List<Role> allRoles = getAllRoles();
        for (Role role : allRoles) {
            if (role.getIsSuperAdminRole() != null && role.getIsSuperAdminRole()) {
                throw new IllegalArgumentException("A super admin role already exists. Only one super admin role is allowed.");
            }
        }
    }

    /**
     * Retrieves all roles
     * @return list of all roles
     */
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }

    /**
     * Retrieves a role by ID
     * @param id the role ID
     * @return the role if found
     */
    public Optional<Role> getRoleById(String id) {
        Role role = roleMapper.selectById(id);
        return Optional.ofNullable(role);
    }

    /**
     * Updates an existing role with permission changes
     * @param role the role with updated information
     * @return the updated role
     */
    public Role updateRole(Role role) {
        // Get existing role to validate changes
        Role existingRole = roleMapper.selectById(role.getId());
        if (existingRole == null) {
            throw new IllegalArgumentException("Role not found with ID: " + role.getId());
        }

        // Validate role data
        validateRoleData(role);

        // Prevent changing name of super admin role
        if (existingRole.getIsSuperAdminRole() != null && existingRole.getIsSuperAdminRole()) {
            role.setName(existingRole.getName()); // Don't allow changing name of super admin role
        }

        // Check for duplicate role name (except for the current role)
        List<Role> allRoles = getAllRoles();
        for (Role otherRole : allRoles) {
            if (!otherRole.getId().equals(role.getId()) && 
                otherRole.getName().equalsIgnoreCase(role.getName())) {
                throw new IllegalArgumentException("Role with name '" + role.getName() + "' already exists");
            }
        }

        // Validate permissions JSON format if provided
        if (role.getPermissions() != null && !isValidPermissionJson(role.getPermissions())) {
            throw new IllegalArgumentException("Invalid permission format");
        }

        // For super admin role, ensure only one exists
        if (role.getIsSuperAdminRole() != null && role.getIsSuperAdminRole()) {
            ensureSingleSuperAdminUpdate(role.getId());
        }

        role.setUpdatedAt(LocalDateTime.now());
        roleMapper.updateById(role);
        return role;
    }

    /**
     * Ensures only one super admin role exists when updating
     * @param currentRoleId the ID of the role being updated
     */
    private void ensureSingleSuperAdminUpdate(String currentRoleId) {
        List<Role> allRoles = getAllRoles();
        for (Role role : allRoles) {
            if (!role.getId().equals(currentRoleId) && 
                role.getIsSuperAdminRole() != null && 
                role.getIsSuperAdminRole()) {
                throw new IllegalArgumentException("A super admin role already exists. Only one super admin role is allowed.");
            }
        }
    }

    /**
     * Deletes a role by ID
     * @param id the role ID to delete
     */
    public void deleteRole(String id) {
        roleMapper.deleteById(id);
    }

    /**
     * Checks if a role is the super admin role
     * @param roleId the role ID to check
     * @return true if the role is the super admin role, false otherwise
     */
    public boolean isSuperAdminRole(String roleId) {
        Optional<Role> roleOpt = getRoleById(roleId);
        return roleOpt.isPresent() && Boolean.TRUE.equals(roleOpt.get().getIsSuperAdminRole());
    }
}