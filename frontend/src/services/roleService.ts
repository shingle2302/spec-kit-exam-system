import { processApiResponse, getAuthHeaders } from './api'
import type { Role, CreateRoleRequest, UpdateRoleRequest } from '@/types'

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
    const response = await fetch('/api/roles/list', {
      method: 'GET',
      headers: getAuthHeaders()
    })
    const data = await processApiResponse<Role[]>(response)
    // Parse permissions for each role
    return data.map(parseRoleResponse)
  },

  /**
   * Get a specific role by ID
   */
  async getRoleById(id: string): Promise<Role> {
    const response = await fetch(`/api/roles/${id}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    const data = await processApiResponse<Role>(response)
    return parseRoleResponse(data)
  },

  /**
   * Create a new role
   */
  async createRole(roleData: CreateRoleRequest): Promise<Role> {
    const response = await fetch('/api/roles/create', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(prepareRoleData(roleData))
    })
    const data = await processApiResponse<Role>(response)
    return parseRoleResponse(data)
  },

  /**
   * Update an existing role
   */
  async updateRole(id: string, roleData: UpdateRoleRequest): Promise<Role> {
    const response = await fetch('/api/roles/update', {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({...prepareRoleData(roleData), id})
    })
    const data = await processApiResponse<Role>(response)
    return parseRoleResponse(data)
  },

  /**
   * Delete a role
   */
  async deleteRole(id: string): Promise<void> {
    const response = await fetch(`/api/roles/delete/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    await processApiResponse<void>(response)
  },

  /**
   * Get a specific role by code
   */
  async getRoleByCode(code: string): Promise<Role> {
    const response = await fetch(`/api/roles/code/${code}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    const data = await processApiResponse<Role>(response)
    return parseRoleResponse(data)
  }
}

