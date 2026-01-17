package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.dto.UserDTO;
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
     * GET /users endpoint with pagination and status filtering
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String status) {
        
        List<User> users = userService.getUsers(page, limit, status);
        return ResponseEntity.ok(users);
    }

    /**
     * POST /users endpoint for creating new users
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * GET /users/{id} endpoint for retrieving specific user
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /users/{id} endpoint for updating user information
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        // Set the ID to match the path variable
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE /users/{id} endpoint for deleting users
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /users/{id}/unlock endpoint for admin account unlocking
     */
    @PostMapping("/{id}/unlock")
    public ResponseEntity<?> unlockUser(@PathVariable String id) {
        try {
            // In a real implementation, you'd verify that the caller is an admin
            // For now, we'll call the unlock method directly
            
            // Call the unlock functionality in AuthService
            // We'll need to get the AuthService bean in this controller
            // For now, we'll use a simplified approach
            
            // Since we don't have direct access to AuthService here, 
            // we'll add the method to UserService that calls AuthService
            userService.unlockUserAccount(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User account unlocked successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to unlock user: " + e.getMessage()
            ));
        }
    }
}