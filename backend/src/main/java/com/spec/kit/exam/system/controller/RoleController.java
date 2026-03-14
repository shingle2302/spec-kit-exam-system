package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.service.RoleService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.RoleErrorCodeEnum;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * POST /roles/list endpoint for retrieving all roles.
     */
    @PermissionRequired(menu = "role-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<Role>> getAllRoles(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        List<Role> roles = roleService.getAllRoles();
        int totalCount = roles.size();

        int startIndex = (pageRequest.getPage() - 1) * pageRequest.getSize();
        if (startIndex >= totalCount) {
            roles = new java.util.ArrayList<>();
        } else {
            int endIndex = Math.min(startIndex + pageRequest.getSize(), totalCount);
            roles = roles.subList(startIndex, endIndex);
        }

        PageResponse<Role> pageResponse = PageResponse.of(roles, totalCount, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(pageResponse, "Roles retrieved successfully");
    }

    /**
     * @deprecated use POST /api/roles/list with PageRequestDTO body instead.
     */
    @Deprecated
    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/list")
    public Result<PageResponse<Role>> getAllRolesLegacy(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return getAllRoles(new PageRequestDTO(page, size));
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
            return Result.error(RoleErrorCodeEnum.ROLE_NOT_FOUND, "Role not found");
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
            return Result.error(RoleErrorCodeEnum.ROLE_NOT_FOUND, "Role not found");
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
