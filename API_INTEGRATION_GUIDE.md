# 前后端API集成指南

## 概述

本文档详细介绍了前端与后端的API集成方案，包括统一的响应格式、错误处理机制和安全策略。

## API设计原则

### 1. RESTful风格
- 使用标准HTTP方法（GET、POST、PUT、DELETE）
- 资源路径清晰明确
- 统一的响应格式

### 2. 统一响应格式
后端统一返回Result格式：
```json
{
  "data": {},
  "code": 0000,
  "msg": "success"
}
```

前端统一处理这种格式，提供`processApiResponse`工具函数。

## API端点

### 用户管理
- `GET /api/users/list` - 获取用户列表
- `POST /api/users/create` - 创建用户
- `PUT /api/users/update` - 更新用户
- `DELETE /api/users/delete/{id}` - 删除用户
- `POST /api/users/unlock/{id}` - 解锁用户

### 角色管理
- `GET /api/role/list` - 获取角色列表
- `POST /api/role/create` - 创建角色
- `PUT /api/role/update` - 更新角色
- `DELETE /api/role/delete/{id}` - 删除角色
- `GET /api/role/{id}` - 获取角色详情
- `GET /api/role/code/{code}` - 根据代码获取角色

### 菜单管理
- `GET /api/menu/tree` - 获取菜单树
- `POST /api/menu/create` - 创建菜单
- `PUT /api/menu/update` - 更新菜单
- `DELETE /api/menu/delete/{id}` - 删除菜单
- `GET /api/menu/list` - 获取所有菜单
- `GET /api/menu/{id}` - 获取菜单详情

### 权限管理
- `GET /api/permission/role/{roleId}` - 获取角色权限
- `POST /api/permission/assign` - 分配权限给角色
- `POST /api/permission/remove` - 移除角色权限
- `GET /api/permission/list` - 获取所有权限
- `POST /api/permission/create` - 创建权限
- `PUT /api/permission/update` - 更新权限
- `DELETE /api/permission/delete/{id}` - 删除权限

### 认证
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出

## 前端API服务

### 统一工具函数
```typescript
// api.ts
interface BackendResult<T> {
  data: T;
  code: number;
  msg: string;
}

export async function processApiResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  const result: BackendResult<T> = await response.json();
  
  if (result.code !== 0) {
    throw new Error(result.msg || `Backend error with code: ${result.code}`);
  }
  
  return result.data;
}

export function getAuthHeaders(): Record<string, string> {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}` || ''
  };
}
```

### 服务层实现
所有服务层都使用fetch API和统一的错误处理：

```javascript
// 示例：menuService.js
export const getMenuTree = async (roleId = null) => {
  let url = `${BASE_URL}/menu/tree`;
  if (roleId) {
    url += `?roleId=${encodeURIComponent(roleId)}`;
  }
  const response = await fetch(url, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};
```

## 权限控制

### 后端权限检查
- 使用`@PermissionRequired`注解标记需要权限的方法
- AOP切面自动检查用户权限
- 超级管理员可绕过权限检查

### 前端权限显示
- 根据用户权限动态显示菜单项
- 对敏感操作进行二次权限验证

## 错误处理

### 后端错误码
- `0` - 成功
- `1001` - 验证错误
- `2001` - 授权错误
- `423` - 账户锁定
- `500` - 服务器内部错误

### 前端错误处理
- 统一捕获和显示错误信息
- 对不同错误类型提供不同处理策略
- 用户友好的错误提示

## 安全措施

### 认证
- JWT Token认证
- 自动刷新Token机制
- 安全的Token存储

### 授权
- 基于角色的访问控制（RBAC）
- 细粒度权限控制
- 操作审计日志

## 开发建议

### 添加新API
1. 在后端创建Controller方法，返回Result格式
2. 在前端创建对应的服务函数
3. 更新TypeScript类型定义
4. 测试API连接和错误处理

### 调试技巧
- 使用浏览器开发者工具查看网络请求
- 检查API响应格式是否符合Result规范
- 验证权限控制是否正常工作

## 性能优化

- 合理使用缓存减少API调用
- 分页加载大数据集
- 优化数据库查询性能

## 扩展性考虑

- API版本管理策略
- 向后兼容性保证
- 模块化设计便于扩展