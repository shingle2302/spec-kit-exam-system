package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.UserErrorCodeEnum;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PermissionRequired(menu = "user-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<User>> list(@RequestBody(required = false) PageRequestDTO request) {
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
        return Result.success(pageResponse, "用户列表查询成功");
    }

    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Result.success(user.get(), "用户详情查询成功");
        } else {
            return Result.error(UserErrorCodeEnum.USER_NOT_FOUND, "用户不存在");
        }
    }

    @PermissionRequired(menu = "user-management", operation = "CREATE")
    @PostMapping
    public Result<User> create(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return Result.success(createdUser, "用户创建成功");
    }

    @PermissionRequired(menu = "user-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser, "用户更新成功");
    }

    @PermissionRequired(menu = "user-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        userService.deleteUser(id);
        return Result.success(null, "用户删除成功");
    }

    @PermissionRequired(menu = "user-management", operation = "UNLOCK")
    @PostMapping("/unlock/{id}")
    public Result<Void> unlock(@PathVariable String id) {
        try {
            userService.unlockUserAccount(id);
            return Result.success(null, "用户账户解锁成功");
        } catch (Exception e) {
            return Result.error(UserErrorCodeEnum.FAILED_TO_UNLOCK_USER, "用户解锁失败: " + e.getMessage());
        }
    }

    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", userService.getTotalCount());
        statistics.put("active", userService.getActiveCount());
        statistics.put("inactive", userService.getInactiveCount());
        statistics.put("locked", userService.getLockedCount());
        return Result.success(statistics, "用户统计查询成功");
    }

    @PermissionRequired(menu = "user-management", operation = "READ")
    @GetMapping("/search")
    public Result<List<User>> search(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return Result.success(users, "用户搜索成功");
    }
}