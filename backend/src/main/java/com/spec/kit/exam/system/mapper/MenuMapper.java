package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * Mapper interface for Menu entity operations
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    /**
     * Select menu by parent ID
     * @param parentId The parent ID to get menus for
     * @return List of menus with the specified parent
     */
    @Select("SELECT * FROM menus WHERE parent_id = #{parentId} ORDER BY order_num")
    List<Menu> selectByParentId(@Param("parentId") String parentId);
    
    /**
     * Select all root menus (menus without parents)
     * @return List of root menus
     */
    @Select("SELECT * FROM menus WHERE parent_id IS NULL ORDER BY order_num")
    List<Menu> selectRootMenus();
    
    /**
     * Select menus by role ID (through menu_roles table)
     * @param roleId The role ID to get menus for
     * @return List of menus accessible by the role
     */
    @Select("SELECT m.* FROM menus m " +
            "INNER JOIN menu_roles mr ON m.id = mr.menu_id " +
            "WHERE mr.role_id = #{roleId} AND m.status = 'ACTIVE' " +
            "ORDER BY m.order_num")
    List<Menu> selectByRoleId(@Param("roleId") String roleId);
    
    /**
     * Check if a menu exists by name and parent
     * @param name The menu name to check
     * @param parentId The parent ID to check under (can be null for root menus)
     * @return count of matching menus
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM menus WHERE name = #{name} " +
            "<if test='parentId != null'>AND parent_id = #{parentId}</if>" +
            "<if test='parentId == null'>AND parent_id IS NULL</if>" +
            "</script>")
    int countByNameAndParent(@Param("name") String name, @Param("parentId") String parentId);
}