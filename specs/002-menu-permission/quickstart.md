# Quickstart Guide: Menu and Permission Management System

## Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.8+
- PostgreSQL (for production) or H2 (for development)
- Redis server
- Elasticsearch

## Setup Instructions

### 1. Clone and Navigate to Project
```bash
git clone <repository-url>
cd <project-root>
```

### 2. Backend Setup
```bash
# Navigate to backend directory
cd backend

# Install dependencies
mvn clean install

# Configure database (application.yml)
# For development: H2 in-memory database
# For production: PostgreSQL connection

# Run the application
mvn spring-boot:run
```

### 3. Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start the development server
npm run dev
```

## Key Components

### 1. Permission Annotation
Use `@PermissionRequired` annotation on controller methods:
```java
@PermissionRequired(menu = "user-management", operation = "CREATE")
@PostMapping("/users")
public Result createUser(@RequestBody User user) {
    // Implementation
}
```

### 2. API Response Format
All APIs return standardized response:
```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

### 3. Error Codes
- 0: Success
- 1001: Validation Error
- 2001: Authorization Error
- 3001: System Error

## Initial Configuration

### 1. Super Admin Setup
The system automatically creates a super admin account on first startup:
- Username: `superadmin`
- Password: Set during initial configuration

### 2. Menu Registration
New features with `@PermissionRequired` annotations are automatically registered during startup.

### 3. Access Control
- Super admins bypass all permission checks
- Regular users require appropriate role assignments
- Permissions are checked at method level via AOP

## Development Workflow

### Adding New Menu Item
1. Create controller method with `@PermissionRequired` annotation
2. System automatically registers permission during startup
3. Admin assigns permissions to roles via UI

### Testing Permissions
```bash
# Test API with proper authentication
curl -X GET http://localhost:8080/api/menu/list \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json"
```

## API Endpoints

### Menu Management
- `GET /api/menu/tree` - Get menu hierarchy
- `POST /api/menu/create` - Create new menu
- `PUT /api/menu/update` - Update menu
- `DELETE /api/menu/delete/{id}` - Delete menu

### Permission Management
- `GET /api/permission/list` - Get permissions for role
- `POST /api/permission/assign` - Assign permissions to role
- `POST /api/permission/check` - Check user permissions

### Role Management
- `GET /api/role/list` - Get all roles
- `POST /api/role/create` - Create new role
- `PUT /api/role/update` - Update role
- `POST /api/role/assign` - Assign role to user