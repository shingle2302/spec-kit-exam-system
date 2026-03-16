import { processApiResponse, getAuthHeaders } from './api'
import type { Role, CreateRoleRequest, UpdateRoleRequest } from '@/types'

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

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
  async list(params?: { page?: number; size?: number; filters?: Record<string, unknown> }): Promise<PageResponse<Role>> {
    const response = await fetch('/api/roles/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params?.page ?? 1,
        size: params?.size ?? 10,
        filters: params?.filters ?? {}
      })
    })
    const data = await processApiResponse<PageResponse<any>>(response)
    // Parse permissions for each role in data
    if (data && data.records) {
      data.records = data.records.map(parseRoleResponse)
    }
    return data as PageResponse<Role>
  },

  /**
   * Get a specific role by ID
   */
  async getById(id: string): Promise<Role> {
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
  async create(roleData: CreateRoleRequest): Promise<Role> {
    const response = await fetch('/api/roles', {
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
  async update(id: string, roleData: UpdateRoleRequest): Promise<Role> {
    const response = await fetch(`/api/roles/${id}`, {
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
  async remove(id: string): Promise<void> {
    const response = await fetch(`/api/roles/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    await processApiResponse<void>(response)
  },

  /**
   * Get a specific role by code
   */
  async getByCode(code: string): Promise<Role> {
    const response = await fetch(`/api/roles/code/${code}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    const data = await processApiResponse<Role>(response)
    return parseRoleResponse(data)
  },

  /**
   * Get role statistics
   */
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch('/api/roles/statistics', {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<Record<string, unknown>>(response)
  }
}