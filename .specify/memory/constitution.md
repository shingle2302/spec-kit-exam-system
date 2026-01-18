<!-- 
Sync Impact Report:
- Version change: 1.1.0 → 1.2.0
- Modified principles: Architecture and Technology Standards (added package structure requirements)
- Added sections: Package Structure Standards
- Removed sections: N/A
- Templates requiring updates: ⚠ pending - .specify/templates/plan-template.md, .specify/templates/spec-template.md, .specify/templates/tasks-template.md, .specify/templates/commands/*.md
- Follow-up TODOs: None
-->
# Spec-Kit Exam System Constitution

## Core Principles

### Code Quality Standards
All code must adhere to established style guides including the Alibaba Java Coding Guidelines and pass static analysis checks (SonarQube) before merging. Code reviews are mandatory for all changes, with emphasis on maintainability, readability, and adherence to architectural patterns. Code complexity must comply with SonarQube rules and thresholds. Complexity must be justified and documented, with preference given to simple, clear solutions over clever implementations.

### Testing Standards (NON-NEGOTIABLE)
Comprehensive test coverage is mandatory: unit tests for all business logic, integration tests for service interactions, and end-to-end tests for critical user flows. Test-driven development is encouraged for new features. All tests must pass before code can be merged, with minimum 80% coverage thresholds for new code.

### User Experience Consistency
User interfaces must follow consistent design patterns and component libraries across all applications. All user-facing features must maintain visual and behavioral consistency with established design systems. Accessibility standards (WCAG 2.1 AA) must be met for all new features.

### Performance Requirements
All system responses must meet defined SLAs: API endpoints under 500ms, UI interactions under 100ms, and page load times under 3 seconds. Performance benchmarks must be established and monitored for all critical paths. Resource utilization must remain within defined thresholds under expected load conditions.

### Architecture and Technology Standards
Frontend applications must use Vue3 with Ant Design Vue components for consistent UI patterns. Backend services must use Spring Boot with MyBatisPlus for database operations. Package structure must follow com.spec.kit.exam.system convention. Support dual database configuration (H2 for development, PostgreSQL for production) with seamless switching capability. All data must be simultaneously stored in Elasticsearch for fast search queries. Redis caching must be implemented for frequently accessed data to improve response times.

### API Response Standards
All API responses must follow the standardized format {data, code, msg} where:
- data: Contains the actual response payload or null if no data
- code: Numeric status code (200 for success, specific error codes for failures)
- msg: Human-readable message describing the result or error

### Request Method Standards
Pagination queries and all complex requests must use POST method with parameters passed in the request body rather than URL query parameters. This ensures consistency and allows handling of complex filter objects.

### Package Structure Standards
All backend code must follow the package structure convention: com.spec.kit.exam.system. DTO (Data Transfer Objects) must be used for frontend-backend interaction objects. Entity classes must represent database entities. Existing project structure must be preserved and not modified unnecessarily.

## Technology Stack Requirements

The project must use the following technology stack:
- Frontend: Vue3 with TypeScript, Ant Design Vue and Element Plus for UI components
- Backend: Spring Boot with Java, MyBatisPlus for ORM
- Databases: Support both H2 (development) and PostgreSQL (production)
- Search: Elasticsearch for advanced querying and indexing
- Caching: Redis for performance optimization
- Testing: JUnit for backend, Vitest/Jest for frontend, Cypress for E2E tests
- Build tools: Maven for backend, Vite for frontend
- Code Quality: SonarQube for static analysis and complexity monitoring

## Development Workflow

All development must follow a structured workflow including:
- Feature branches created from main
- Pull requests with mandatory code reviews
- Automated testing and quality checks
- Pre-commit hooks enforcing code standards
- Continuous integration with build verification
- Documentation updates for new features
- Security scanning for dependencies
- Static analysis compliance with SonarQube rules


# 项目规范手册 (Constitution)

## 1. 技术栈规范

### 1.1 后端技术栈
- **编程语言**: Java 17
- **框架**: Spring Boot
- **持久层**: MyBatisPlus
- **数据库**:
    - 开发环境: H2 内存数据库
    - 生产环境: PostgreSQL
- **缓存**: Redis
- **搜索**: Elasticsearch
- **安全**: Spring Security

### 1.2 前端技术栈
- **框架**: Vue 3
- **语言**: TypeScript
- **UI组件库**: Ant Design Vue
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios

## 2. 接口响应体规范

### 2.1 统一响应格式
所有API接口返回统一的数据结构：

```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

其中：
- `data`: 响应数据载体，不同类型接口返回不同数据结构
- `code`: 数字型状态码，0表示成功，非0表示错误
- `msg`: 人类可读的消息描述

### 2.2 成功响应示例
```json
{
  "data": {
    "id": "user_001",
    "username": "john_doe",
    "email": "john@example.com"
  },
  "code": 0,
  "msg": "User retrieved successfully"
}
```

### 2.3 错误响应示例
```json
{
  "data": null,
  "code": 1001,
  "msg": "Validation error occurred"
}
```

## 3. 分页查询规范

### 3.1 分页查询方式
- **方法**: POST (而非GET)，请求体携带分页参数
- **原因**: 支持复杂的过滤条件对象，保持API一致性

### 3.2 分页请求参数
分页参数封装在 `PageRequestDTO` 中：

```json
{
  "page": 1,
  "size": 10,
  "sortField": "createTime",
  "sortOrder": "desc",
  "filters": {
    "status": "ACTIVE"
  }
}
```

参数说明：
- `page`: 页码，从1开始
- `size`: 每页大小，默认10，最大100
- `sortField`: 排序字段
- `sortOrder`: 排序方向，asc 或 desc
- `filters`: 额外过滤条件

### 3.3 分页响应结构
分页结果封装在 `PageResponseDTO` 中：

```json
{
  "data": {
    "records": [
      {
        "id": "item_001",
        "name": "Sample Item"
      }
    ],
    "total": 150,
    "current": 1,
    "size": 10,
    "pages": 15
  },
  "code": "0000",
  "msg": "Data retrieved successfully"
}
```

## 4. 错误码规范

### 4.1 错误码分类
按模块划分错误码范围：

| 范围 | 模块 | 示例 |
|------|------|------|
| 100X | 用户模块 | UserErrorCodeEnum |
| 200X | 角色模块 | RoleErrorCodeEnum |
| 300X | 菜单模块 | MenuErrorCodeEnum |
| 400X | 权限模块 | PermissionErrorCodeEnum |
| 500X | 通用模块 | CommonErrorCodeEnum |

### 4.2 通用错误码 (500X)
| 错误码  | 描述 | 说明      |
|------|------|---------|
| 0000 | SYSTEM_ERROR | 成功      |
| 5000 | SYSTEM_ERROR | 系统内部错误  |
| 5001 | VALIDATION_ERROR | 参数验证错误  |
| 5002 | AUTHORIZATION_ERROR | 授权错误    |
| 5003 | INVALID_PERMISSION_CONFIG | 无效的权限配置 |
| 5004 | INVALID_PERMISSION_CONFIG2 | 无效的权限配置 |
| 5005 | GENERAL_SERVER_ERROR | 一般服务器错误 |
| 5006 | RUNTIME_EXCEPTION | 运行时异常   |

### 4.3 用户模块错误码 (100X)
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 1001 | USER_NOT_FOUND | 用户未找到 |
| 1002 | FAILED_TO_UNLOCK_USER | 解锁用户失败 |
| 1003 | REGISTRATION_ERROR | 注册错误 |
| 1004 | AUTHENTICATION_FAILED | 认证失败 |
| 1005 | LOGOUT_FAILED | 登出失败 |
| 1006 | ACCOUNT_LOCKED | 账户已被锁定 |

### 4.4 角色模块错误码 (200X)
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 2001 | ROLE_NOT_FOUND | 角色未找到 |

### 4.5 菜单模块错误码 (300X)
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 3000 | MENU_NOT_FOUND | 菜单不存在 |
| 3001 | FAILED_TO_CREATE_MENU | 创建菜单失败 |
| 3002 | FAILED_TO_UPDATE_MENU | 更新菜单失败 |
| 3003 | FAILED_TO_DELETE_MENU | 删除菜单失败 |
| 3004 | MENU_ALREADY_EXISTS | 菜单已存在 |

### 4.6 权限模块错误码 (400X)
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 4001 | FAILED_TO_ASSIGN_PERMISSIONS | 分配权限失败 |
| 4002 | FAILED_TO_REMOVE_PERMISSIONS | 移除权限失败 |
| 4003 | FAILED_TO_UPDATE_PERMISSION | 更新权限失败 |
| 4004 | FAILED_TO_DELETE_PERMISSION | 删除权限失败 |
| 4005 | PERMISSION_NOT_FOUND | 权限未找到 |

## 5. 权限设计规范

### 5.1 权限控制方式
使用 `@PermissionRequired` 注解进行方法级权限控制：

```java
@PermissionRequired(
    menu = "user-management", 
    button = "create-user", 
    operation = "CREATE"
)
@PostMapping("/users")
public Result<User> createUser(@RequestBody User user) {
    // Implementation
}
```

### 5.2 权限验证流程
1. 请求到达控制器方法
2. AOP拦截器检查 `@PermissionRequired` 注解
3. 验证用户身份和权限
4. 允许或拒绝请求

### 5.3 权限模型
- 支持基于角色的访问控制（RBAC）
- 超级管理员账户可绕过所有权限检查
- 权限格式：`{menu}:{operation}` (如: `user-management:CREATE`)

### 5.4 特殊权限规则
- 超级管理员拥有所有权限，无需验证
- 普通用户需要对应的角色分配
- 权限验证在方法调用前完成

## 6. 界面风格规范

### 6.1 UI组件库
- **主框架**: Ant Design Vue
- **图标**: @ant-design/icons-vue
- **国际化**: 支持中文简体

### 6.2 页面布局
- 采用响应式设计
- 使用 `a-layout` 组件构建页面结构
- 左侧导航菜单，右侧内容区域
- 顶部面包屑导航

### 6.3 表单设计
- 使用 `a-form` 及相关组件
- 表单验证规则统一配置
- 支持实时验证和提交前验证

### 6.4 表格设计
- 使用 `a-table` 组件
- 支持分页、排序、筛选功能
- 操作列包含编辑、删除等常用按钮

### 6.5 国际化支持
- 默认使用中文界面
- 预留国际化扩展能力

## 7. 代码规范

### 7.1 命名规范
- 后端包名：`com.spec.kit.exam.system`
- 类名：大驼峰命名法 (PascalCase)
- 方法名：小驼峰命名法 (camelCase)
- 常量：全大写加下划线 (UPPER_SNAKE_CASE)

### 7.2 注释规范
- 类和方法：中文注释（业务相关）和英文注释（技术相关）
- 类和公共方法必须有JavaDoc注释
- 关键逻辑需要行内注释
- 使用中文注释（业务相关）和英文注释（技术相关）

### 7.3 异常处理
- 使用自定义错误码枚举
- 统一异常处理机制
- 不向客户端暴露系统内部详细错误信息

## 8. 数据库设计规范

### 8.1 命名规范
- 表名：复数形式，下划线分隔 (snake_case)
- 字段名：下划线分隔 (snake_case)
- 主键：统一使用 `id` 字段

### 8.2 通用字段
所有表包含以下审计字段：
- `create_time`: 创建时间
- `update_time`: 更新时间
- `created_by`: 创建者
- `updated_by`: 更新者

### 8.3 状态字段
- 状态字段使用枚举值（如: ACTIVE, INACTIVE, DELETED）
- 布尔字段使用 tinyint(1) 存储

## Governance

This constitution serves as the authoritative guide for all technical decisions within the project. All code changes must comply with these principles. Amendments to this constitution require approval from the technical steering committee and must include a migration plan for existing code. All team members are responsible for ensuring compliance with these standards during code reviews and development activities.

**Version**: 1.2.0 | **Ratified**: 2025-12-26 | **Last Amended**: 2026-01-18