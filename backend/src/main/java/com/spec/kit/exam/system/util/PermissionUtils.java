package com.spec.kit.exam.system.util;

import com.spec.kit.exam.system.entity.User;

/**
 * Helper class for permission-related utility functions
 */
public class PermissionUtils {

    /**
     * Check if a user is a super admin
     * @param user The user to check
     * @return true if user is a super admin, false otherwise
     */
    public static boolean isSuperAdmin(User user) {
        if (user == null) {
            return false;
        }

        // Check if user is marked as super admin
        return user.getIsSuperAdmin() != null && user.getIsSuperAdmin();
    }

    /**
     * Generate permission code from menu and operation
     * @param menu The menu identifier
     * @param operation The operation type
     * @return Generated permission code
     */
    public static String generatePermissionCode(String menu, String operation) {
        return String.format("%s:%s", menu, operation).toUpperCase();
    }
}