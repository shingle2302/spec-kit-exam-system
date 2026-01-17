package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spec.kit.exam.system.entity.Permission;
import com.spec.kit.exam.system.entity.RolePermission;
import com.spec.kit.exam.system.mapper.PermissionMapper;
import com.spec.kit.exam.system.mapper.RolePermissionMapper;
import com.spec.kit.exam.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of PermissionService interface
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    /**
     * Get all permissions for a specific role
     * @param roleId The ID of the role
     * @return List of permissions assigned to the role
     */
    @Override
    public List<Permission> getPermissionsByRole(String roleId) {
        if (roleId == null || roleId.isEmpty()) {
            return List.of();
        }
        return permissionMapper.selectByRoleId(roleId);
    }
    
    /**
     * Assign permissions to a role
     * @param roleId The ID of the role
     * @param permissionIds List of permission IDs to assign
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean assignPermissionsToRole(String roleId, List<String> permissionIds) {
        if (roleId == null || permissionIds == null || permissionIds.isEmpty()) {
            return false;
        }
        
        for (String permissionId : permissionIds) {
            // Check if association already exists
            if (rolePermissionMapper.countByRoleAndPermission(roleId, permissionId) == 0) {
                RolePermission rp = new RolePermission(roleId, permissionId);
                rolePermissionMapper.insert(rp);
            }
        }
        return true;
    }
    
    /**
     * Remove permissions from a role
     * @param roleId The ID of the role
     * @param permissionIds List of permission IDs to remove
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean removePermissionsFromRole(String roleId, List<String> permissionIds) {
        if (roleId == null || permissionIds == null || permissionIds.isEmpty()) {
            return false;
        }
        
        for (String permissionId : permissionIds) {
            rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>()
                    .eq(RolePermission::getRoleId, roleId)
                    .eq(RolePermission::getPermissionId, permissionId)
            );
        }
        return true;
    }
    
    /**
     * Check if a user has a specific permission
     * @param userId The ID of the user
     * @param menuId The menu ID (permission code format: menuId:operation)
     * @param operation The operation type
     * @return true if user has permission, false otherwise
     */
    @Override
    public boolean hasPermission(String userId, String menuId, String operation) {
        if (userId == null || menuId == null || operation == null) {
            return false;
        }
        String permissionCode = String.format("%s:%s", menuId, operation).toUpperCase();
        return permissionMapper.countUserPermission(userId, permissionCode) > 0;
    }
    
    /**
     * Create a new permission
     * @param permission The permission to create
     * @return The created permission with ID
     */
    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        if (permission.getId() == null || permission.getId().isEmpty()) {
            permission.setId(UUID.randomUUID().toString());
        }
        if (permission.getStatus() == null) {
            permission.setStatus("ACTIVE");
        }
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedAt(LocalDateTime.now());
        
        permissionMapper.insert(permission);
        return permission;
    }
    
    /**
     * Update an existing permission
     * @param permission The permission to update
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean updatePermission(Permission permission) {
        if (permission.getId() == null || permission.getId().isEmpty()) {
            return false;
        }
        permission.setUpdatedAt(LocalDateTime.now());
        return permissionMapper.updateById(permission) > 0;
    }
    
    /**
     * Delete a permission by ID
     * @param permissionId The ID of the permission to delete
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean deletePermission(String permissionId) {
        if (permissionId == null || permissionId.isEmpty()) {
            return false;
        }
        // First delete role associations
        rolePermissionMapper.deleteByPermissionId(permissionId);
        // Then delete the permission
        return permissionMapper.deleteById(permissionId) > 0;
    }
    
    /**
     * Get all available permissions
     * @return List of all permissions
     */
    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectList(null);
    }
    
    /**
     * Get permissions by menu ID
     * @param menuId The ID of the menu
     * @return List of permissions for the menu
     */
    @Override
    public List<Permission> getPermissionsByMenu(String menuId) {
        if (menuId == null || menuId.isEmpty()) {
            return List.of();
        }
        return permissionMapper.selectByMenuId(menuId);
    }
    
    /**
     * Get permission by permission code
     * @param permissionCode The permission code
     * @return The permission or null if not found
     */
    @Override
    public Permission getPermissionByCode(String permissionCode) {
        if (permissionCode == null || permissionCode.isEmpty()) {
            return null;
        }
        return permissionMapper.selectByPermissionCode(permissionCode);
    }
    
    /**
     * Get all permission codes for a user
     * @param userId The user ID
     * @return List of permission codes
     */
    @Override
    public List<String> getPermissionCodesByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            return List.of();
        }
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }
}