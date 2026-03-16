import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from './auth'
import { authService } from '@/services/authService'

// Mock dependencies
jest.mock('@/services/authService', () => ({
  authService: {
    login: jest.fn(),
    register: jest.fn(),
    logout: jest.fn(),
    getToken: jest.fn(),
    getCurrentUser: jest.fn()
  }
}))

const mockAuthService = authService as jest.Mocked<typeof authService>

// Mock message
jest.mock('ant-design-vue', () => ({
  message: {
    success: jest.fn(),
    error: jest.fn()
  }
}))

describe('useAuthStore', () => {
  beforeEach(() => {
    // Clear localStorage
    localStorage.clear()
    // Create a fresh pinia instance
    const pinia = createPinia()
    setActivePinia(pinia)
    // Clear all mocks
    jest.clearAllMocks()
  })

  describe('initialize', () => {
    it('should initialize from localStorage', () => {
      const store = useAuthStore()
      
      // Set up mock data in localStorage
      localStorage.setItem('accessToken', 'test-token')
      localStorage.setItem('user', JSON.stringify({ id: '1', username: 'testuser' }))
      
      // Initialize store
      store.initialize()
      
      expect(store.token).toBe('test-token')
      expect(store.user).toEqual({ id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null })
    })

    it('should handle invalid user data', () => {
      const store = useAuthStore()
      
      // Set up invalid user data in localStorage
      localStorage.setItem('accessToken', 'test-token')
      localStorage.setItem('user', 'invalid-json')
      
      // Initialize store
      store.initialize()
      
      expect(store.token).toBe('test-token')
      expect(store.user).toBeNull()
    })
  })

  describe('login', () => {
    it('should successfully login', async () => {
      const store = useAuthStore()
      
      const credentials = {
        identifier: 'testuser',
        password: 'Password123!'
      }
      
      const mockResponse = {
        success: true,
        accessToken: 'test-token',
        user: { id: '1', username: 'testuser' }
      }
      
      mockAuthService.login.mockResolvedValue(mockResponse as any)
      
      const result = await store.login(credentials)
      
      expect(mockAuthService.login).toHaveBeenCalledWith(credentials)
      expect(result).toEqual({ success: true })
      expect(store.token).toBe('test-token')
      expect(store.user).toEqual({ id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null })
      expect(localStorage.getItem('accessToken')).toBe('test-token')
      expect(localStorage.getItem('user')).toBe(JSON.stringify({ id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null }))
    })

    it('should handle login failure', async () => {
      const store = useAuthStore()
      
      const credentials = {
        identifier: 'testuser',
        password: 'wrongpassword'
      }
      
      mockAuthService.login.mockResolvedValue({ success: false, message: 'Invalid credentials' } as any)
      
      const result = await store.login(credentials)
      
      expect(result).toEqual({ success: false, message: '登录失败' })
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
    })
  })

  describe('register', () => {
    it('should successfully register', async () => {
      const store = useAuthStore()
      
      const userData = {
        username: 'newuser',
        password: 'Password123!',
        email: 'new@example.com',
        phone: '1234567890'
      }
      
      mockAuthService.register.mockResolvedValue({ id: '1' } as any)
      
      const result = await store.register(userData)
      
      expect(mockAuthService.register).toHaveBeenCalledWith(userData)
      expect(result).toEqual({ success: true })
    })

    it('should handle registration failure', async () => {
      const store = useAuthStore()
      
      const userData = {
        username: 'newuser',
        password: 'Password123!',
        email: 'new@example.com',
        phone: '1234567890'
      }
      
      mockAuthService.register.mockResolvedValue({ success: false, message: 'Registration failed' } as any)
      
      const result = await store.register(userData)
      
      expect(result).toEqual({ success: false, message: '注册失败' })
    })
  })

  describe('logout', () => {
    it('should successfully logout', async () => {
      const store = useAuthStore()
      
      // Set up initial state
      store.token = 'test-token'
      store.user = { id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null }
      
      // Mock logout
      mockAuthService.logout.mockResolvedValue(undefined)
      
      await store.logout()
      
      expect(mockAuthService.logout).toHaveBeenCalled()
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
      expect(localStorage.getItem('accessToken')).toBeNull()
      expect(localStorage.getItem('user')).toBeNull()
    })

    it('should handle logout API error', async () => {
      const store = useAuthStore()
      
      // Set up initial state
      store.token = 'test-token'
      store.user = { id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null }
      
      // Mock logout error
      mockAuthService.logout.mockRejectedValue(new Error('Logout failed'))
      
      await store.logout()
      
      expect(mockAuthService.logout).toHaveBeenCalled()
      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
    })
  })

  describe('getters', () => {
    it('isAuthenticated should return true when token exists', () => {
      const store = useAuthStore()
      store.token = 'test-token'
      expect(store.isAuthenticated).toBe(true)
    })

    it('isAuthenticated should return false when token does not exist', () => {
      const store = useAuthStore()
      store.token = null
      expect(store.isAuthenticated).toBe(false)
    })

    it('isAdmin should return true for admin users', () => {
      const store = useAuthStore()
      store.user = {
        id: '1',
        username: 'admin',
        roleCode: 'ADMIN',
        roles: [],
        roleName: 'Admin'
      }
      expect(store.isAdmin).toBe(true)
    })

    it('isAdmin should return false for non-admin users', () => {
      const store = useAuthStore()
      store.user = {
        id: '1',
        username: 'user',
        roleCode: 'USER',
        roles: [],
        roleName: 'User'
      }
      expect(store.isAdmin).toBe(false)
    })

    it('currentUser should return the user', () => {
      const store = useAuthStore()
      const user = { id: '1', username: 'testuser', roles: [], roleCode: null, roleName: null }
      store.user = user
      expect(store.currentUser).toEqual(user)
    })
  })
})