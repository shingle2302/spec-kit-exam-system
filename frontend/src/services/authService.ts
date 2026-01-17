import { processApiResponse, getAuthHeaders } from './api'
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types'

export const authService = {
  /**
   * Login with identifier (username/email/phone) and password
   */
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    })
    return processApiResponse<AuthResponse>(response)
  },

  /**
   * Register a new user account
   */
  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData)
    })
    return processApiResponse<AuthResponse>(response)
  },

  /**
   * Logout the current user
   */
  async logout(): Promise<void> {
    const response = await fetch('/api/auth/logout', {
      method: 'POST',
      headers: getAuthHeaders()
    })
    await processApiResponse<void>(response)
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