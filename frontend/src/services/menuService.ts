import { processApiResponse, getAuthHeaders } from './api'
import type { Menu } from '@/types'

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
   * Create a new menu
   */
  async createMenu(menu: Partial<Menu>): Promise<Menu> {
    const response = await fetch(`/api/menus/create`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(menu)
    })
    
    return processApiResponse<Menu>(response)
  },

  /**
   * Update an existing menu
   */
  async updateMenu(menu: Partial<Menu>): Promise<Menu> {
    const response = await fetch(`/api/menus/update`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(menu)
    })
    
    return processApiResponse<Menu>(response)
  },

  /**
   * Delete a menu by ID
   */
  async deleteMenu(menuId: string): Promise<void> {
    const response = await fetch(`/api/menus/delete/${encodeURIComponent(menuId)}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<void>(response)
  },

  /**
   * Get all menus
   */
  async getAllMenus(params?: { page?: number; limit?: number }): Promise<import('@/types').PageResponse<Menu>> {
    let url = '/api/menus/list'
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
    
    return processApiResponse<import('@/types').PageResponse<Menu>>(response)
  },

  /**
   * Get a menu by ID
   */
  async getMenuById(menuId: string): Promise<Menu> {
    const response = await fetch(`/api/menus/${encodeURIComponent(menuId)}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    
    return processApiResponse<Menu>(response)
  }
}