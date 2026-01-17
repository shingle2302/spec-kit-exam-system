# Data Model: Menu and Permission Management System

## Entity: Menu
- **id**: String (primary key)
- **name**: String (menu display name)
- **parentId**: String (parent menu id, null for root menus)
- **path**: String (route path for navigation)
- **component**: String (component name to render)
- **icon**: String (menu icon identifier)
- **orderNum**: Integer (display order)
- **status**: String (ACTIVE, INACTIVE)
- **permissions**: List<Permission> (associated permissions)
- **children**: List<Menu> (child menu items)
- **createTime**: LocalDateTime
- **updateTime**: LocalDateTime

## Entity: Permission
- **id**: String (primary key)
- **menuId**: String (associated menu id)
- **buttonName**: String (display name of button/function)
- **operationType**: String (QUERY, DELETE, MODIFY, UNLOCK, LOCK)
- **permissionCode**: String (unique permission identifier)
- **description**: String (permission description)
- **status**: String (ACTIVE, INACTIVE)
- **roles**: List<Role> (roles with this permission)
- **createTime**: LocalDateTime
- **updateTime**: LocalDateTime

## Entity: Role
- **id**: String (primary key)
- **roleName**: String (role display name)
- **roleCode**: String (unique role identifier)
- **description**: String (role description)
- **status**: String (ACTIVE, INACTIVE)
- **permissions**: List<Permission> (permissions assigned to role)
- **menus**: List<Menu> (menus accessible to role)
- **users**: List<User> (users assigned to this role)
- **createTime**: LocalDateTime
- **updateTime**: LocalDateTime

## Entity: User
- **id**: String (primary key)
- **username**: String (unique username)
- **password**: String (hashed password)
- **email**: String (user email)
- **realName**: String (user's real name)
- **status**: String (ACTIVE, INACTIVE, LOCKED)
- **roles**: List<Role> (user's assigned roles)
- **createTime**: LocalDateTime
- **updateTime**: LocalDateTime

## Entity: UserRole (Junction Table)
- **id**: String (primary key)
- **userId**: String (user id)
- **roleId**: String (role id)
- **assignedBy**: String (admin who assigned)
- **assignedTime**: LocalDateTime

## Entity: MenuRole (Junction Table)
- **id**: String (primary key)
- **menuId**: String (menu id)
- **roleId**: String (role id)
- **permissions**: List<String> (specific permission codes allowed)
- **assignedTime**: LocalDateTime

## Relationships
- **User** 1:M **UserRole** M:1 **Role** (many-to-many user-role relationship)
- **Role** M:M **Permission** (many-to-many role-permission relationship via permissions field)
- **Menu** 1:M **Permission** (one-to-many menu-permission relationship)
- **Role** M:M **Menu** (many-to-many role-menu relationship via MenuRole junction table)
- **Menu** 1:M **Menu** (self-referencing parent-child relationship)

## Validation Rules
- Menu name and path must be unique within parent context
- Permission codes must be unique across the system
- Role codes must be unique across the system
- Username must be unique across all users
- Menu hierarchy depth limited to 5 levels maximum
- All entities must have non-null createTime and updateTime fields

## State Transitions
- Menu/Permission/Role/User status: DRAFT → ACTIVE → INACTIVE
- User status can transition to/from LOCKED independently
- Status changes require audit trail logging