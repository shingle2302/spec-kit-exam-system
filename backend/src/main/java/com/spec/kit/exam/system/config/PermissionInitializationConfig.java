package com.spec.kit.exam.system.config;

import com.spec.kit.exam.system.service.PermissionInitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for permission-related functionality including super admin initialization
 */
@Configuration
public class PermissionInitializationConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionInitializationConfig.class);


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
        logger.info("Initializing permissions from annotations...");
        
        try {
            // Register permissions from annotations
            permissionInitializationService.registerPermissionsFromAnnotations();
            
            // Validate that required permissions exist
            boolean isValid = permissionInitializationService.validateRequiredPermissionsExist();
            if (isValid) {
                logger.info("Permission validation passed.");
            } else {
                logger.warn("Some required permissions are missing.");
            }
        } catch (Exception e) {
            logger.error("Error initializing permissions: " + e.getMessage(), e);
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