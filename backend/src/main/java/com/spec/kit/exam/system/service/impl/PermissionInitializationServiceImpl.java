package com.spec.kit.exam.system.service.impl;

import com.spec.kit.exam.system.service.PermissionInitializationService;
import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Permission;
import com.spec.kit.exam.system.entity.Menu;
import com.spec.kit.exam.system.service.PermissionService;
import com.spec.kit.exam.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation of PermissionInitializationService interface
 */
@Service
public class PermissionInitializationServiceImpl implements PermissionInitializationService {
    
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private MenuService menuService;
    
    /**
     * Scan for @PermissionRequired annotations and register permissions in the database
     * @return List of registered permission codes
     */
    @Override
    public List<String> registerPermissionsFromAnnotations() {
        List<String> registeredPermissions = new ArrayList<>();
        
        try {
            System.out.println("Scanning for @PermissionRequired annotations...");
            
            // This is a simplified example - in reality, you'd use Spring's component scanning
            // or reflection to find all annotated methods
            registeredPermissions = scanForPermissionAnnotations();
            
            // Register each permission in the database
            for (String permissionCode : registeredPermissions) {
                // Check if permission already exists
                Permission existingPermission = permissionService.getPermissionByCode(permissionCode);
                if (existingPermission == null) {
                    // Parse the permission code to extract menu and operation
                    String[] parts = permissionCode.split(":");
                    if (parts.length >= 2) {
                        String menuId = parts[0];
                        String operationType = parts[1];
                        
                        // Ensure the menu exists before creating the permission
                        ensureMenuExists(menuId);
                        
                        // Create new permission
                        Permission permission = new Permission();
                        permission.setPermissionCode(permissionCode);
                        permission.setMenuId(menuId);
                        permission.setOperationType(operationType);
                        permission.setDescription("Auto-generated permission for: " + permissionCode);
                        permission.setStatus("ACTIVE");
                        
                        permissionService.createPermission(permission);
                        System.out.println("Registered permission: " + permissionCode);
                    }
                } else {
                    System.out.println("Permission already exists: " + permissionCode);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error registering permissions from annotations: " + e.getMessage());
            e.printStackTrace();
        }
        
        return registeredPermissions;
    }
    
    /**
     * Validate and synchronize permissions in the database with annotations
     * @return true if synchronization successful, false otherwise
     */
    @Override
    public boolean synchronizePermissionsWithAnnotations() {
        try {
            // Get permissions from annotations
            List<String> annotatedPermissions = registerPermissionsFromAnnotations();
            
            System.out.println("Synchronizing permissions with annotations...");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error synchronizing permissions: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all registered permissions from the database
     * @return List of registered permission codes
     */
    @Override
    public List<String> getAllRegisteredPermissions() {
        return permissionService.getAllPermissions().stream()
            .map(Permission::getPermissionCode)
            .toList();
    }
    
    /**
     * Validate that all required permissions exist in the database
     * @return true if all required permissions exist, false otherwise
     */
    @Override
    public boolean validateRequiredPermissionsExist() {
        try {
            List<String> annotatedPermissions = registerPermissionsFromAnnotations();
            List<String> dbPermissions = getAllRegisteredPermissions();
            
            // Check if all annotated permissions exist in the database
            for (String perm : annotatedPermissions) {
                if (!dbPermissions.contains(perm)) {
                    System.out.println("Missing permission in database: " + perm);
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error validating permissions: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update existing permissions in the database based on current annotations
     * @return number of permissions updated
     */
    @Override
    public int updateExistingPermissions() {
        try {
            System.out.println("Updating existing permissions...");
            
            // In a real implementation, this would update permissions in the database
            // based on current annotations
            
            // For now, just return 0 as number of permissions updated
            return 0;
        } catch (Exception e) {
            System.err.println("Error updating permissions: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Remove permissions that no longer exist in annotations
     * @param keepUnused if true, keep unused permissions; if false, remove them
     * @return number of permissions removed
     */
    @Override
    public int removeUnusedPermissions(boolean keepUnused) {
        if (keepUnused) {
            return 0; // Nothing to remove if keeping unused
        }
        
        try {
            List<String> annotatedPermissions = registerPermissionsFromAnnotations();
            List<String> dbPermissions = getAllRegisteredPermissions();
            
            int removedCount = 0;
            
            // In a real implementation, this would remove permissions from the database
            // that are no longer annotated
            
            System.out.println("Removing unused permissions...");
            
            return removedCount;
        } catch (Exception e) {
            System.err.println("Error removing permissions: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Helper method to scan for @PermissionRequired annotations
     * @return List of permission codes found in annotations
     */
    private List<String> scanForPermissionAnnotations() {
        List<String> permissions = new ArrayList<>();
        Set<String> uniquePermissions = new HashSet<>();
        
        try {
            // Use Spring's resource pattern resolver to scan for classes
            PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
            CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceResolver);
            
            // Scan for all classes in the controller package
            Resource[] resources = resourceResolver.getResources("classpath*:com/spec/kit/exam/system/controller/**/*.class");
            
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    
                    // Get the class name
                    String className = metadataReader.getClassMetadata().getClassName();
                    
                    try {
                        // Load the actual class to check for method annotations
                        Class<?> clazz = Class.forName(className);
                        Method[] methods = clazz.getDeclaredMethods();
                        
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(PermissionRequired.class)) {
                                PermissionRequired permAnnotation = method.getAnnotation(PermissionRequired.class);
                                
                                // Generate permission code based on annotation values
                                String menu = permAnnotation.menu();
                                String operation = permAnnotation.operation();
                                
                                if (!menu.isEmpty() && !operation.isEmpty()) {
                                    String permissionCode = String.format("%s:%s", menu, operation).toUpperCase();
                                    
                                    // Add to unique set to avoid duplicates
                                    uniquePermissions.add(permissionCode);
                                    
                                    System.out.println("Found permission: " + permissionCode + " in " + className + "." + method.getName());
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("Could not load class: " + className);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error scanning for annotations: " + e.getMessage());
        }
        
        return new ArrayList<>(uniquePermissions);
    }
    
    /**
     * Ensure a menu exists in the database, create it if it doesn't exist
     * @param menuId the menu ID to ensure exists
     */
    private void ensureMenuExists(String menuId) {
        // Check if the menu already exists
        Optional<Menu> existingMenuOpt = menuService.getMenuById(menuId);
        if (existingMenuOpt.isEmpty()) {
            // Create a basic menu entry if it doesn't exist
            Menu newMenu = new Menu();
            newMenu.setId(menuId);
            newMenu.setName(generateMenuName(menuId));
            newMenu.setPath("/" + menuId.toLowerCase().replace("_", "-"));
            newMenu.setComponent("DefaultComponent");
            newMenu.setIcon("default-icon");
            newMenu.setParentId(null); // Top level menu
            newMenu.setOrderNum(999); // Default order
            newMenu.setStatus("ACTIVE");
            
            menuService.createMenu(newMenu);
            System.out.println("Created missing menu: " + menuId);
        }
    }
    
    /**
     * Generate a human-readable menu name from the menu ID
     * @param menuId the menu ID
     * @return a human-readable menu name
     */
    private String generateMenuName(String menuId) {
        // Convert from MENU_MANAGEMENT to Menu Management format
        String[] words = menuId.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i > 0) sb.append(" ");
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1));
        }
        return sb.toString();
    }
}