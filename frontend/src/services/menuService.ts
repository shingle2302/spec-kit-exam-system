import { processApiResponse, getAuthHeaders } from './api'
import type { Menu } from '@/types'

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const menuService = {
  /**
   * Get menu tree structure for a specific role
   */
  async getMenuTree(roleId: string | null = null): Promise<Menu[]> {
    let url = `/api/menus/tree`
    if (roleId) {
      url += `?roleId=${encodeURIComponent(roleId)}`
    }
    const response = await fetch(url, {
      method: 'GET',
      headers: getAuthHeaders()
    })

    return processApiResponse<Menu[]>(response)
  },

  /**
   * Get all menus
   */
  async list(params?: { page?: number; size?: number; filters?: Record<string, unknown> }): Promise<PageResponse<Menu>> {
    const response = await fetch('/api/menus/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params?.page ?? 1,
        size: params?.size ?? 10,
        filters: params?.filters ?? {}
      })
    })

    return processApiResponse<PageResponse<Menu>>(response)
  },

  /**
   * Get a menu by ID
   */
  async getById(menuId: string): Promise<Menu> {
    const response = await fetch(`/api/menus/${encodeURIComponent(menuId)}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })

    return processApiResponse<Menu>(response)
  },

  /**
   * Create a new menu
   */
  async create(menu: Partial<Menu>): Promise<Menu> {
    const response = await fetch(`/api/menus`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(menu)
    })

    return processApiResponse<Menu>(response)
  },

  /**
   * Update an existing menu
   */
  async update(menuId: string, menu: Partial<Menu>): Promise<Menu> {
    const response = await fetch(`/api/menus/${encodeURIComponent(menuId)}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({...menu, id: menuId})
    })

    return processApiResponse<Menu>(response)
  },

  /**
   * Delete a menu by ID
   */
  async remove(menuId: string): Promise<void> {
    const response = await fetch(`/api/menus/${encodeURIComponent(menuId)}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })

    return processApiResponse<void>(response)
  },

  /**
   * Get menu statistics
   */
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch('/api/menus/statistics', {
      method: 'GET',
      headers: getAuthHeaders()
    })

    return processApiResponse<Record<string, unknown>>(response)
  }
}