import { processApiResponse, getAuthHeaders } from './api'
import type { Permission } from '@/types'

export const permissionService = {
  /**
   * Get permissions for a specific role
   */
  async getPermissionsByRole(roleId: string): Promise<Permission[]> {
    const response = await fetch(`/api/permissions/role/${encodeURIComponent(roleId)}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<Permission[]>(response)
  },

  /**
   * Assign permissions to a role
   */
  async assignPermissionsToRole(roleId: string, permissionIds: string[]): Promise<void> {
    const response = await fetch(`/api/permissions/assign`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        roleId: roleId,
        permissionIds: permissionIds
      })
    })
    
    return processApiResponse<void>(response)
  },

  /**
   * Remove permissions from a role
   */
  async removePermissionsFromRole(roleId: string, permissionIds: string[]): Promise<void> {
    const response = await fetch(`/api/permissions/remove`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        roleId: roleId,
        permissionIds: permissionIds
      })
    })
    
    return processApiResponse<void>(response)
  },

  /**
   * Get all permissions
   */
  async getAllPermissions(params?: { page?: number; limit?: number }): Promise<import('@/types').PageResponse<Permission>> {
    let url = '/api/permissions/list'
    if (params) {
      const searchParams = new URLSearchParams()
      if (params.page) searchParams.append('page', params.page.toString())
      if (params.limit) searchParams.append('limit', params.limit.toString())
      if (searchParams.toString()) url += '?' + searchParams.toString()
    }
    
    const response = await fetch(url, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<import('@/types').PageResponse<Permission>>(response)
  },

  /**
   * Create a new permission
   */
  async createPermission(permission: Partial<Permission>): Promise<Permission> {
    const response = await fetch(`/api/permissions/create`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(permission)
    })
    
    return processApiResponse<Permission>(response)
  },

  /**
   * Update an existing permission
   */
  async updatePermission(permission: Partial<Permission>): Promise<Permission> {
    const response = await fetch(`/api/permissions/update`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(permission)
    })
    
    return processApiResponse<Permission>(response)
  },

  /**
   * Get a permission by ID
   */
  async getPermissionById(permissionId: string): Promise<Permission> {
    const response = await fetch(`/api/permissions/${encodeURIComponent(permissionId)}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<Permission>(response)
  },

  /**
   * Delete a permission by ID
   */
  async deletePermission(permissionId: string): Promise<void> {
    const response = await fetch(`/api/permissions/delete/${encodeURIComponent(permissionId)}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<void>(response)
  }
}