-- Migration to initialize built-in 'admin' user with default password

-- Create the super admin role (H2 compatible MERGE syntax)
MERGE INTO roles (id, name, description, permissions, is_super_admin_role, is_active, created_at, updated_at) 
KEY (id)
VALUES ('00000000-0000-0000-0000-000000000001', 'Super Admin', 'System super administrator with all privileges', 
        '{"users":{"read":true,"create":true,"update":true,"delete":true,"unlock":true},"roles":{"read":true,"create":true,"update":true,"delete":true},"system":{"admin":true,"super_admin":true,"audit":true}}',
        true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Create the built-in admin user with default password 'admin' (BCrypt hash)
MERGE INTO users (id, username, password_hash, phone, email, status, role_id, created_at, updated_at, last_login_at, password_changed_at, failed_login_attempts, locked_until, is_super_admin) 
KEY (id)
VALUES ('00000000-0000-0000-0000-000000000001', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqHPZv4rR4t3QFVHFdnAHhVEe.u3m', '+10000000000', 'admin@system.local', 'ACTIVE', '00000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), NULL, CURRENT_TIMESTAMP(), 0, NULL, true);