import { processApiResponse, getAuthHeaders } from './api'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export const userService = {
  /**
   * Get all users with pagination and filtering
   */
  async getUsers(params?: { page?: number; size?: number; filters?: { status?: string } }): Promise<import('@/types').PageResponse<User>> {
    const response = await fetch('/api/users/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params?.page ?? 1,
        size: params?.size ?? 10,
        filters: params?.filters ?? {}
      })
    })
    return processApiResponse<import('@/types').PageResponse<User>>(response)
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
