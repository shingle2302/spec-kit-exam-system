package com.spec.kit.exam.system.service;

import java.util.List;

/**
 * Service interface for automatic permission initialization during startup
 */
public interface PermissionInitializationService {
    
    /**
     * Scan for @PermissionRequired annotations and register permissions in the database
     * @return List of registered permission codes
     */
    List<String> registerPermissionsFromAnnotations();
    
    /**
     * Validate and synchronize permissions in the database with annotations
     * @return true if synchronization successful, false otherwise
     */
    boolean synchronizePermissionsWithAnnotations();
    
    /**
     * Get all registered permissions from the database
     * @return List of registered permission codes
     */
    List<String> getAllRegisteredPermissions();
    
    /**
     * Validate that all required permissions exist in the database
     * @return true if all required permissions exist, false otherwise
     */
    boolean validateRequiredPermissionsExist();
    
    /**
     * Update existing permissions in the database based on current annotations
     * @return number of permissions updated
     */
    int updateExistingPermissions();
    
    /**
     * Remove permissions that no longer exist in annotations
     * @param keepUnused if true, keep unused permissions; if false, remove them
     * @return number of permissions removed
     */
    int removeUnusedPermissions(boolean keepUnused);
}