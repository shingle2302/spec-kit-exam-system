package com.spec.kit.exam.system.aspect;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.PermissionService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Aspect for handling permission checks using annotations
 */
@Aspect
@Component
public class PermissionAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionAspect.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionService permissionService;

    /**
     * Around advice to intercept methods annotated with @PermissionRequired
     */
    @Around("@annotation(permissionRequired)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, PermissionRequired permissionRequired) throws Throwable {
        // Get the current authentication from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            logger.warn("User not authenticated when accessing method requiring permission");
            return Result.unauthorized("Authentication required");
        }
        
        // Get user ID from authentication principal
        String userId = null;
        if (authentication.getPrincipal() instanceof User) {
            userId = ((User) authentication.getPrincipal()).getId();
        } else {
            // If principal is a string (like username), we need to find the user
            userId = authentication.getName();
        }
        
        // Fetch the user from database to get full details
        User currentUser = userService.getById(userId);
        
        if (currentUser == null) {
            logger.warn("User not found in database: {}", userId);
            return Result.unauthorized("User not found");
        }
        
        // Check if user is a super admin - they bypass all permission checks
        if (currentUser.getIsSuperAdmin() != null && currentUser.getIsSuperAdmin()) {
            logger.debug("Super admin access granted for method: {}", joinPoint.getSignature().getName());
            return joinPoint.proceed(); // Proceed without permission check
        }
        
        // Extract permission requirements from annotation
        String menu = permissionRequired.menu();
        String button = permissionRequired.button();
        String operation = permissionRequired.operation();
        
        // Validate that required attributes are provided
        if (menu.isEmpty()) {
            logger.error("Menu attribute is required in @PermissionRequired annotation");
            return Result.error(2001, "Invalid permission configuration: menu is required");
        }
        
        if (operation.isEmpty()) {
            logger.error("Operation attribute is required in @PermissionRequired annotation");
            return Result.error(2001, "Invalid permission configuration: operation is required");
        }
        
        // Generate permission code and check if user has the required permission
        String permissionCode = String.format("%s:%s", menu, operation).toUpperCase();
        List<String> userPermissions = permissionService.getPermissionCodesByUserId(currentUser.getId());
        boolean hasPermission = userPermissions.contains(permissionCode);
        
        if (!hasPermission) {
            logger.warn("Permission denied for user: {} on menu: {} operation: {}", 
                       currentUser.getUsername(), menu, operation);
            return Result.unauthorized("Insufficient permissions");
        }
        
        logger.debug("Permission granted for user: {} on menu: {} operation: {}", 
                    currentUser.getUsername(), menu, operation);
        
        // Proceed with the original method call
        return joinPoint.proceed();
    }
}