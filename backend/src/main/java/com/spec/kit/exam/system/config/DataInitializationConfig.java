package com.spec.kit.exam.system.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.entity.Role;
import com.spec.kit.exam.system.mapper.UserMapper;
import com.spec.kit.exam.system.mapper.RoleMapper;
import com.spec.kit.exam.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataInitializationConfig implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    public void run(String... args) throws Exception {
        // Create super admin role if it doesn't exist
        Role superAdminRole = createSuperAdminRoleIfNeeded();
        
        // Create admin user if it doesn't exist
        createAdminUserIfNeeded(superAdminRole);
    }

    private Role createSuperAdminRoleIfNeeded() {
        // Check if super admin role already exists
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.eq("name", "Super Admin");
        Role existingRole = roleMapper.selectOne(roleQuery);
        
        if (existingRole != null) {
            return existingRole;
        }
        
        Role superAdminRole = new Role();
        superAdminRole.setName("Super Admin");
        superAdminRole.setDescription("Has full system access with all privileges");
        superAdminRole.setIsSuperAdminRole(true);
        superAdminRole.setIsActive(true);
        superAdminRole.setPermissions(generateSuperAdminPermissions());
        superAdminRole.setCreatedAt(LocalDateTime.now());
        superAdminRole.setUpdatedAt(LocalDateTime.now());
        
        roleMapper.insert(superAdminRole);
        return superAdminRole;
    }

    private void createAdminUserIfNeeded(Role superAdminRole) {
        // Check if admin user already exists
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("username", "admin");
        User existingUser = userMapper.selectOne(userQuery);
        
        if (existingUser != null) {
            return; // Admin user already exists
        }
        
        User adminUser = new User();
        adminUser.setUsername("admin");
        // Using pre-hashed password for 'admin' to avoid validation issues
        adminUser.setPasswordHash(passwordUtil.hashPassword("Admin@123")); // Password that meets complexity
        adminUser.setEmail("admin@system.local");
        adminUser.setPhone("+10000000000");
        adminUser.setStatus("ACTIVE");
        adminUser.setIsSuperAdmin(true);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        adminUser.setFailedLoginAttempts(0);
        
        if (superAdminRole != null) {
            adminUser.setRoleId(superAdminRole.getId());
        }
        
        userMapper.insert(adminUser);
        System.out.println("==> Admin user created successfully. Username: admin, Password: Admin@123");
    }

    private String generateSuperAdminPermissions() {
        // Return a JSON string representing super admin permissions
        return "{\n" +
               "  \"users\": {\n" +
               "    \"read\": true,\n" +
               "    \"create\": true,\n" +
               "    \"update\": true,\n" +
               "    \"delete\": true,\n" +
               "    \"unlock\": true\n" +
               "  },\n" +
               "  \"roles\": {\n" +
               "    \"read\": true,\n" +
               "    \"create\": true,\n" +
               "    \"update\": true,\n" +
               "    \"delete\": true\n" +
               "  },\n" +
               "  \"system\": {\n" +
               "    \"admin\": true,\n" +
               "    \"super_admin\": true,\n" +
               "    \"audit\": true\n" +
               "  }\n" +
               "}";
    }
}