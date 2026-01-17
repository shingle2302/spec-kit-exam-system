package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Permission;
import com.spec.kit.exam.system.service.PermissionService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.PermissionErrorCodeEnum;
import com.spec.kit.exam.system.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for permission-related operations
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * Get permissions for a specific role
     */
    @PermissionRequired(menu = "permission-management", operation = "READ")
    @GetMapping("/role/{roleId}")
    public Result<List<Permission>> getPermissionsByRole(@PathVariable String roleId) {
        List<Permission> permissions = permissionService.getPermissionsByRole(roleId);
        return Result.success(permissions, "Permissions retrieved successfully");
    }
    
    /**
     * Assign permissions to a role
     */
    @PermissionRequired(menu = "permission-management", operation = "UPDATE")
    @PostMapping("/assign")
    public Result<Void> assignPermissionsToRole(@RequestBody PermissionAssignmentRequest request) {
        boolean success = permissionService.assignPermissionsToRole(request.getRoleId(), request.getPermissionIds());
        if (success) {
            return Result.success(null, "Permissions assigned successfully");
        } else {
            return Result.error(PermissionErrorCodeEnum.FAILED_TO_ASSIGN_PERMISSIONS, "Failed to assign permissions");
        }
    }
    
    /**
     * Remove permissions from a role
     */
    @PermissionRequired(menu = "permission-management", operation = "UPDATE")
    @PostMapping("/remove")
    public Result<Void> removePermissionsFromRole(@RequestBody PermissionAssignmentRequest request) {
        boolean success = permissionService.removePermissionsFromRole(request.getRoleId(), request.getPermissionIds());
        if (success) {
            return Result.success(null, "Permissions removed successfully");
        } else {
            return Result.error(PermissionErrorCodeEnum.FAILED_TO_REMOVE_PERMISSIONS, "Failed to remove permissions");
        }
    }
    
    /**
     * Get all permissions
     */
    @PermissionRequired(menu = "permission-management", operation = "READ")
    @GetMapping("/list")
    public Result<PageResponse<Permission>> getAllPermissions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Permission> permissions = permissionService.getAllPermissions();
        int totalCount = permissions.size();
        
        // Simple pagination implementation
        int startIndex = (page - 1) * limit;
        if (startIndex >= totalCount) {
            permissions = new java.util.ArrayList<>();
        } else {
            int endIndex = Math.min(startIndex + limit, totalCount);
            permissions = permissions.subList(startIndex, endIndex);
        }
        
        PageResponse<Permission> pageResponse = PageResponse.of(permissions, totalCount, page, limit);
        return Result.success(pageResponse, "Permissions retrieved successfully");
    }
    
    /**
     * Create a new permission
     */
    @PermissionRequired(menu = "permission-management", operation = "CREATE")
    @PostMapping("/create")
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return Result.success(createdPermission, "Permission created successfully");
    }
    
    /**
     * Update an existing permission
     */
    @PermissionRequired(menu = "permission-management", operation = "UPDATE")
    @PutMapping("/update")
    public Result<Permission> updatePermission(@RequestBody Permission permission) {
        boolean success = permissionService.updatePermission(permission);
        if (success) {
            return Result.success(permission, "Permission updated successfully");
        } else {
            return Result.error(PermissionErrorCodeEnum.FAILED_TO_UPDATE_PERMISSION, "Failed to update permission");
        }
    }
    
    /**
     * Delete a permission by ID
     */
    @PermissionRequired(menu = "permission-management", operation = "DELETE")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deletePermission(@PathVariable String id) {
        boolean success = permissionService.deletePermission(id);
        if (success) {
            return Result.success(null, "Permission deleted successfully");
        } else {
            return Result.error(PermissionErrorCodeEnum.FAILED_TO_DELETE_PERMISSION, "Failed to delete permission");
        }
    }
    
    /**
     * Get a permission by ID
     */
    @PermissionRequired(menu = "permission-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Permission> getPermissionById(@PathVariable String id) {
        Permission permission = permissionService.getPermissionById(id);
        if (permission != null) {
            return Result.success(permission, "Permission retrieved successfully");
        } else {
            return Result.error(PermissionErrorCodeEnum.PERMISSION_NOT_FOUND, "Permission not found");
        }
    }
    
    /**
     * Request class for permission assignment
     */
    public static class PermissionAssignmentRequest {
        private String roleId;
        private List<String> permissionIds;
        
        public String getRoleId() {
            return roleId;
        }
        
        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }
        
        public List<String> getPermissionIds() {
            return permissionIds;
        }
        
        public void setPermissionIds(List<String> permissionIds) {
            this.permissionIds = permissionIds;
        }
    }
}