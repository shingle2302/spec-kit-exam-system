package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Menu;
import com.spec.kit.exam.system.service.MenuService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for menu-related operations
 */
@RestController
@RequestMapping("/api/menu")
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
        return Result.success(createdMenu, "Menu created successfully");
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
            return Result.error(4001, "Failed to update menu");
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
            return Result.error(4001, "Failed to delete menu");
        }
    }
    
    /**
     * Get all menus
     */
    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/list")
    public Result<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return Result.success(menus, "Menus retrieved successfully");
    }
}