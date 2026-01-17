import api from './api'
import type { Role, CreateRoleRequest, UpdateRoleRequest, ApiResponse } from '@/types'

export const roleService = {
  /**
   * Get all roles
   */
  async getRoles(): Promise<ApiResponse<Role[]>> {
    const response = await api.get<ApiResponse<Role[]>>('/roles')
    return response.data
  },

  /**
   * Get a specific role by ID
   */
  async getRoleById(id: string): Promise<ApiResponse<Role>> {
    const response = await api.get<ApiResponse<Role>>(`/roles/${id}`)
    return response.data
  },

  /**
   * Create a new role
   */
  async createRole(roleData: CreateRoleRequest): Promise<ApiResponse<Role>> {
    const response = await api.post<ApiResponse<Role>>('/roles', roleData)
    return response.data
  },

  /**
   * Update an existing role
   */
  async updateRole(id: string, roleData: UpdateRoleRequest): Promise<ApiResponse<Role>> {
    const response = await api.put<ApiResponse<Role>>(`/roles/${id}`, roleData)
    return response.data
  },

  /**
   * Delete a role
   */
  async deleteRole(id: string): Promise<ApiResponse> {
    const response = await api.delete<ApiResponse>(`/roles/${id}`)
    return response.data
  }
}

export default roleService
