package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.dto.UserDTO;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET /users/list endpoint with pagination and status filtering
     */
    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/list")
    public Result<List<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String status) {
        
        List<User> users = userService.getUsers(page, limit, status);
        return Result.success(users, "Users retrieved successfully");
    }

    /**
     * POST /users/create endpoint for creating new users
     */
    @PermissionRequired(menu = "user-management", operation = "CREATE")
    @PostMapping("/create")
    public Result<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return Result.success(createdUser, "User created successfully");
    }

    /**
     * GET /users/{id} endpoint for retrieving specific user
     */
    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Result.success(user.get(), "User retrieved successfully");
        } else {
            return Result.error(404, "User not found");
        }
    }

    /**
     * PUT /users/update endpoint for updating user information
     */
    @PermissionRequired(menu = "user-management", operation = "UPDATE")
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser, "User updated successfully");
    }

    /**
     * DELETE /users/delete/{id} endpoint for deleting users
     */
    @PermissionRequired(menu = "user-management", operation = "DELETE")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return Result.success(null, "User deleted successfully");
    }

    /**
     * POST /users/unlock/{id} endpoint for admin account unlocking
     */
    @PermissionRequired(menu = "user-management", operation = "UNLOCK")
    @PostMapping("/unlock/{id}")
    public Result<Void> unlockUser(@PathVariable String id) {
        try {
            // In a real implementation, you'd verify that the caller is an admin
            // For now, we'll call the unlock method directly
            
            // Call the unlock functionality in AuthService
            // We'll need to get the AuthService bean in this controller
            // For now, we'll use a simplified approach
            
            // Since we don't have direct access to AuthService here, 
            // we'll add the method to UserService that calls AuthService
            userService.unlockUserAccount(id);
            
            return Result.success(null, "User account unlocked successfully");
        } catch (Exception e) {
            return Result.error(500, "Failed to unlock user: " + e.getMessage());
        }
    }
}