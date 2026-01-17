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
public class DataInitializationConfig implements CommandLineRunner {

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
        createDefaultAdminUserIfNeeded();
    }

    private void createDefaultRolesIfNeeded() {
        // Check if admin role already exists
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.eq("name", "ADMIN");
        Role existingAdminRole = roleMapper.selectOne(roleQuery);
        
        if (existingAdminRole == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator with broad system access");
            adminRole.setIsSuperAdminRole(false);
            adminRole.setIsActive(true);
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRole.setUpdatedAt(LocalDateTime.now());
            
            roleMapper.insert(adminRole);
            System.out.println("Default ADMIN role created.");
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

    private void createDefaultAdminUserIfNeeded() {
        // Check if admin user already exists
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("username", "admin");
        User existingUser = userMapper.selectOne(userQuery);
        
        if (existingUser != null) {
            return; // Admin user already exists
        }
        
        // Get the ADMIN role
        QueryWrapper<Role> adminRoleQuery = new QueryWrapper<>();
        adminRoleQuery.eq("name", "ADMIN");
        Role adminRole = roleMapper.selectOne(adminRoleQuery);
        
        User adminUser = new User();
        adminUser.setUsername("admin");
        // Using pre-hashed password for 'admin' to avoid validation issues
        adminUser.setPasswordHash(passwordUtil.hashPassword("Admin@123")); // Password that meets complexity
        adminUser.setEmail("admin@system.local");
        adminUser.setPhone("+10000000000");
        adminUser.setStatus("ACTIVE");
        adminUser.setIsSuperAdmin(false); // Regular admin, not super admin
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        adminUser.setFailedLoginAttempts(0);
        
        if (adminRole != null) {
            adminUser.setRoleId(adminRole.getId());
        }
        
        userMapper.insert(adminUser);
        System.out.println("==> Default admin user created successfully. Username: admin, Password: Admin@123");
    }
}
