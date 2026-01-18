# 错误码规范

## 概述
本项目采用统一的错误码规范，按照模块进行分类，便于维护和定位问题。每个模块都有独立的枚举类管理其错误码。

## 错误码分类

### 通用错误码 (500X) - CommonErrorCodeEnum
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 5000 | SYSTEM_ERROR | 系统内部错误 |
| 5001 | VALIDATION_ERROR | 参数验证错误 |
| 5002 | AUTHORIZATION_ERROR | 授权错误 |
| 5003 | INVALID_PERMISSION_CONFIG | 无效的权限配置 |
| 5004 | INVALID_PERMISSION_CONFIG2 | 无效的权限配置 |
| 5005 | GENERAL_SERVER_ERROR | 一般服务器错误 |
| 5006 | RUNTIME_EXCEPTION | 运行时异常 |

### 用户模块错误码 (100X) - UserErrorCodeEnum
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 1001 | USER_NOT_FOUND | 用户未找到 |
| 1002 | FAILED_TO_UNLOCK_USER | 解锁用户失败 |
| 1003 | REGISTRATION_ERROR | 注册错误 |
| 1004 | AUTHENTICATION_FAILED | 认证失败 |
| 1005 | LOGOUT_FAILED | 登出失败 |
| 1006 | ACCOUNT_LOCKED | 账户已被锁定 |

### 角色模块错误码 (200X) - RoleErrorCodeEnum
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 2001 | ROLE_NOT_FOUND | 角色未找到 |

### 菜单模块错误码 (300X) - MenuErrorCodeEnum
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 3001 | FAILED_TO_UPDATE_MENU | 更新菜单失败 |
| 3002 | FAILED_TO_DELETE_MENU | 删除菜单失败 |

### 权限模块错误码 (400X) - PermissionErrorCodeEnum
| 错误码 | 描述 | 说明 |
|--------|------|------|
| 4001 | FAILED_TO_ASSIGN_PERMISSIONS | 分配权限失败 |
| 4002 | FAILED_TO_REMOVE_PERMISSIONS | 移除权限失败 |
| 4003 | FAILED_TO_UPDATE_PERMISSION | 更新权限失败 |
| 4004 | FAILED_TO_DELETE_PERMISSION | 删除权限失败 |
| 4005 | PERMISSION_NOT_FOUND | 权限未找到 |

## 使用规范

1. 每个模块有独立的错误码枚举类，包含code和message
2. 使用枚举类替代硬编码字符串
3. 错误码统一为字符串类型，如 "1001"
4. 前端根据错误码进行相应的处理和提示

## 扩展原则

1. 添加新错误码时，遵循模块分类原则
2. 用户模块：UserErrorCodeEnum
3. 角色模块：RoleErrorCodeEnum
4. 菜单模块：MenuErrorCodeEnum
5. 权限模块：PermissionErrorCodeEnum
6. 通用错误：CommonErrorCodeEnum