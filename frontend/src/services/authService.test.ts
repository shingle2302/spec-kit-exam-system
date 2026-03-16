import { authService } from './authService'
import { processApiResponse, getAuthHeaders } from './api'

// Mock dependencies
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}))

const mockProcessApiResponse = processApiResponse as jest.MockedFunction<typeof processApiResponse>
const mockGetAuthHeaders = getAuthHeaders as jest.MockedFunction<typeof getAuthHeaders>

// Mock fetch
global.fetch = jest.fn() as jest.MockedFunction<typeof fetch>

const mockFetch = global.fetch as jest.MockedFunction<typeof fetch>

describe('authService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
  })

  describe('login', () => {
    it('should successfully login and return auth payload', async () => {
      const credentials = {
        identifier: 'testuser',
        password: 'Password123!'
      }

      const mockResponse = {
        accessToken: 'test-token',
        user: {
          id: '1',
          username: 'testuser',
          email: 'test@example.com'
        }
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await authService.login(credentials)

      expect(mockFetch).toHaveBeenCalledWith('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
      })

      expect(result).toEqual(mockResponse)
    })

    it('should throw error when login fails', async () => {
      const credentials = {
        identifier: 'testuser',
        password: 'wrongpassword'
      }

      mockFetch.mockResolvedValueOnce({
        ok: false,
        status: 401,
        json: jest.fn().mockResolvedValueOnce({ message: 'Invalid credentials' })
      } as Response)

      mockProcessApiResponse.mockRejectedValueOnce(new Error('Invalid credentials'))

      await expect(authService.login(credentials)).rejects.toThrow('Invalid credentials')
    })
  })

  describe('register', () => {
    it('should successfully register and return user data', async () => {
      const userData = {
        username: 'newuser',
        password: 'Password123!',
        email: 'new@example.com',
        phone: '1234567890'
      }

      const mockResponse = {
        id: '1',
        username: 'newuser',
        email: 'new@example.com'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await authService.register(userData)

      expect(mockFetch).toHaveBeenCalledWith('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('logout', () => {
    it('should successfully logout', async () => {
      localStorage.setItem('accessToken', 'test-token')
      localStorage.setItem('user', JSON.stringify({ id: '1', username: 'testuser' }))

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await authService.logout()

      expect(mockFetch).toHaveBeenCalledWith('/api/auth/logout', {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(localStorage.getItem('accessToken')).toBeNull()
      expect(localStorage.getItem('user')).toBeNull()
    })
  })

  describe('getToken', () => {
    it('should return token from localStorage', () => {
      localStorage.setItem('accessToken', 'test-token')
      expect(authService.getToken()).toBe('test-token')
    })

    it('should return null when no token exists', () => {
      expect(authService.getToken()).toBeNull()
    })
  })

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      localStorage.setItem('accessToken', 'test-token')
      expect(authService.isAuthenticated()).toBe(true)
    })

    it('should return false when no token exists', () => {
      expect(authService.isAuthenticated()).toBe(false)
    })
  })

  describe('getCurrentUser', () => {
    it('should return user from localStorage', () => {
      const user = { id: '1', username: 'testuser' }
      localStorage.setItem('user', JSON.stringify(user))
      expect(authService.getCurrentUser()).toEqual(user)
    })

    it('should return null when no user exists', () => {
      expect(authService.getCurrentUser()).toBeNull()
    })

    it('should return null when user data is invalid JSON', () => {
      localStorage.setItem('user', 'invalid-json')
      expect(authService.getCurrentUser()).toBeNull()
    })
  })
})