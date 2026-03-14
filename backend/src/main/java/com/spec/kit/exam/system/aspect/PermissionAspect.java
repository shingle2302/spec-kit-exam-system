package com.spec.kit.exam.system.aspect;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.PermissionService;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.CommonErrorCodeEnum;
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
import java.util.ArrayList;

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
            return Result.error(CommonErrorCodeEnum.INVALID_PERMISSION_CONFIG, "Invalid permission configuration: menu is required");
        }
        
        if (operation.isEmpty()) {
            logger.error("Operation attribute is required in @PermissionRequired annotation");
            return Result.error(CommonErrorCodeEnum.INVALID_PERMISSION_CONFIG2, "Invalid permission configuration: operation is required");
        }
        
        // Generate required permission code(s): menu:button:operation (preferred), menu:operation (compatible)
        List<String> requiredPermissionCodes = buildRequiredPermissionCodes(menu, button, operation);
        List<String> userPermissions = permissionService.getPermissionCodesByUserId(currentUser.getId());
        boolean hasPermission = requiredPermissionCodes.stream().anyMatch(userPermissions::contains);
        
        if (!hasPermission) {
            String missingPermission = String.join(" or ", requiredPermissionCodes);
            logger.warn("Permission denied for user: {} on menu: {} button: {} operation: {} required: {}",
                       currentUser.getUsername(), menu, button, operation, missingPermission);
            return Result.unauthorized("Insufficient permissions, missing permission: " + missingPermission);
        }
        
        logger.debug("Permission granted for user: {} on menu: {} button: {} operation: {}",
                    currentUser.getUsername(), menu, button, operation);
        
        // Proceed with the original method call
        return joinPoint.proceed();
    }

    private List<String> buildRequiredPermissionCodes(String menu, String button, String operation) {
        List<String> permissionCodes = new ArrayList<>();
        String normalizedMenu = menu.trim();
        String normalizedOperation = operation.trim();

        if (button != null && !button.trim().isEmpty()) {
            String normalizedButton = button.trim();
            permissionCodes.add(String.format("%s:%s:%s", normalizedMenu, normalizedButton, normalizedOperation).toUpperCase());
        }

        permissionCodes.add(String.format("%s:%s", normalizedMenu, normalizedOperation).toUpperCase());
        return permissionCodes;
    }
}
