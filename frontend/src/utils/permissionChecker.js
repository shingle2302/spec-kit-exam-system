/**
 * Utility for checking permissions on the frontend side
 * Note: This is for UI display purposes only; backend validation is still required
 */

/**
 * Check if the current user has a specific permission
 * @param {string} menu - The menu identifier
 * @param {string} operation - The operation type
 * @returns {boolean} Whether the user has the permission
 */
export const hasPermission = (menu, operation) => {
  // Get user permissions from local storage or session
  const userPermissions = getUserPermissions();
  
  // Check if user is super admin (has all permissions)
  if (userPermissions.includes('*')) {
    return true;
  }
  
  // Generate the permission code based on menu and operation
  const permissionCode = generatePermissionCode(menu, operation);
  
  // Check if the user has this specific permission
  return userPermissions.includes(permissionCode);
};

/**
 * Get user permissions from local storage/session
 * @returns {Array<string>} List of permission codes
 */
export const getUserPermissions = () => {
  try {
    const permissionsStr = localStorage.getItem('userPermissions') || sessionStorage.getItem('userPermissions');
    if (permissionsStr) {
      return JSON.parse(permissionsStr);
    }
    return [];
  } catch (error) {
    console.error('Error parsing user permissions:', error);
    return [];
  }
};

/**
 * Set user permissions in local storage/session
 * @param {Array<string>} permissions - List of permission codes
 */
export const setUserPermissions = (permissions) => {
  try {
    localStorage.setItem('userPermissions', JSON.stringify(permissions));
  } catch (error) {
    console.error('Error storing user permissions:', error);
  }
};

/**
 * Generate permission code from menu and operation
 * @param {string} menu - The menu identifier
 * @param {string} operation - The operation type
 * @returns {string} Generated permission code
 */
export const generatePermissionCode = (menu, operation) => {
  return `${menu}:${operation}`.toUpperCase();
};

/**
 * Check if the current user is a super admin
 * @returns {boolean} Whether the user is a super admin
 */
export const isSuperAdmin = () => {
  const userPermissions = getUserPermissions();
  return userPermissions.includes('*'); // '*' represents all permissions for super admin
};

/**
 * Filter menu items based on user permissions
 * @param {Array<Object>} menuItems - List of menu items
 * @returns {Array<Object>} Filtered menu items the user has access to
 */
export const filterMenuByPermissions = (menuItems) => {
  if (isSuperAdmin()) {
    return menuItems; // Super admin can see all menu items
  }

  return menuItems.filter(item => {
    // If the menu item has required permissions, check if user has them
    if (item.requiredPermissions && item.requiredPermissions.length > 0) {
      return item.requiredPermissions.some(perm => {
        const [menu, operation] = perm.split(':');
        return hasPermission(menu, operation);
      });
    }
    // If no specific permissions required, allow access
    return true;
  });
};

/**
 * Check if user can perform multiple operations
 * @param {Array<{menu: string, operation: string}>} permissionChecks - List of permission checks
 * @returns {boolean} Whether the user has all required permissions
 */
export const hasMultiplePermissions = (permissionChecks) => {
  return permissionChecks.every(check => hasPermission(check.menu, check.operation));
};