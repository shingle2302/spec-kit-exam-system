import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from './auth'
import { authService } from '@/services/authService'

// Mock dependencies
jest.mock('@/services/authService', () => ({
  authService: {
    login: jest.fn(),
    logout: jest.fn(),
    refreshToken: jest.fn()
  }
}))

const mockAuthService = authService as jest.Mocked<typeof authService>

describe('useAuthStore', () => {
  beforeEach(() => {
    localStorage.clear()
    const pinia = createPinia()
    setActivePinia(pinia)
    jest.clearAllMocks()
  })

  describe('login', () => {
    it('should successfully login', async () => {
      const store = useAuthStore()
      
      const credentials = {
        identifier: 'testuser',
        password: 'Password123!'
      }
      
      const mockResponse = {
        accessToken: 'test-token',
        refreshToken: 'refresh-token',
        user: { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      }
      
      mockAuthService.login.mockResolvedValue(mockResponse)
      
      const result = await store.login('testuser', 'Password123!')
      
      expect(mockAuthService.login).toHaveBeenCalledWith('testuser', 'Password123!')
      expect(result).toBe(true)
      expect(store.token).toBe('test-token')
      expect(store.user).toEqual(mockResponse.user)
      expect(localStorage.getItem('accessToken')).toBe('test-token')
      expect(localStorage.getItem('refreshToken')).toBe('refresh-token')
    })

    it('should handle login failure', async () => {
      const store = useAuthStore()
      
      mockAuthService.login.mockRejectedValue(new Error('Invalid credentials'))
      
      const result = await store.login('testuser', 'wrongpassword')
      
      expect(result).toBe(false)
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
    })
  })

  describe('refreshAccessToken', () => {
    it('should successfully refresh token', async () => {
      const store = useAuthStore()
      store.refreshToken = 'old-refresh-token'
      
      const mockResponse = {
        accessToken: 'new-access-token',
        refreshToken: 'new-refresh-token',
        user: { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      }
      
      mockAuthService.refreshToken.mockResolvedValue(mockResponse)
      
      const result = await store.refreshAccessToken()
      
      expect(mockAuthService.refreshToken).toHaveBeenCalledWith('old-refresh-token')
      expect(result).toBe(true)
      expect(store.token).toBe('new-access-token')
      expect(store.refreshToken).toBe('new-refresh-token')
    })

    it('should handle refresh token failure', async () => {
      const store = useAuthStore()
      store.refreshToken = null
      
      const result = await store.refreshAccessToken()
      
      expect(result).toBe(false)
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
    })
  })

  describe('logout', () => {
    it('should successfully logout', async () => {
      const store = useAuthStore()
      store.token = 'test-token'
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      
      await store.logout()
      
      expect(mockAuthService.logout).toHaveBeenCalled()
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
      expect(localStorage.getItem('accessToken')).toBeNull()
      expect(localStorage.getItem('refreshToken')).toBeNull()
      expect(localStorage.getItem('user')).toBeNull()
    })

    it('should handle logout failure', async () => {
      const store = useAuthStore()
      store.token = 'test-token'
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      
      mockAuthService.logout.mockRejectedValue(new Error('Logout failed'))
      
      await store.logout()
      
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
      expect(localStorage.getItem('accessToken')).toBeNull()
    })
  })

  describe('loadUserFromStorage', () => {
    it('should load user from storage', () => {
      const mockUser = { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      
      localStorage.setItem('user', JSON.stringify(mockUser))
      
      const store = useAuthStore()
      store.loadUserFromStorage()
      
      expect(store.user).toEqual(mockUser)
    })

    it('should not load user when storage is empty', () => {
      localStorage.removeItem('user')
      
      const store = useAuthStore()
      store.loadUserFromStorage()
      
      expect(store.user).toBeNull()
    })
  })

  describe('computed properties', () => {
    it('should compute isAuthenticated correctly', () => {
      const store = useAuthStore()
      
      expect(store.isAuthenticated).toBe(false)
      
      store.token = 'test-token'
      expect(store.isAuthenticated).toBe(true)
    })

    it('should compute isAdmin correctly', () => {
      const store = useAuthStore()
      
      expect(store.isAdmin).toBe(false)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'ADMIN' }
      expect(store.isAdmin).toBe(true)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'TEACHER' }
      expect(store.isAdmin).toBe(false)
    })

    it('should compute isTeacher correctly', () => {
      const store = useAuthStore()
      
      expect(store.isTeacher).toBe(false)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'TEACHER' }
      expect(store.isTeacher).toBe(true)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'STUDENT' }
      expect(store.isTeacher).toBe(false)
    })

    it('should compute isStudent correctly', () => {
      const store = useAuthStore()
      
      expect(store.isStudent).toBe(false)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'STUDENT' }
      expect(store.isStudent).toBe(true)
      
      store.user = { id: '1', username: 'testuser', email: 'test@example.com', role: 'TEACHER' }
      expect(store.isStudent).toBe(false)
    })
  })
})