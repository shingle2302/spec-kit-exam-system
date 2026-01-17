# API Contract: Menu and Permission Management System

## Base Response Format
All API endpoints return responses in the following format:

```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

Where:
- `data`: Contains the response payload (varies by endpoint)
- `code`: Numeric status code (0 for success, specific error codes for failures)
- `msg`: Human-readable message describing the result

## Error Codes
- `0`: Success
- `1001`: Validation Error
- `1002`: Invalid Request Parameter
- `2001`: Unauthorized Access
- `2002`: Insufficient Permissions
- `2003`: Authentication Required
- `3001`: Internal Server Error
- `3002`: Service Unavailable
- `4001`: Resource Not Found
- `4002`: Resource Already Exists

## Menu Management APIs

### Get Menu Tree
- **Endpoint**: `GET /api/menu/tree`
- **Description**: Retrieve hierarchical menu structure
- **Authentication**: Required
- **Permissions**: `menu:read`
- **Request Parameters**:
  - `roleId` (optional): Role ID to filter menus by permissions
- **Response**:
```json
{
  "data": [
    {
      "id": "menu_001",
      "name": "User Management",
      "path": "/user",
      "component": "UserPage",
      "icon": "user-icon",
      "orderNum": 1,
      "status": "ACTIVE",
      "children": [
        {
          "id": "menu_002",
          "name": "User List",
          "path": "/user/list",
          "component": "UserList",
          "icon": "list-icon",
          "orderNum": 1,
          "status": "ACTIVE",
          "children": []
        }
      ]
    }
  ],
  "code": 0,
  "msg": "success"
}
```

### Create Menu
- **Endpoint**: `POST /api/menu/create`
- **Description**: Create a new menu item
- **Authentication**: Required
- **Permissions**: `menu:create`
- **Request Body**:
```json
{
  "name": "New Menu",
  "parentId": "parent_menu_id",
  "path": "/new-menu",
  "component": "NewMenuComponent",
  "icon": "menu-icon",
  "orderNum": 1,
  "status": "ACTIVE"
}
```
- **Response**:
```json
{
  "data": {
    "id": "new_menu_id",
    "createTime": "2026-01-17T10:00:00"
  },
  "code": 0,
  "msg": "Menu created successfully"
}
```

### Update Menu
- **Endpoint**: `PUT /api/menu/update`
- **Description**: Update an existing menu item
- **Authentication**: Required
- **Permissions**: `menu:update`
- **Request Body**:
```json
{
  "id": "menu_id",
  "name": "Updated Menu Name",
  "path": "/updated-path",
  "status": "INACTIVE"
}
```
- **Response**:
```json
{
  "data": {
    "id": "menu_id",
    "updateTime": "2026-01-17T10:00:00"
  },
  "code": 0,
  "msg": "Menu updated successfully"
}
```

### Delete Menu
- **Endpoint**: `DELETE /api/menu/delete/{id}`
- **Description**: Delete a menu item
- **Authentication**: Required
- **Permissions**: `menu:delete`
- **Path Parameters**:
  - `id`: Menu ID to delete
- **Response**:
```json
{
  "data": null,
  "code": 0,
  "msg": "Menu deleted successfully"
}
```

## Permission Management APIs

### Get Permissions for Role
- **Endpoint**: `GET /api/permission/role/{roleId}`
- **Description**: Get all permissions assigned to a role
- **Authentication**: Required
- **Permissions**: `permission:read`
- **Path Parameters**:
  - `roleId`: Role ID
- **Response**:
```json
{
  "data": [
    {
      "id": "perm_001",
      "menuId": "menu_001",
      "buttonName": "Query Button",
      "operationType": "QUERY",
      "permissionCode": "menu_001:query",
      "description": "Allow query operations on menu"
    }
  ],
  "code": 0,
  "msg": "Permissions retrieved successfully"
}
```

### Assign Permissions to Role
- **Endpoint**: `POST /api/permission/assign`
- **Description**: Assign permissions to a role
- **Authentication**: Required
- **Permissions**: `permission:assign`
- **Request Body**:
```json
{
  "roleId": "role_001",
  "permissionIds": ["perm_001", "perm_002", "perm_003"],
  "assignedBy": "admin_user_id"
}
```
- **Response**:
```json
{
  "data": {
    "assignedCount": 3,
    "timestamp": "2026-01-17T10:00:00"
  },
  "code": 0,
  "msg": "Permissions assigned successfully"
}
```

## Role Management APIs

### List Roles
- **Endpoint**: `GET /api/role/list`
- **Description**: Get all available roles
- **Authentication**: Required
- **Permissions**: `role:read`
- **Response**:
```json
{
  "data": [
    {
      "id": "role_001",
      "roleName": "Administrator",
      "roleCode": "ADMIN",
      "description": "Full system access",
      "status": "ACTIVE",
      "createTime": "2026-01-17T10:00:00"
    }
  ],
  "code": 0,
  "msg": "Roles retrieved successfully"
}
```

### Create Role
- **Endpoint**: `POST /api/role/create`
- **Description**: Create a new role
- **Authentication**: Required
- **Permissions**: `role:create`
- **Request Body**:
```json
{
  "roleName": "New Role",
  "roleCode": "NEW_ROLE",
  "description": "Description of new role",
  "status": "ACTIVE"
}
```
- **Response**:
```json
{
  "data": {
    "id": "new_role_id",
    "createTime": "2026-01-17T10:00:00"
  },
  "code": 0,
  "msg": "Role created successfully"
}
```

## Pagination APIs

### List with Pagination
- **Endpoint**: `POST /api/user/list`
- **Description**: Get paginated list of users
- **Authentication**: Required
- **Permissions**: `user:read`
- **Request Body**:
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
- **Response**:
```json
{
  "data": {
    "records": [
      {
        "id": "user_001",
        "username": "john_doe",
        "realName": "John Doe",
        "email": "john@example.com",
        "status": "ACTIVE"
      }
    ],
    "total": 150,
    "current": 1,
    "size": 10,
    "pages": 15
  },
  "code": 0,
  "msg": "Users retrieved successfully"
}
```

## Annotation-Based Permissions

### Controller Method Annotation
```java
@PermissionRequired(
    menu = "user-management", 
    button = "create-user", 
    operation = "CREATE"
)
@PostMapping("/users")
public Result createUser(@RequestBody UserDTO userDTO) {
    // Implementation
}
```

This will automatically register the permission `user-management:create-user:CREATE` in the system during startup.