package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.UserErrorCodeEnum;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST /users/list endpoint with pagination and status filtering.
     */
    @PermissionRequired(menu = "user-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<User>> getUsers(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;

        Map<String, Object> params = new HashMap<>(pageRequest.getFilters());
        params.put("page", pageRequest.getPage());
        params.put("size", pageRequest.getSize());

        String status = null;
        Object statusObj = params.get("status");
        if (statusObj != null) {
            status = statusObj.toString();
        }

        int totalCount = userService.getUserCount(params);
        List<User> users = userService.getUsers(pageRequest.getPage(), pageRequest.getSize(), status);

        PageResponse<User> pageResponse = PageResponse.of(users, totalCount, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(pageResponse, "Users retrieved successfully");
    }

    /**
     * @deprecated use POST /api/users/list with PageRequestDTO body instead.
     */
    @Deprecated
    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/list")
    public Result<PageResponse<User>> getUsersLegacy(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        PageRequestDTO request = new PageRequestDTO(page, size);
        if (status != null && !status.trim().isEmpty()) {
            Map<String, Object> filters = new HashMap<>();
            filters.put("status", status);
            request.setFilters(filters);
        }
        return getUsers(request);
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
            return Result.error(UserErrorCodeEnum.USER_NOT_FOUND, "User not found");
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
            userService.unlockUserAccount(id);
            return Result.success(null, "User account unlocked successfully");
        } catch (Exception e) {
            return Result.error(UserErrorCodeEnum.FAILED_TO_UNLOCK_USER, "Failed to unlock user: " + e.getMessage());
        }
    }
}
