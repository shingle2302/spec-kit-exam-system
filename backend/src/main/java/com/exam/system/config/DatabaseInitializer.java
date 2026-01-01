package com.exam.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 数据库初始化器，用于在应用启动时创建数据库表
 */
@Configuration
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Value("${exam.system.sample-data.enabled:true}")
    private boolean sampleDataEnabled;

    @PostConstruct
    public void initializeDatabase() {
        if (!sampleDataEnabled) {
            logger.info("Sample data is disabled, skipping database initialization");
            return;
        }

        logger.info("Starting database initialization...");

        try (Connection connection = dataSource.getConnection()) {
            // 检查表是否已存在
            if (tablesExist(connection)) {
                logger.info("Tables already exist, skipping initialization");
                return;
            }

            // 执行数据库初始化脚本
            executeInitializationScript(connection);

            logger.info("Database initialization completed successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
        }
    }

    private boolean tablesExist(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 检查几个主要表是否存在
            return tableExists(metaData, "users") && 
                   tableExists(metaData, "students") && 
                   tableExists(metaData, "teachers") && 
                   tableExists(metaData, "questions") && 
                   tableExists(metaData, "tests");
        } catch (SQLException e) {
            logger.warn("Could not check if tables exist", e);
            return false;
        }
    }

    private boolean tableExists(DatabaseMetaData metaData, String tableName) throws SQLException {
        try (var rs = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            if (rs.next()) {
                return true;
            }
        }
        
        // Also check with lowercase name (for some databases)
        try (var rs = metaData.getTables(null, null, tableName.toLowerCase(), new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private void executeInitializationScript(Connection connection) throws SQLException {
        String script = readScriptFromResource();
        if (script != null) {
            try (Statement statement = connection.createStatement()) {
                // 分割并执行SQL语句
                String[] statements = script.split(";");
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        try {
                            statement.execute(sql);
                            logger.debug("Executed: {}", sql);
                        } catch (SQLException e) {
                            logger.warn("Failed to execute statement: {} - {}", sql, e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private String readScriptFromResource() {
        try {
            ClassPathResource resource = new ClassPathResource("db/schema.sql");
            if (resource.exists()) {
                try (Reader reader = new InputStreamReader(resource.getInputStream())) {
                    Scanner scanner = new Scanner(reader);
                    scanner.useDelimiter("\\A");
                    return scanner.hasNext() ? scanner.next() : "";
                }
            } else {
                logger.warn("Database schema file not found: classpath:db/schema.sql");
                // 如果没有找到外部脚本，则使用默认的SQL
                return getDefaultSchema();
            }
        } catch (Exception e) {
            logger.error("Failed to read database schema", e);
            return getDefaultSchema();
        }
    }

    private String getDefaultSchema() {
        // 根据模型类定义生成的默认表结构
        StringBuilder schema = new StringBuilder();
        
        // 用户表
        schema.append("CREATE TABLE IF NOT EXISTS users (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  username VARCHAR(255) NOT NULL UNIQUE,\n")
              .append("  email VARCHAR(255),\n")
              .append("  password_hash VARCHAR(255) NOT NULL,\n")
              .append("  first_name VARCHAR(255),\n")
              .append("  last_name VARCHAR(255),\n")
              .append("  role VARCHAR(20) NOT NULL,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  is_active BOOLEAN DEFAULT TRUE,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n")
              .append(");\n\n");

        // 学生表
        schema.append("CREATE TABLE IF NOT EXISTS students (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  student_id VARCHAR(255) NOT NULL UNIQUE,\n")
              .append("  grade_id BIGINT,\n")
              .append("  class_id BIGINT,\n")
              .append("  parent_id BIGINT,\n")
              .append("  user_id BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (user_id) REFERENCES users(id)\n")
              .append(");\n\n");

        // 教师表
        schema.append("CREATE TABLE IF NOT EXISTS teachers (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  teacher_id VARCHAR(255) NOT NULL UNIQUE,\n")
              .append("  department VARCHAR(255),\n")
              .append("  user_id BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (user_id) REFERENCES users(id)\n")
              .append(");\n\n");

        // 管理员表
        schema.append("CREATE TABLE IF NOT EXISTS administrators (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  admin_level VARCHAR(20) DEFAULT 'SCHOOL',\n")
              .append("  user_id BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (user_id) REFERENCES users(id)\n")
              .append(");\n\n");

        // 科目表
        schema.append("CREATE TABLE IF NOT EXISTS subjects (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  name VARCHAR(255) NOT NULL,\n")
              .append("  description TEXT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE\n")
              .append(");\n\n");

        // 年级表
        schema.append("CREATE TABLE IF NOT EXISTS grades (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  name VARCHAR(255) NOT NULL,\n")
              .append("  description TEXT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE\n")
              .append(");\n\n");

        // 班级表
        schema.append("CREATE TABLE IF NOT EXISTS classes (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  name VARCHAR(255) NOT NULL,\n")
              .append("  grade_id BIGINT,\n")
              .append("  teacher_id BIGINT,\n")
              .append("  subject_id BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (grade_id) REFERENCES grades(id),\n")
              .append("  FOREIGN KEY (teacher_id) REFERENCES teachers(id),\n")
              .append("  FOREIGN KEY (subject_id) REFERENCES subjects(id)\n")
              .append(");\n\n");

        // 班级注册表
        schema.append("CREATE TABLE IF NOT EXISTS class_enrollments (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  class_id BIGINT NOT NULL,\n")
              .append("  student_id BIGINT NOT NULL,\n")
              .append("  enrollment_date DATE DEFAULT CURRENT_DATE,\n")
              .append("  status VARCHAR(20) DEFAULT 'ACTIVE',\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (class_id) REFERENCES classes(id),\n")
              .append("  FOREIGN KEY (student_id) REFERENCES students(id),\n")
              .append("  UNIQUE (class_id, student_id)\n")
              .append(");\n\n");

        // 题目表
        schema.append("CREATE TABLE IF NOT EXISTS questions (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  question_text TEXT NOT NULL,\n")
              .append("  question_type VARCHAR(50) NOT NULL,\n")
              .append("  subject_id BIGINT,\n")
              .append("  grade_id BIGINT,\n")
              .append("  knowledge_point VARCHAR(255),\n")
              .append("  standard_explanation TEXT,\n")
              .append("  difficulty_level INT DEFAULT 1,\n")
              .append("  created_by BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  is_active BOOLEAN DEFAULT TRUE,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (subject_id) REFERENCES subjects(id),\n")
              .append("  FOREIGN KEY (grade_id) REFERENCES grades(id),\n")
              .append("  FOREIGN KEY (created_by) REFERENCES users(id)\n")
              .append(");\n\n");

        // 答案选项表
        schema.append("CREATE TABLE IF NOT EXISTS answer_options (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  question_id BIGINT NOT NULL,\n")
              .append("  option_text TEXT NOT NULL,\n")
              .append("  is_correct BOOLEAN DEFAULT FALSE,\n")
              .append("  option_order INT DEFAULT 0,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (question_id) REFERENCES questions(id)\n")
              .append(");\n\n");

        // 测试表
        schema.append("CREATE TABLE IF NOT EXISTS tests (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  title VARCHAR(255) NOT NULL,\n")
              .append("  description TEXT,\n")
              .append("  grade_id BIGINT,\n")
              .append("  subject_id BIGINT,\n")
              .append("  time_limit_minutes INT DEFAULT 60,\n")
              .append("  is_active BOOLEAN DEFAULT TRUE,\n")
              .append("  is_published BOOLEAN DEFAULT FALSE,\n")
              .append("  publish_date TIMESTAMP,\n")
              .append("  created_by BIGINT,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (grade_id) REFERENCES grades(id),\n")
              .append("  FOREIGN KEY (subject_id) REFERENCES subjects(id),\n")
              .append("  FOREIGN KEY (created_by) REFERENCES users(id)\n")
              .append(");\n\n");

        // 测试题目关联表
        schema.append("CREATE TABLE IF NOT EXISTS test_questions (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  test_id BIGINT NOT NULL,\n")
              .append("  question_id BIGINT NOT NULL,\n")
              .append("  question_order INT DEFAULT 0,\n")
              .append("  points INT DEFAULT 1,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (test_id) REFERENCES tests(id),\n")
              .append("  FOREIGN KEY (question_id) REFERENCES questions(id),\n")
              .append("  UNIQUE (test_id, question_id)\n")
              .append(");\n\n");

        // 测试分配表
        schema.append("CREATE TABLE IF NOT EXISTS test_assignments (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  test_id BIGINT NOT NULL,\n")
              .append("  student_id BIGINT NOT NULL,\n")
              .append("  assigned_by BIGINT,\n")
              .append("  assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  due_date TIMESTAMP,\n")
              .append("  status VARCHAR(20) DEFAULT 'ASSIGNED',\n")
              .append("  completed_at TIMESTAMP NULL,\n")
              .append("  submitted_at TIMESTAMP NULL,\n")
              .append("  graded_at TIMESTAMP NULL,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (test_id) REFERENCES tests(id),\n")
              .append("  FOREIGN KEY (student_id) REFERENCES students(id),\n")
              .append("  FOREIGN KEY (assigned_by) REFERENCES users(id)\n")
              .append(");\n\n");

        // 学生答题表
        schema.append("CREATE TABLE IF NOT EXISTS student_responses (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  student_id BIGINT NOT NULL,\n")
              .append("  test_id BIGINT NOT NULL,\n")
              .append("  question_id BIGINT NOT NULL,\n")
              .append("  response_text TEXT,\n")
              .append("  selected_option_index INT,\n")
              .append("  response_time INT,\n")
              .append("  is_correct BOOLEAN,\n")
              .append("  manual_grade INT,\n")
              .append("  teacher_grade INT,\n")
              .append("  teacher_comments TEXT,\n")
              .append("  submitted_at TIMESTAMP,\n")
              .append("  graded_at TIMESTAMP,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (student_id) REFERENCES students(id),\n")
              .append("  FOREIGN KEY (test_id) REFERENCES tests(id),\n")
              .append("  FOREIGN KEY (question_id) REFERENCES questions(id)\n")
              .append(");\n\n");

        // 错题本表
        schema.append("CREATE TABLE IF NOT EXISTS error_books (\n")
              .append("  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n")
              .append("  student_id BIGINT NOT NULL,\n")
              .append("  question_id BIGINT NOT NULL,\n")
              .append("  is_mastered BOOLEAN DEFAULT FALSE,\n")
              .append("  correct_in_a_row INT DEFAULT 0,\n")
              .append("  total_attempts INT DEFAULT 0,\n")
              .append("  last_attempted_at TIMESTAMP,\n")
              .append("  first_incorrect_at TIMESTAMP,\n")
              .append("  mastered_at TIMESTAMP NULL,\n")
              .append("  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  creator VARCHAR(255),\n")
              .append("  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  updater VARCHAR(255),\n")
              .append("  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n")
              .append("  deleted BOOLEAN DEFAULT FALSE,\n")
              .append("  FOREIGN KEY (student_id) REFERENCES students(id),\n")
              .append("  FOREIGN KEY (question_id) REFERENCES questions(id),\n")
              .append("  UNIQUE (student_id, question_id)\n")
              .append(");\n\n");

        return schema.toString();
    }
}