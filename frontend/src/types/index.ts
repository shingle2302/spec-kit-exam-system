// User Types
export interface User {
  id: string
  username: string
  email: string
  phone: string
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED' | 'LOCKED'
  roleId: string | null
  isSuperAdmin: boolean
  failedLoginAttempts: number
  lockedUntil: string | null
  createdAt: string
  updatedAt: string
  lastLoginAt: string | null
}

export interface CreateUserRequest {
  username: string
  password: string
  email: string
  phone: string
  status?: string
  roleId?: string
}

export interface UpdateUserRequest {
  username?: string
  email?: string
  phone?: string
  status?: string
  roleId?: string
}

// Role Types
export interface Role {
  id: string
  name: string
  description: string
  permissions: Permissions
  isSuperAdminRole: boolean
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface Permissions {
  users: {
    read: boolean
    create: boolean
    update: boolean
    delete: boolean
    unlock: boolean
  }
  roles: {
    read: boolean
    create: boolean
    update: boolean
    delete: boolean
  }
  system: {
    admin: boolean
    super_admin: boolean
    audit: boolean
  }
}

export interface CreateRoleRequest {
  name: string
  description?: string
  permissions: Permissions
}

export interface UpdateRoleRequest {
  name?: string
  description?: string
  permissions?: Permissions
}

// Auth Types
export interface LoginRequest {
  identifier: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  phone: string
}

export interface AuthResponse {
  success: boolean
  data?: {
    accessToken: string
    refreshToken?: string
    user: User
    expiresIn?: number
  }
  message?: string
}

// Menu Types
export interface Menu {
  id: string
  name: string
  path: string
  component: string
  icon: string
  parentId: string | null
  orderNum: number
  status: 'ACTIVE' | 'INACTIVE'
  createdAt: string
  updatedAt: string
}

// Permission Types
export interface Permission {
  id: string
  menuId: string
  menuName: string
  buttonName: string
  operationType: 'QUERY' | 'CREATE' | 'UPDATE' | 'DELETE' | 'UNLOCK' | 'LOCK'
  description: string
  status: 'ACTIVE' | 'INACTIVE'
  createdAt: string
  updatedAt: string
}

// API Response Types
export interface ApiResponse<T = any> {
  success: boolean
  data?: T
  message?: string
  errors?: Array<{
    field: string
    message: string
  }>
}

export interface PaginatedResponse<T> {
  success: boolean
  data: {
    users?: T[]
    roles?: T[]
    pagination: {
      currentPage: number
      totalPages: number
      totalItems: number
      itemsPerPage: number
    }
  }
}
