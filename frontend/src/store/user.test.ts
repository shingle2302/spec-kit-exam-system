import { message } from 'ant-design-vue'
import { createPinia, setActivePinia } from 'pinia'

// Mock dependencies
jest.mock('@/services/userService', () => ({
  userService: {
    list: jest.fn(),
    getById: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
    remove: jest.fn(),
    unlock: jest.fn()
  }
}))

jest.mock('ant-design-vue', () => ({
  message: {
    success: jest.fn(),
    error: jest.fn()
  }
}))

import { useUserStore } from './user'
import { userService } from '@/services/userService'

const mockUserService = userService as jest.Mocked<typeof userService>
const mockMessage = message as jest.Mocked<typeof message>

describe('useUserStore', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    const pinia = createPinia()
    setActivePinia(pinia)
  })

  describe('fetchUsers', () => {
    it('should successfully fetch users', async () => {
      const mockResponse = {
        records: [
          {
            id: '1',
            username: 'admin',
            email: 'admin@example.com',
            status: 'ACTIVE'
          },
          {
            id: '2',
            username: 'user',
            email: 'user@example.com',
            status: 'ACTIVE'
          }
        ],
        total: 2,
        page: 1,
        size: 10
      }

      mockUserService.list.mockResolvedValue(mockResponse)

      const store = useUserStore()
      await store.fetchUsers()

      expect(mockUserService.list).toHaveBeenCalledWith({
        page: 1,
        size: 10
      })

      expect(store.users).toEqual(mockResponse.records)
      expect(store.loading).toBe(false)
    })

    it('should handle fetch users error', async () => {
      const error = new Error('Fetch error')
      mockUserService.list.mockRejectedValue(error)

      const store = useUserStore()
      await store.fetchUsers()

      expect(mockUserService.list).toHaveBeenCalled()
      expect(store.users).toEqual([])
      expect(store.loading).toBe(false)
    })
  })

  describe('updateUser', () => {
    it('should successfully update user', async () => {
      const userId = '1'
      const userData = {
        username: 'updateduser',
        email: 'updateduser@example.com',
        phone: '0987654321'
      }

      const mockUpdatedUser = {
        id: userId,
        username: 'updateduser',
        email: 'updateduser@example.com',
        phone: '0987654321',
        status: 'ACTIVE'
      }

      mockUserService.update.mockResolvedValue(mockUpdatedUser)

      const store = useUserStore()
      const result = await store.updateUser(userId, userData)

      expect(mockUserService.update).toHaveBeenCalledWith(userId, userData)
      expect(result).toBe(true)
    })

    it('should handle update user error', async () => {
      const userId = '1'
      const userData = {
        username: 'updateduser',
        email: 'updateduser@example.com',
        phone: '0987654321'
      }

      const error = new Error('Update error')
      mockUserService.update.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.updateUser(userId, userData)

      expect(mockUserService.update).toHaveBeenCalledWith(userId, userData)
      expect(result).toBe(false)
    })
  })

  describe('deleteUser', () => {
    it('should successfully delete user', async () => {
      const userId = '1'

      mockUserService.remove.mockResolvedValue(undefined)

      const store = useUserStore()
      const result = await store.deleteUser(userId)

      expect(mockUserService.remove).toHaveBeenCalledWith(userId)
      expect(result).toBe(true)
    })

    it('should handle delete user error', async () => {
      const userId = '1'

      const error = new Error('Delete error')
      mockUserService.remove.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.deleteUser(userId)

      expect(mockUserService.remove).toHaveBeenCalledWith(userId)
      expect(result).toBe(false)
    })
  })

  describe('setCurrentUser', () => {
    it('should set current user', () => {
      const user = {
        id: '1',
        username: 'admin',
        email: 'admin@example.com',
        status: 'ACTIVE'
      }

      const store = useUserStore()
      store.setCurrentUser(user)

      expect(store.currentUser).toEqual(user)
    })

    it('should clear current user', () => {
      const store = useUserStore()
      store.clearCurrentUser()

      expect(store.currentUser).toBeNull()
    })
  })
})