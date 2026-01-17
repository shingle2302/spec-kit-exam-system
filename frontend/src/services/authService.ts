import api from './api'
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types'

export const authService = {
  /**
   * Login with identifier (username/email/phone) and password
   */
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', credentials)
    return response.data
  },

  /**
   * Register a new user account
   */
  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/register', userData)
    return response.data
  },

  /**
   * Logout the current user
   */
  async logout(): Promise<void> {
    await api.post('/auth/logout')
    localStorage.removeItem('accessToken')
    localStorage.removeItem('user')
  },

  /**
   * Get the current access token
   */
  getToken(): string | null {
    return localStorage.getItem('accessToken')
  },

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    return !!this.getToken()
  },

  /**
   * Get current user from localStorage
   */
  getCurrentUser() {
    const userStr = localStorage.getItem('user')
    if (userStr) {
      try {
        return JSON.parse(userStr)
      } catch {
        return null
      }
    }
    return null
  }
}

export default authService
