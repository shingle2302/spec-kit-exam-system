package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spec.kit.exam.system.entity.Menu;
import com.spec.kit.exam.system.mapper.MenuMapper;
import com.spec.kit.exam.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of MenuService interface
 */
@Service
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private MenuMapper menuMapper;
    
    /**
     * Get menu tree structure for a specific role
     * @param roleId The ID of the role (null to get all menus)
     * @return List of menus in tree structure for the role
     */
    @Override
    public List<Menu> getMenuTreeByRole(String roleId) {
        List<Menu> menus;
        if (roleId != null && !roleId.isEmpty()) {
            menus = menuMapper.selectByRoleId(roleId);
        } else {
            menus = menuMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getStatus, "ACTIVE")
                    .orderByAsc(Menu::getOrderNum)
            );
        }
        return buildMenuTree(menus);
    }
    
    /**
     * Build menu tree from flat list
     */
    private List<Menu> buildMenuTree(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Group menus by parentId
        Map<String, List<Menu>> childrenMap = menus.stream()
            .filter(m -> m.getParentId() != null)
            .collect(Collectors.groupingBy(Menu::getParentId));
        
        // Set children for each menu
        menus.forEach(menu -> {
            List<Menu> children = childrenMap.get(menu.getId());
            menu.setChildren(children != null ? children : new ArrayList<>());
        });
        
        // Return only root menus (those without parent)
        return menus.stream()
            .filter(m -> m.getParentId() == null)
            .collect(Collectors.toList());
    }
    
    /**
     * Create a new menu
     * @param menu The menu to create
     * @return The created menu with ID
     */
    @Override
    @Transactional
    public Menu createMenu(Menu menu) {
        if (menu.getId() == null || menu.getId().isEmpty()) {
            menu.setId(UUID.randomUUID().toString());
        }
        if (menu.getStatus() == null) {
            menu.setStatus("ACTIVE");
        }
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(0);
        }
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());
        
        menuMapper.insert(menu);
        return menu;
    }
    
    /**
     * Update an existing menu
     * @param menu The menu to update
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean updateMenu(Menu menu) {
        if (menu.getId() == null || menu.getId().isEmpty()) {
            return false;
        }
        menu.setUpdatedAt(LocalDateTime.now());
        return menuMapper.updateById(menu) > 0;
    }
    
    /**
     * Delete a menu by ID
     * @param menuId The ID of the menu to delete
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean deleteMenu(String menuId) {
        if (menuId == null || menuId.isEmpty()) {
            return false;
        }
        // This will also delete child menus due to CASCADE
        return menuMapper.deleteById(menuId) > 0;
    }
    
    /**
     * Get a menu by ID
     * @param menuId The ID of the menu to get
     * @return The menu or null if not found
     */
    @Override
    public Menu getMenuById(String menuId) {
        if (menuId == null || menuId.isEmpty()) {
            return null;
        }
        return menuMapper.selectById(menuId);
    }
    
    /**
     * Get all menus
     * @return List of all menus
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.selectList(
            new LambdaQueryWrapper<Menu>().orderByAsc(Menu::getOrderNum)
        );
    }
    
    /**
     * Get child menus for a parent menu
     * @param parentId The ID of the parent menu
     * @return List of child menus
     */
    @Override
    public List<Menu> getChildMenus(String parentId) {
        return menuMapper.selectByParentId(parentId);
    }
    
    /**
     * Get root menus (menus without parents)
     * @return List of root menus
     */
    @Override
    public List<Menu> getRootMenus() {
        return menuMapper.selectRootMenus();
    }
    
    /**
     * Check if a menu exists by name and parent
     * @param name The menu name to check
     * @param parentId The parent ID to check under (can be null for root menus)
     * @return true if menu exists, false otherwise
     */
    @Override
    public boolean menuExists(String name, String parentId) {
        return menuMapper.countByNameAndParent(name, parentId) > 0;
    }
    
    /**
     * Move a menu to a different parent
     * @param menuId The ID of the menu to move
     * @param newParentId The ID of the new parent (null for root)
     * @return true if successful, false otherwise
     */
    @Override
    @Transactional
    public boolean moveMenu(String menuId, String newParentId) {
        Menu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            return false;
        }
        menu.setParentId(newParentId);
        menu.setUpdatedAt(LocalDateTime.now());
        return menuMapper.updateById(menu) > 0;
    }
}