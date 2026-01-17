package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Menu;
import com.spec.kit.exam.system.service.MenuService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.MenuErrorCodeEnum;
import com.spec.kit.exam.system.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for menu-related operations
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * Get menu tree structure
     */
    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree(@RequestParam(required = false) String roleId) {
        List<Menu> menuTree = menuService.getMenuTreeByRole(roleId);
        return Result.success(menuTree, "Menu tree retrieved successfully");
    }
    
    /**
     * Create a new menu
     */
    @PermissionRequired(menu = "menu-management", operation = "CREATE")
    @PostMapping("/create")
    public Result<Menu> createMenu(@RequestBody Menu menu) {
        Menu createdMenu = menuService.createMenu(menu);
        if (createdMenu != null) {
            return Result.success(createdMenu, "Menu created successfully");
        } else {
            return Result.error(MenuErrorCodeEnum.MENU_ALREADY_EXISTS);
        }
    }
    
    /**
     * Update an existing menu
     */
    @PermissionRequired(menu = "menu-management", operation = "UPDATE")
    @PutMapping("/update")
    public Result<Menu> updateMenu(@RequestBody Menu menu) {
        boolean success = menuService.updateMenu(menu);
        if (success) {
            return Result.success(menu, "Menu updated successfully");
        } else {
            return Result.error(MenuErrorCodeEnum.FAILED_TO_UPDATE_MENU, "Failed to update menu");
        }
    }
    
    /**
     * Delete a menu by ID
     */
    @PermissionRequired(menu = "menu-management", operation = "DELETE")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteMenu(@PathVariable String id) {
        boolean success = menuService.deleteMenu(id);
        if (success) {
            return Result.success(null, "Menu deleted successfully");
        } else {
            return Result.error(MenuErrorCodeEnum.FAILED_TO_DELETE_MENU, "Failed to delete menu");
        }
    }
    
    /**
     * Get menu by ID
     */
    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Menu> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);
        if (menu != null) {
            return Result.success(menu, "Menu retrieved successfully");
        } else {
            return Result.error(MenuErrorCodeEnum.MENU_NOT_FOUND);
        }
    }
    
    /**
     * Get all menus
     */
    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/list")
    public Result<PageResponse<Menu>> getAllMenus(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Menu> menus = menuService.getAllMenus();
        int totalCount = menus.size();
        
        // Simple pagination implementation
        int startIndex = (page - 1) * limit;
        if (startIndex >= totalCount) {
            menus = new java.util.ArrayList<>();
        } else {
            int endIndex = Math.min(startIndex + limit, totalCount);
            menus = menus.subList(startIndex, endIndex);
        }
        
        PageResponse<Menu> pageResponse = PageResponse.of(menus, totalCount, page, limit);
        return Result.success(pageResponse, "Menus retrieved successfully");
    }
}