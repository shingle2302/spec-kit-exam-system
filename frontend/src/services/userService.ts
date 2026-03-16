import { processApiResponse, getAuthHeaders } from './api'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const userService = {
  /**
   * Get all users with pagination and filtering
   */
  async list(params?: { page?: number; size?: number; filters?: { status?: string } }): Promise<PageResponse<User>> {
    const response = await fetch('/api/users/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params?.page ?? 1,
        size: params?.size ?? 10,
        filters: params?.filters ?? {}
      })
    })
    return processApiResponse<PageResponse<User>>(response)
  },

  /**
   * Get a specific user by ID
   */
  async getById(id: string): Promise<User> {
    const response = await fetch(`/api/users/${id}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<User>(response)
  },

  /**
   * Create a new user
   */
  async create(userData: CreateUserRequest): Promise<User> {
    const response = await fetch('/api/users', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(userData)
    })
    return processApiResponse<User>(response)
  },

  /**
   * Update an existing user
   */
  async update(id: string, userData: UpdateUserRequest): Promise<User> {
    const response = await fetch(`/api/users/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({...userData, id})
    })
    return processApiResponse<User>(response)
  },

  /**
   * Delete a user
   */
  async remove(id: string): Promise<void> {
    const response = await fetch(`/api/users/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })
    return processApiResponse<void>(response)
  },

  /**
   * Unlock a user account
   */
  async unlock(id: string): Promise<void> {
    const response = await fetch(`/api/users/unlock/${id}`, {
      method: 'POST',
      headers: getAuthHeaders()
    })
    return processApiResponse<void>(response)
  },

  /**
   * Get user statistics
   */
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch('/api/users/statistics', {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<Record<string, unknown>>(response)
  },

  /**
   * Search users by keyword
   */
  async search(keyword: string): Promise<User[]> {
    const response = await fetch(`/api/users/search?keyword=${encodeURIComponent(keyword)}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })
    return processApiResponse<User[]>(response)
  }
}