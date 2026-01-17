import { processApiResponse, getAuthHeaders } from './api'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export const userService = {
  /**
   * Get all users with pagination and filtering
   */
  async getUsers(params?: { page?: number; limit?: number; status?: string }): Promise<User[]> {
    let url = '/api/users/list'
    if (params) {
      const searchParams = new URLSearchParams()
      if (params.page) searchParams.append('page', params.page.toString())
      if (params.limit) searchParams.append('limit', params.limit.toString())
      if (params.status) searchParams.append('status', params.status)
      if (searchParams.toString()) url += '?' + searchParams.toString()
    }
    
    const response = await fetch(url, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<User[]>(response)
  },

  /**
   * Get a specific user by ID
   */
  async getUserById(id: string): Promise<User> {
    const response = await fetch(`/api/users/${id}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<User>(response)
  },

  /**
   * Create a new user
   */
  async createUser(userData: CreateUserRequest): Promise<User> {
    const response = await fetch('/api/users/create', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(userData)
    })
    return processApiResponse<User>(response)
  },

  /**
   * Update an existing user
   */
  async updateUser(id: string, userData: UpdateUserRequest): Promise<User> {
    const response = await fetch('/api/users/update', {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({...userData, id})
    })
    return processApiResponse<User>(response)
  },

  /**
   * Delete a user
   */
  async deleteUser(id: string): Promise<void> {
    const response = await fetch(`/api/users/delete/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    return processApiResponse<void>(response)
  },

  /**
   * Unlock a user account
   */
  async unlockUser(id: string): Promise<void> {
    const response = await fetch(`/api/users/unlock/${id}`, {
      method: 'POST',
      headers: getAuthHeaders()
    })
    return processApiResponse<void>(response)
  }
}

