package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * GET /role/list endpoint for retrieving all roles
     */
    @GetMapping("/list")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * POST /role/create endpoint for creating new roles
     */
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    /**
     * GET /role/{id} endpoint for retrieving specific role
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable String id) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /role/code/{code} endpoint for retrieving role by code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Role> getRoleByCode(@PathVariable String code) {
        Optional<Role> role = roleService.getRoleByCode(code);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /role/update endpoint for updating roles
     */
    @PutMapping("/update")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role updatedRole = roleService.updateRole(role);
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * DELETE /role/delete/{id} endpoint for deleting roles
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}