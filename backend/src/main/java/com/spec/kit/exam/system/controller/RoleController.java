package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.service.RoleService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.RoleErrorCodeEnum;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PermissionRequired(menu = "role-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<Role>> list(@RequestBody(required = false) PageRequestDTO request) {
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
        return Result.success(pageResponse, "角色列表查询成功");
    }

    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable String id) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            return Result.success(role.get(), "角色详情查询成功");
        } else {
            return Result.error(RoleErrorCodeEnum.ROLE_NOT_FOUND, "角色不存在");
        }
    }

    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/code/{code}")
    public Result<Role> getByCode(@PathVariable String code) {
        Optional<Role> role = roleService.getRoleByCode(code);
        if (role.isPresent()) {
            return Result.success(role.get(), "角色详情查询成功");
        } else {
            return Result.error(RoleErrorCodeEnum.ROLE_NOT_FOUND, "角色不存在");
        }
    }

    @PermissionRequired(menu = "role-management", operation = "CREATE")
    @PostMapping
    public Result<Role> create(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return Result.success(createdRole, "角色创建成功");
    }

    @PermissionRequired(menu = "role-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Role> update(@PathVariable String id, @RequestBody Role role) {
        role.setId(id);
        Role updatedRole = roleService.updateRole(role);
        return Result.success(updatedRole, "角色更新成功");
    }

    @PermissionRequired(menu = "role-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success(null, "角色删除成功");
    }

    @PermissionRequired(menu = "role-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> getStatistics() {
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        List<Role> allRoles = roleService.getAllRoles();
        statistics.put("total", allRoles.size());
        statistics.put("adminRoles", allRoles.stream().filter(r -> r.getCode().equals("ADMIN")).count());
        statistics.put("userRoles", allRoles.stream().filter(r -> r.getCode().equals("USER")).count());
        statistics.put("otherRoles", allRoles.stream().filter(r -> !r.getCode().equals("ADMIN") && !r.getCode().equals("USER")).count());
        return Result.success(statistics, "角色统计查询成功");
    }
}