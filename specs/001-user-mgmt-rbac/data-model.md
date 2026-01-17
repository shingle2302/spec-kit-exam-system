# Data Model: User Management Module with RBAC

## User Entity
**Description**: Represents a system user with personal information and access controls

**Fields**:
- `id`: UUID (Primary Key, auto-generated)
- `username`: String (Unique, required, 3-50 chars, alphanumeric + underscore/hyphen)
- `passwordHash`: String (Required, BCrypt hashed, min 60 chars)
- `phone`: String (Optional, validated format, unique if provided)
- `email`: String (Required, unique, valid email format)
- `status`: Enum (ACTIVE, INACTIVE, SUSPENDED, LOCKED) - default: ACTIVE
- `roleId`: UUID (Foreign Key to Role, nullable)
- `createdAt`: DateTime (Auto-generated)
- `updatedAt`: DateTime (Auto-generated)
- `lastLoginAt`: DateTime (Nullable)
- `passwordChangedAt`: DateTime (Auto-generated, updates with password change)
- `failedLoginAttempts`: Integer (Default: 0, tracks consecutive failed login attempts)
- `lockedUntil`: DateTime (Nullable, timestamp until which account is locked)
- `isSuperAdmin`: Boolean (Default: false, indicates if user has super admin privileges)

**Validation Rules**:
- Username: Alphanumeric characters, underscore, hyphen only; 3-50 characters
- Password: Minimum 8 characters with uppercase, lowercase, number, special character
- Email: Valid email format using standard regex, must be unique
- Phone: Valid phone format (international format supported), if provided must be unique
- Status: Must be one of the defined enum values
- FailedLoginAttempts: Must be >= 0 and <= 3 (triggers lockout when reaching 3)

## Role Entity
**Description**: Defines permissions that determine what actions a user can perform in the system, with no predefined tier structure

**Fields**:
- `id`: UUID (Primary Key, auto-generated)
- `name`: String (Required, unique, 3-100 chars)
- `description`: Text (Optional, up to 500 chars)
- `permissions`: JSON (Required, contains permission matrix)
- `isSuperAdminRole`: Boolean (Default: false, indicates if this is the super admin role)
- `isActive`: Boolean (Default: true)
- `createdAt`: DateTime (Auto-generated)
- `updatedAt`: DateTime (Auto-generated)

**Validation Rules**:
- Name: Unique across all roles
- Permissions: Valid JSON structure with defined permission schema
- Permissions must follow the format: `{ resource: { action: boolean } }`
- Only one role can have isSuperAdminRole set to true

## Session Entity
**Description**: Represents an authenticated user's active connection to the system with 30-minute idle timeout mechanism

**Fields**:
- `id`: UUID (Primary Key, auto-generated)
- `userId`: UUID (Foreign Key to User, required)
- `sessionId`: String (Unique, randomly generated)
- `jwtToken`: String (Encrypted JWT token)
- `refreshToken`: String (Encrypted refresh token)
- `expiresAt`: DateTime (Calculated: 30 minutes from last activity)
- `lastActivityAt`: DateTime (Updated on each activity)
- `ipAddress`: String (Recorded on creation)
- `userAgent`: String (Browser/device info)
- `createdAt`: DateTime (Auto-generated)

**Validation Rules**:
- Session must be linked to an active user
- Expiration time must be within 30 minutes of last activity
- Refresh tokens must be rotated appropriately

## Permission Schema
**Description**: Defines the structure for storing permissions in the Role entity

**Structure**:
```json
{
  "users": {
    "read": true/false,
    "create": true/false,
    "update": true/false,
    "delete": true/false,
    "unlock": true/false
  },
  "roles": {
    "read": true/false,
    "create": true/false,
    "update": true/false,
    "delete": true/false
  },
  "system": {
    "admin": true/false,
    "super_admin": true/false,
    "audit": true/false
  }
}
```

## Relationships
- User `belongsTo` Role (optional relationship)
- Role `hasMany` Users (one-to-many)
- User `hasMany` Sessions (one-to-many)

## Indexes
- User.username (unique)
- User.email (unique)
- User.phone (unique, if not null)
- Session.sessionId (unique)
- Session.expiresAt (for cleanup queries)
- Role.name (unique)

## State Transitions
**User Status**:
- ACTIVE → INACTIVE (admin action or self-deactivation)
- INACTIVE → ACTIVE (admin action)
- ACTIVE → SUSPENDED (admin action for violations)
- SUSPENDED → ACTIVE (admin action)
- ACTIVE → LOCKED (after 3 failed login attempts)
- LOCKED → ACTIVE (admin unlock action)
- Any → DELETED (permanent deletion by admin)

**Account Locking**:
- Normal → LOCKED (after 3 failed login attempts)
- LOCKED → ACTIVE (after admin unlock action)