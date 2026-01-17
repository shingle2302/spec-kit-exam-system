package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.service.RoleService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * GET /role/list endpoint for retrieving all roles
     */
    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/list")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles, "Roles retrieved successfully");
    }

    /**
     * POST /role/create endpoint for creating new roles
     */
    @PermissionRequired(menu = "role-management", operation = "CREATE")
    @PostMapping("/create")
    public Result<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return Result.success(createdRole, "Role created successfully");
    }

    /**
     * GET /role/{id} endpoint for retrieving specific role
     */
    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable String id) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            return Result.success(role.get(), "Role retrieved successfully");
        } else {
            return Result.error(404, "Role not found");
        }
    }

    /**
     * GET /role/code/{code} endpoint for retrieving role by code
     */
    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/code/{code}")
    public Result<Role> getRoleByCode(@PathVariable String code) {
        Optional<Role> role = roleService.getRoleByCode(code);
        if (role.isPresent()) {
            return Result.success(role.get(), "Role retrieved successfully");
        } else {
            return Result.error(404, "Role not found");
        }
    }
    
    /**
     * PUT /role/update endpoint for updating roles
     */
    @PermissionRequired(menu = "role-management", operation = "UPDATE")
    @PutMapping("/update")
    public Result<Role> updateRole(@RequestBody Role role) {
        Role updatedRole = roleService.updateRole(role);
        return Result.success(updatedRole, "Role updated successfully");
    }

    /**
     * DELETE /role/delete/{id} endpoint for deleting roles
     */
    @PermissionRequired(menu = "role-management", operation = "DELETE")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success(null, "Role deleted successfully");
    }
}