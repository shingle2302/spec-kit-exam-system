package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * Mapper interface for RolePermission entity operations
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    
    /**
     * Select role-permission associations by role ID
     * @param roleId The role ID
     * @return List of role-permission associations
     */
    @Select("SELECT * FROM role_permissions WHERE role_id = #{roleId}")
    List<RolePermission> selectByRoleId(@Param("roleId") String roleId);
    
    /**
     * Select role-permission associations by permission ID
     * @param permissionId The permission ID
     * @return List of role-permission associations
     */
    @Select("SELECT * FROM role_permissions WHERE permission_id = #{permissionId}")
    List<RolePermission> selectByPermissionId(@Param("permissionId") String permissionId);
    
    /**
     * Delete all permissions for a role
     * @param roleId The role ID
     * @return Number of affected rows
     */
    @Delete("DELETE FROM role_permissions WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") String roleId);
    
    /**
     * Delete all role associations for a permission
     * @param permissionId The permission ID
     * @return Number of affected rows
     */
    @Delete("DELETE FROM role_permissions WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(@Param("permissionId") String permissionId);
    
    /**
     * Check if a role-permission association exists
     * @param roleId The role ID
     * @param permissionId The permission ID
     * @return count of matching associations
     */
    @Select("SELECT COUNT(*) FROM role_permissions WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int countByRoleAndPermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);
}
