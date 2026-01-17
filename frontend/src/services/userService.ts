import api from './api'
import type { User, CreateUserRequest, UpdateUserRequest, ApiResponse } from '@/types'

export const userService = {
  /**
   * Get all users with pagination and filtering
   */
  async getUsers(params?: { page?: number; limit?: number; status?: string }): Promise<User[]> {
    const response = await api.get<User[]>('/users', { params })
    return response.data
  },

  /**
   * Get a specific user by ID
   */
  async getUserById(id: string): Promise<User> {
    const response = await api.get<User>(`/users/${id}`)
    return response.data
  },

  /**
   * Create a new user
   */
  async createUser(userData: CreateUserRequest): Promise<ApiResponse<User>> {
    const response = await api.post<ApiResponse<User>>('/users', userData)
    return response.data
  },

  /**
   * Update an existing user
   */
  async updateUser(id: string, userData: UpdateUserRequest): Promise<ApiResponse<User>> {
    const response = await api.put<ApiResponse<User>>(`/users/${id}`, userData)
    return response.data
  },

  /**
   * Delete a user
   */
  async deleteUser(id: string): Promise<void> {
    await api.delete(`/users/${id}`)
  },

  /**
   * Unlock a user account
   */
  async unlockUser(id: string): Promise<ApiResponse> {
    const response = await api.post<ApiResponse>(`/users/${id}/unlock`)
    return response.data
  }
}

export default userService
