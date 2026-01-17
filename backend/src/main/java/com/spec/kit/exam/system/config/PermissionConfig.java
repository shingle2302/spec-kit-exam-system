package com.spec.kit.exam.system.config;

import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.PermissionInitializationService;
import com.spec.kit.exam.system.service.RoleService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for permission-related functionality including super admin initialization
 */
@Configuration
public class PermissionConfig {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private PermissionInitializationService permissionInitializationService;

    /**
     * Initialize super admin account on application startup
     */
    @Bean
    public CommandLineRunner initializeSuperAdmin() {
        return args -> {
            // Create or verify super admin account exists
            createSuperAdminAccount();
            
            // Initialize permissions from annotations
            initializePermissionsFromAnnotations();
        };
    }

    /**
     * Create super admin account if it doesn't exist
     */
    private void createSuperAdminAccount() {
        System.out.println("Initializing super admin account...");
        
        // Check if super admin already exists
        User existingSuperAdmin = userService.getByUsername("superadmin");
        if (existingSuperAdmin != null) {
            System.out.println("Super admin account already exists.");
            return;
        }
        
        // Create super admin role if it doesn't exist
        Role superAdminRole = roleService.getByName("SUPER_ADMIN");
        if (superAdminRole == null) {
            superAdminRole = new Role();
            superAdminRole.setName("SUPER_ADMIN");
            superAdminRole.setDescription("Super Administrator with all permissions");
            superAdminRole.setIsSuperAdminRole(true);
            superAdminRole.setIsActive(true);
            roleService.save(superAdminRole);
            System.out.println("Super admin role created.");
        }
        
        // Create super admin user
        User superAdmin = new User();
        superAdmin.setUsername("superadmin");
        superAdmin.setPasswordHash(PasswordUtil.hashPasswordStatic("SuperAdmin123!"));
        superAdmin.setEmail("superadmin@exam-system.local");
        superAdmin.setIsSuperAdmin(true);
        superAdmin.setStatus("ACTIVE");
        superAdmin.setRoleId(superAdminRole.getId());
        
        userService.save(superAdmin);
        System.out.println("Super admin account created with username: superadmin and default password");
    }
    
    /**
     * Initialize permissions from @PermissionRequired annotations
     */
    private void initializePermissionsFromAnnotations() {
        System.out.println("Initializing permissions from annotations...");
        
        try {
            // Register permissions from annotations
            permissionInitializationService.registerPermissionsFromAnnotations();
            
            // Validate that required permissions exist
            boolean isValid = permissionInitializationService.validateRequiredPermissionsExist();
            if (isValid) {
                System.out.println("Permission validation passed.");
            } else {
                System.out.println("Some required permissions are missing.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing permissions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verify that there's always at least one super admin account
     */
    public boolean validateSuperAdminExists() {
        // This would typically query the database to ensure
        // at least one super admin account exists
        
        // In a real implementation, this would check the database
        // and potentially prevent certain operations if no super admin exists
        return true; // Placeholder implementation
    }
}