package com.exam.system.config;

import com.exam.system.model.User;
import com.exam.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 初始化默认管理员用户的组件
 */
@Component
public class DefaultAdminInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${exam.system.default-admin.username:admin}")
    private String defaultAdminUsername;

    @Value("${exam.system.default-admin.password:change-me}")
    private String defaultAdminPassword;

    @Value("${exam.system.sample-data.enabled:true}")
    private boolean sampleDataEnabled;

    @PostConstruct
    public void initializeDefaultAdmin() {
        if (!sampleDataEnabled) {
            logger.info("Sample data is disabled, skipping default admin initialization");
            return;
        }

        // 检查是否已存在管理员用户
        User existingAdmin = userRepository.findByUsername(defaultAdminUsername);
        if (existingAdmin != null) {
            logger.info("Default admin user already exists: {}", defaultAdminUsername);
            return;
        }

        // 创建默认管理员用户
        User adminUser = new User();
        adminUser.setUsername(defaultAdminUsername);
        adminUser.setPasswordHash(passwordEncoder.encode(defaultAdminPassword));
        adminUser.setFirstName("Default");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin@exam-system.local");
        adminUser.setRole(User.Role.ADMIN);
        adminUser.setIsActive(true);

        try {
            userRepository.insert(adminUser);
            logger.info("Default admin user created successfully: {}", defaultAdminUsername);
        } catch (Exception e) {
            logger.error("Failed to create default admin user: {}", e.getMessage(), e);
        }
    }
}