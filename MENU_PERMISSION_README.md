# 菜单权限管理系统

## 概述

菜单权限管理系统是一个基于RBAC（基于角色的访问控制）模型的权限管理解决方案，提供了层级菜单管理、细粒度权限控制和灵活的角色分配功能。

## 核心功能

### 1. 菜单管理
- 支持层级菜单结构（父子关系）
- 可自定义菜单名称、路径、组件、图标等属性
- 提供菜单的增删改查功能

### 2. 权限管理
- 细粒度权限控制（QUERY、CREATE、UPDATE、DELETE、UNLOCK、LOCK）
- 权限与菜单绑定，便于管理
- 支持权限的动态分配和回收

### 3. 角色管理
- 支持创建和管理用户角色
- 可为角色分配不同菜单权限
- 内置超级管理员角色

## 技术架构

### 后端
- **框架**: Spring Boot + MyBatis-Plus
- **权限控制**: Spring AOP + 自定义注解
- **数据库**: H2（开发环境）或其他关系型数据库
- **API响应**: 统一的Result格式 `{data, code, msg}`

### 前端
- **框架**: Vue 3 + TypeScript
- **UI组件**: Ant Design Vue
- **状态管理**: Pinia
- **路由**: Vue Router

## 主要特性

### 1. 注解驱动权限控制
使用 `@PermissionRequired` 注解实现声明式权限控制：

```java
@PermissionRequired(menu = "menu-management", operation = "CREATE")
@PostMapping("/create")
public Result<Menu> createMenu(@RequestBody Menu menu) {
    // 创建菜单逻辑
}
```

### 2. 自动权限初始化
系统启动时自动扫描注解并创建对应权限：
- 扫描所有带有 `@PermissionRequired` 注解的方法
- 自动创建缺失的菜单和权限记录
- 验证必需权限的存在性

### 3. 统一响应格式
所有API接口返回统一的Result格式：
```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

### 4. 超级管理员特权
- 超级管理员账户拥有所有权限
- 可以绕过权限检查
- 权限验证时优先检查超级管理员身份

## 数据库设计

### 核心表结构
- `menus` - 菜单表，存储系统菜单信息
- `permissions` - 权限表，存储具体权限信息
- `role_permissions` - 角色权限关联表，多对多关系
- `roles` - 角色表，存储角色信息
- `users` - 用户表，存储用户信息

### 表关系
```
Users --< UserRoles >-- Roles --< RolePermissions >-- Permissions >-- Menus
```

## 前端组件

### 1. 菜单管理组件
- `MenuManagementView` - 主管理界面
- `MenuTree` - 树形菜单展示
- `MenuItemEditor` - 菜单项编辑器

### 2. 权限管理组件
- `RolePermissionEditor` - 角色权限编辑器
- 权限搜索和批量分配功能

## API接口

### 菜单相关
- `GET /api/menu/tree` - 获取菜单树结构
- `POST /api/menu/create` - 创建菜单
- `PUT /api/menu/update` - 更新菜单
- `DELETE /api/menu/delete/{id}` - 删除菜单
- `GET /api/menu/list` - 获取所有菜单

### 权限相关
- `GET /api/permission/role/{roleId}` - 获取角色权限
- `POST /api/permission/assign` - 分配权限给角色
- `POST /api/permission/remove` - 从角色移除权限
- `GET /api/permission/list` - 获取所有权限

### 角色相关
- `GET /api/role/list` - 获取所有角色
- `POST /api/role/create` - 创建角色
- `PUT /api/role/update` - 更新角色
- `DELETE /api/role/delete/{id}` - 删除角色

## 使用指南

### 1. 启动系统
- 后端：运行 `ExamSystemApplication` 启动Spring Boot应用
- 前端：运行 `npm run dev` 启动开发服务器

### 2. 初始登录
- 系统会自动创建超级管理员账户
- 默认用户名：`admin`
- 密码将在控制台输出

### 3. 权限管理流程
1. 登录超级管理员账户
2. 进入"菜单管理"页面创建系统菜单
3. 进入"角色管理"页面创建角色
4. 为角色分配相应的菜单权限
5. 将角色分配给普通用户

## 安全特性

### 1. 权限验证流程
1. 请求到达控制器
2. AOP拦截器检查 `@PermissionRequired` 注解
3. 验证用户身份和权限
4. 允许或拒绝请求

### 2. 输入验证
- 所有输入参数都会进行验证
- 防止SQL注入和XSS攻击
- 参数长度和格式限制

### 3. 错误处理
- 统一的异常处理机制
- 详细的错误码和消息
- 保护敏感信息不泄露

## 扩展性

### 1. 新增权限类型
可通过修改Permission实体和枚举来增加新的权限类型

### 2. 自定义权限逻辑
可扩展PermissionService来实现特定业务的权限逻辑

### 3. 多租户支持
可通过扩展User和Role实体来支持多租户场景

## 总结

菜单权限管理系统提供了一套完整的权限解决方案，具有良好的安全性、可扩展性和易用性。系统采用现代化的技术栈，遵循最佳实践，可作为企业级应用的权限管理基础。