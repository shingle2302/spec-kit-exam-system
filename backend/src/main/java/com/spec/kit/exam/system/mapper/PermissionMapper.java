package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * Mapper interface for Permission entity operations
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    /**
     * Select permissions by role ID (through role_permissions table)
     * @param roleId The role ID to get permissions for
     * @return List of permissions for the role
     */
    @Select("SELECT p.* FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.status = 'ACTIVE'")
    List<Permission> selectByRoleId(@Param("roleId") String roleId);
    
    /**
     * Select permissions by menu ID
     * @param menuId The menu ID to get permissions for
     * @return List of permissions for the menu
     */
    @Select("SELECT * FROM permissions WHERE menu_id = #{menuId}")
    List<Permission> selectByMenuId(@Param("menuId") String menuId);
    
    /**
     * Select permission by permission code
     * @param permissionCode The permission code to search for
     * @return The permission or null if not found
     */
    @Select("SELECT * FROM permissions WHERE permission_code = #{permissionCode}")
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * Select permissions by menu ID and operation type
     * @param menuId The menu ID
     * @param operationType The operation type
     * @return List of matching permissions
     */
    @Select("SELECT * FROM permissions WHERE menu_id = #{menuId} AND operation_type = #{operationType}")
    List<Permission> selectByMenuIdAndOperation(@Param("menuId") String menuId, 
                                                @Param("operationType") String operationType);
    
    /**
     * Check if user has specific permission through their role
     * @param userId The user ID
     * @param permissionCode The permission code to check
     * @return count of matching permissions (0 = no permission, >0 = has permission)
     */
    @Select("SELECT COUNT(*) FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN users u ON u.role_id = rp.role_id " +
            "WHERE u.id = #{userId} AND p.permission_code = #{permissionCode} AND p.status = 'ACTIVE'")
    int countUserPermission(@Param("userId") String userId, @Param("permissionCode") String permissionCode);
    
    /**
     * Select all permission codes for a user through their role
     * @param userId The user ID
     * @return List of permission codes
     */
    @Select("SELECT p.permission_code FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN users u ON u.role_id = rp.role_id " +
            "WHERE u.id = #{userId} AND p.status = 'ACTIVE'")
    List<String> selectPermissionCodesByUserId(@Param("userId") String userId);
}