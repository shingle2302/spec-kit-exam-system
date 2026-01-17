import api from './api'
import type { Role, CreateRoleRequest, UpdateRoleRequest, ApiResponse } from '@/types'

// Convert permissions object to JSON string for backend
function prepareRoleData(roleData: CreateRoleRequest | UpdateRoleRequest) {
  return {
    ...roleData,
    permissions: roleData.permissions ? JSON.stringify(roleData.permissions) : undefined
  }
}

// Parse permissions JSON string from backend response
function parseRoleResponse(role: any): Role {
  return {
    ...role,
    permissions: typeof role.permissions === 'string' ? JSON.parse(role.permissions) : role.permissions
  }
}

export const roleService = {
  /**
   * Get all roles
   */
  async getRoles(): Promise<Role[]> {
    const response = await api.get<Role[]>('/roles')
    // Parse permissions for each role
    return response.data.map(parseRoleResponse)
  },

  /**
   * Get a specific role by ID
   */
  async getRoleById(id: string): Promise<Role> {
    const response = await api.get<Role>(`/roles/${id}`)
    return parseRoleResponse(response.data)
  },

  /**
   * Create a new role
   */
  async createRole(roleData: CreateRoleRequest): Promise<Role> {
    const response = await api.post<Role>('/roles', prepareRoleData(roleData))
    return parseRoleResponse(response.data)
  },

  /**
   * Update an existing role
   */
  async updateRole(id: string, roleData: UpdateRoleRequest): Promise<Role> {
    const response = await api.put<Role>(`/roles/${id}`, prepareRoleData(roleData))
    return parseRoleResponse(response.data)
  },

  /**
   * Delete a role
   */
  async deleteRole(id: string): Promise<void> {
    await api.delete(`/roles/${id}`)
  }
}

export default roleService
