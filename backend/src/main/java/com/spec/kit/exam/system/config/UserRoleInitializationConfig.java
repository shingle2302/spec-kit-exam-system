package com.spec.kit.exam.system.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.mapper.RoleMapper;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import java.time.LocalDateTime;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE) // Run first before other initializations
public class UserRoleInitializationConfig implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    public void run(String... args) throws Exception {
        // Create default roles if they don't exist
        createDefaultRolesIfNeeded();
        
        // Create default admin user if it doesn't exist
        createDefaultAdminAccount();
    }

    private void createDefaultRolesIfNeeded() {
        // Check if admin role already exists
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.eq("name", "SUPER_ADMIN");
        Role existingAdminRole = roleMapper.selectOne(roleQuery);
        
        if (existingAdminRole == null) {
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Administrator with broad system access");
            adminRole.setIsSuperAdminRole(true);
            adminRole.setIsActive(true);
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRole.setUpdatedAt(LocalDateTime.now());
            
            roleMapper.insert(adminRole);
            System.out.println("SUPER ADMIN role created.");
        }
        
        // Check if user role already exists
        QueryWrapper<Role> userRoleQuery = new QueryWrapper<>();
        userRoleQuery.eq("name", "USER");
        Role existingUserRole = roleMapper.selectOne(userRoleQuery);
        
        if (existingUserRole == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Regular user with basic access");
            userRole.setIsSuperAdminRole(false);
            userRole.setIsActive(true);
            userRole.setCreatedAt(LocalDateTime.now());
            userRole.setUpdatedAt(LocalDateTime.now());
            
            roleMapper.insert(userRole);
            System.out.println("Default USER role created.");
        }
    }


    /**
     * Create default admin account if it doesn't exist
     */
    private void createDefaultAdminAccount() {
        // Check if admin user already exists
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("username", "admin");
        User existingUser = userMapper.selectOne(userQuery);
        
        // Get the SUPER_ADMIN role
        QueryWrapper<Role> adminRoleQuery = new QueryWrapper<>();
        adminRoleQuery.eq("name", "SUPER_ADMIN");
        Role adminRole = roleMapper.selectOne(adminRoleQuery);
        
        if (existingUser != null) {
            // Update existing admin user to be super admin if not already
            if (existingUser.getIsSuperAdmin() == null || !existingUser.getIsSuperAdmin()) {
                existingUser.setIsSuperAdmin(true);
                existingUser.setUpdatedAt(LocalDateTime.now());
                userMapper.updateById(existingUser);
                System.out.println("==> Existing admin user upgraded to SUPER ADMIN. Username: admin");
            } else {
                System.out.println("==> Admin user already exists as super admin.");
            }
            return; // Admin user already exists
        }
        
        User adminUser = new User();
        adminUser.setUsername("admin");
        // Using pre-hashed password for 'admin' to avoid validation issues
        adminUser.setPasswordHash(passwordUtil.hashPassword("Admin@123")); // Password that meets complexity
        adminUser.setEmail("admin@system.local");
        adminUser.setPhone("+10000000000");
        adminUser.setStatus("ACTIVE");
        adminUser.setIsSuperAdmin(true); // Set admin as super admin
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        adminUser.setFailedLoginAttempts(0);
        
        if (adminRole != null) {
            adminUser.setRoleId(adminRole.getId());
        }
        
        userMapper.insert(adminUser);
        System.out.println("==> Default SUPER ADMIN user created successfully. Username: admin, Password: Admin@123");
    }

}
