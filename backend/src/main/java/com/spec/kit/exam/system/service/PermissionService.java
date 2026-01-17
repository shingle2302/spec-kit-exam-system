package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.Permission;
import java.util.List;

/**
 * Service interface for permission-related operations
 */
public interface PermissionService {
    
    /**
     * Get all permissions for a specific role
     * @param roleId The ID of the role
     * @return List of permissions assigned to the role
     */
    List<Permission> getPermissionsByRole(String roleId);
    
    /**
     * Assign permissions to a role
     * @param roleId The ID of the role
     * @param permissionIds List of permission IDs to assign
     * @return true if successful, false otherwise
     */
    boolean assignPermissionsToRole(String roleId, List<String> permissionIds);
    
    /**
     * Remove permissions from a role
     * @param roleId The ID of the role
     * @param permissionIds List of permission IDs to remove
     * @return true if successful, false otherwise
     */
    boolean removePermissionsFromRole(String roleId, List<String> permissionIds);
    
    /**
     * Check if a user has a specific permission
     * @param userId The ID of the user
     * @param menuId The menu ID (permission code format: menuId:operation)
     * @param operation The operation type
     * @return true if user has permission, false otherwise
     */
    boolean hasPermission(String userId, String menuId, String operation);
    
    /**
     * Create a new permission
     * @param permission The permission to create
     * @return The created permission with ID
     */
    Permission createPermission(Permission permission);
    
    /**
     * Update an existing permission
     * @param permission The permission to update
     * @return true if successful, false otherwise
     */
    boolean updatePermission(Permission permission);
    
    /**
     * Delete a permission by ID
     * @param permissionId The ID of the permission to delete
     * @return true if successful, false otherwise
     */
    boolean deletePermission(String permissionId);
    
    /**
     * Get all available permissions
     * @return List of all permissions
     */
    List<Permission> getAllPermissions();
    
    /**
     * Get permissions by menu ID
     * @param menuId The ID of the menu
     * @return List of permissions for the menu
     */
    List<Permission> getPermissionsByMenu(String menuId);
    
    /**
     * Get permission by permission code
     * @param permissionCode The permission code
     * @return The permission or null if not found
     */
    Permission getPermissionByCode(String permissionCode);
    
    /**
     * Get all permission codes for a user
     * @param userId The user ID
     * @return List of permission codes
     */
    List<String> getPermissionCodesByUserId(String userId);
}