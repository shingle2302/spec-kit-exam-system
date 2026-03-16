-- 第一阶段：数据权限系统数据库迁移脚本

-- 教师班级关联表
CREATE TABLE IF NOT EXISTS teacher_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    subject_id BIGINT COMMENT '科目ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_class (user_id, class_id),
    KEY idx_user_id (user_id),
    KEY idx_class_id (class_id),
    KEY idx_subject_id (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师班级关联表';

-- 数据权限配置表
CREATE TABLE IF NOT EXISTS data_permission_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_type VARCHAR(50) NOT NULL COMMENT '权限类型：ALL, DEPT, CUSTOM',
    dept_ids TEXT COMMENT '部门ID列表，JSON格式',
    custom_sql TEXT COMMENT '自定义SQL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    module VARCHAR(50) COMMENT '模块名称',
    operation VARCHAR(50) COMMENT '操作类型',
    description VARCHAR(255) COMMENT '操作描述',
    request_ip VARCHAR(50) COMMENT '请求IP',
    request_url VARCHAR(255) COMMENT '请求URL',
    request_method VARCHAR(10) COMMENT '请求方法',
    status VARCHAR(20) COMMENT '操作状态：SUCCESS, FAILURE',
    duration BIGINT COMMENT '执行时长(ms)',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 为用户表添加角色字段
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) COMMENT '用户角色：ADMIN, TEACHER, STUDENT';
ALTER TABLE users ADD COLUMN IF NOT EXISTS id_card VARCHAR(100) COMMENT '身份证号(加密)';
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(100) COMMENT '手机号(加密)';

-- 创建索引优化查询性能
CREATE INDEX idx_user_role ON users(role);
CREATE INDEX idx_user_id_card ON users(id_card);
CREATE INDEX idx_user_phone ON users(phone);