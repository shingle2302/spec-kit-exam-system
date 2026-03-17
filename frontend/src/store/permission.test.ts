import { usePermissionStore } from './permission'
import { permissionService } from '@/services/permissionService'
import { createPinia, setActivePinia } from 'pinia'

// Mock dependencies
jest.mock('@/services/permissionService', () => ({
  permissionService: {
    list: jest.fn(),
    getRoles: jest.fn()
  }
}))

const mockPermissionService = permissionService as jest.Mocked<typeof permissionService>

describe('usePermissionStore', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    const pinia = createPinia()
    setActivePinia(pinia)
  })

  describe('fetchPermissions', () => {
    it('should successfully fetch permissions', async () => {
      const mockResponse = {
        data: [
          {
            id: '1',
            name: 'Read',
            code: 'MENU1:READ',
            type: 'BUTTON'
          }
        ]
      }

      mockPermissionService.list.mockResolvedValue(mockResponse)

      const store = usePermissionStore()
      await store.fetchPermissions()

      expect(mockPermissionService.list).toHaveBeenCalled()
      expect(store.permissions).toEqual(mockResponse.data)
    })

    it('should handle fetch permissions error', async () => {
      const error = new Error('Fetch error')
      mockPermissionService.list.mockRejectedValue(error)

      const store = usePermissionStore()
      await store.fetchPermissions()

      expect(mockPermissionService.list).toHaveBeenCalled()
      expect(store.permissions).toEqual([])
    })
  })

  describe('fetchRoles', () => {
    it('should successfully fetch roles', async () => {
      const mockResponse = {
        data: [
          {
            id: '1',
            name: 'Admin',
            code: 'ADMIN',
            permissions: []
          }
        ]
      }

      mockPermissionService.getRoles.mockResolvedValue(mockResponse)

      const store = usePermissionStore()
      await store.fetchRoles()

      expect(mockPermissionService.getRoles).toHaveBeenCalled()
      expect(store.roles).toEqual(mockResponse.data)
    })

    it('should handle fetch roles error', async () => {
      const error = new Error('Fetch error')
      mockPermissionService.getRoles.mockRejectedValue(error)

      const store = usePermissionStore()
      await store.fetchRoles()

      expect(mockPermissionService.getRoles).toHaveBeenCalled()
      expect(store.roles).toEqual([])
    })
  })

  describe('hasPermission', () => {
    it('should return true when user has permission', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: [
          { id: '1', name: 'Read', code: 'MENU1:READ', type: 'BUTTON' }
        ]
      }

      store.setCurrentRole(mockRole)

      expect(store.hasPermission('MENU1:READ')).toBe(true)
    })

    it('should return false when user does not have permission', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: []
      }

      store.setCurrentRole(mockRole)

      expect(store.hasPermission('MENU1:READ')).toBe(false)
    })

    it('should return false when no role is set', () => {
      const store = usePermissionStore()

      expect(store.hasPermission('MENU1:READ')).toBe(false)
    })
  })

  describe('hasAnyPermission', () => {
    it('should return true when user has any of the permissions', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: [
          { id: '1', name: 'Read', code: 'MENU1:READ', type: 'BUTTON' }
        ]
      }

      store.setCurrentRole(mockRole)

      expect(store.hasAnyPermission(['MENU1:READ', 'MENU1:WRITE'])).toBe(true)
    })

    it('should return false when user has none of the permissions', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: []
      }

      store.setCurrentRole(mockRole)

      expect(store.hasAnyPermission(['MENU1:READ', 'MENU1:WRITE'])).toBe(false)
    })
  })

  describe('hasAllPermissions', () => {
    it('should return true when user has all permissions', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: [
          { id: '1', name: 'Read', code: 'MENU1:READ', type: 'BUTTON' },
          { id: '2', name: 'Write', code: 'MENU1:WRITE', type: 'BUTTON' }
        ]
      }

      store.setCurrentRole(mockRole)

      expect(store.hasAllPermissions(['MENU1:READ', 'MENU1:WRITE'])).toBe(true)
    })

    it('should return false when user does not have all permissions', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: [
          { id: '1', name: 'Read', code: 'MENU1:READ', type: 'BUTTON' }
        ]
      }

      store.setCurrentRole(mockRole)

      expect(store.hasAllPermissions(['MENU1:READ', 'MENU1:WRITE'])).toBe(false)
    })
  })

  describe('setCurrentRole', () => {
    it('should set current role', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: []
      }

      store.setCurrentRole(mockRole)

      expect(store.currentRole).toEqual(mockRole)
      expect(localStorage.getItem('currentRole')).toBe(JSON.stringify(mockRole))
    })
  })

  describe('clearCurrentRole', () => {
    it('should clear current role', () => {
      const store = usePermissionStore()
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: []
      }

      store.setCurrentRole(mockRole)
      store.clearCurrentRole()

      expect(store.currentRole).toBeNull()
      expect(localStorage.getItem('currentRole')).toBeNull()
    })
  })

  describe('loadRoleFromStorage', () => {
    it('should load role from storage', () => {
      const mockRole = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        permissions: []
      }

      localStorage.setItem('currentRole', JSON.stringify(mockRole))

      const store = usePermissionStore()
      store.loadRoleFromStorage()

      expect(store.currentRole).toEqual(mockRole)
    })

    it('should not load role when storage is empty', () => {
      localStorage.removeItem('currentRole')

      const store = usePermissionStore()
      store.loadRoleFromStorage()

      expect(store.currentRole).toBeNull()
    })
  })
})