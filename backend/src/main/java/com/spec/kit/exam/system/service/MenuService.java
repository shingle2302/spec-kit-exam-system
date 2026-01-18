package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.Menu;
import java.util.List;

/**
 * Service interface for menu-related operations
 */
public interface MenuService {
    
    /**
     * Get menu tree structure for a specific role
     * @param roleId The ID of the role
     * @return List of menus in tree structure for the role
     */
    List<Menu> getMenuTreeByRole(String roleId);
    
    /**
     * Create a new menu
     * @param menu The menu to create
     * @return The created menu with ID
     */
    Menu createMenu(Menu menu);
    
    /**
     * Update an existing menu
     * @param menu The menu to update
     * @return true if successful, false otherwise
     */
    boolean updateMenu(Menu menu);
    
    /**
     * Delete a menu by ID
     * @param menuId The ID of the menu to delete
     * @return true if successful, false otherwise
     */
    boolean deleteMenu(String menuId);
    
    /**
     * Get a menu by ID
     * @param menuId The ID of the menu to get
     * @return The menu or null if not found
     */
    Menu getMenuById(String menuId);
    
    /**
     * Get all menus
     * @return List of all menus
     */
    List<Menu> getAllMenus();
    
    /**
     * Get child menus for a parent menu
     * @param parentId The ID of the parent menu
     * @return List of child menus
     */
    List<Menu> getChildMenus(String parentId);
    
    /**
     * Get root menus (menus without parents)
     * @return List of root menus
     */
    List<Menu> getRootMenus();
    
    /**
     * Check if a menu exists by name and parent
     * @param name The menu name to check
     * @param parentId The parent ID to check under (can be null for root menus)
     * @return true if menu exists, false otherwise
     */
    boolean menuExists(String name, String parentId);
    
    /**
     * Move a menu to a different parent
     * @param menuId The ID of the menu to move
     * @param newParentId The ID of the new parent (null for root)
     * @return true if successful, false otherwise
     */
    boolean moveMenu(String menuId, String newParentId);
}