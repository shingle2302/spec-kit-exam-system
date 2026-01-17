package com.spec.kit.exam.system.config;

import com.spec.kit.exam.system.service.PermissionInitializationService;
import com.spec.kit.exam.system.service.RoleService;
import com.spec.kit.exam.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for permission-related functionality including super admin initialization
 */
@Configuration
public class PermissionInitializationConfig {
    

    @Autowired
    private PermissionInitializationService permissionInitializationService;

    /**
     * Initialize super admin account on application startup
     */
    @Bean
    public CommandLineRunner initializeSuperAdmin() {
        return args -> {
            // Initialize permissions from annotations
            initializePermissionsFromAnnotations();
        };
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