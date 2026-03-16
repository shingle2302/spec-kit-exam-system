import { useUserStore } from './user'
import { userService } from '@/services/userService'
import { message } from 'ant-design-vue'
import { createPinia, setActivePinia } from 'pinia'

// Mock dependencies
jest.mock('@/services/userService', () => ({
  userService: {
    getUsers: jest.fn(),
    getUserById: jest.fn(),
    createUser: jest.fn(),
    updateUser: jest.fn(),
    deleteUser: jest.fn(),
    unlockUser: jest.fn()
  }
}))

jest.mock('ant-design-vue', () => ({
  message: {
    success: jest.fn(),
    error: jest.fn()
  }
}))

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
        data: [
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
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      }

      mockUserService.getUsers.mockResolvedValue(mockResponse)

      const store = useUserStore()
      const result = await store.fetchUsers()

      expect(mockUserService.getUsers).toHaveBeenCalledWith({
        page: undefined,
        size: undefined,
        filters: { status: undefined }
      })

      expect(result).toEqual({ success: true, data: mockResponse })
      expect(store.users).toEqual(mockResponse.data)
      expect(store.usersPageData).toEqual(mockResponse)
      expect(store.pagination).toEqual({
        current: mockResponse.page,
        pageSize: mockResponse.size,
        total: mockResponse.total
      })
    })

    it('should handle fetch users error', async () => {
      const error = new Error('Fetch error')
      mockUserService.getUsers.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.fetchUsers()

      expect(mockUserService.getUsers).toHaveBeenCalled()
      expect(mockMessage.error).toHaveBeenCalledWith('获取用户列表失败')
      expect(result).toEqual({ success: false, message: '获取用户列表失败' })
    })
  })

  describe('fetchUserById', () => {
    it('should successfully fetch user by ID', async () => {
      const userId = '1'
      const mockUser = {
        id: userId,
        username: 'admin',
        email: 'admin@example.com',
        status: 'ACTIVE'
      }

      mockUserService.getUserById.mockResolvedValue(mockUser)

      const store = useUserStore()
      const result = await store.fetchUserById(userId)

      expect(mockUserService.getUserById).toHaveBeenCalledWith(userId)
      expect(result).toEqual({ success: true, data: mockUser })
      expect(store.currentEditUser).toEqual(mockUser)
    })

    it('should handle fetch user by ID error', async () => {
      const userId = '1'
      const error = new Error('Fetch error')
      mockUserService.getUserById.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.fetchUserById(userId)

      expect(mockUserService.getUserById).toHaveBeenCalledWith(userId)
      expect(mockMessage.error).toHaveBeenCalledWith('获取用户信息失败')
      expect(result).toEqual({ success: false, message: '获取用户信息失败' })
    })
  })

  describe('createUser', () => {
    it('should successfully create user', async () => {
      const userData = {
        username: 'newuser',
        email: 'newuser@example.com',
        password: 'Password123!',
        phone: '1234567890'
      }

      const mockCreatedUser = {
        id: '3',
        username: 'newuser',
        email: 'newuser@example.com',
        status: 'ACTIVE'
      }

      mockUserService.createUser.mockResolvedValue(mockCreatedUser)
      mockUserService.getUsers.mockResolvedValue({
        data: [mockCreatedUser],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      })

      const store = useUserStore()
      const result = await store.createUser(userData)

      expect(mockUserService.createUser).toHaveBeenCalledWith(userData)
      expect(mockMessage.success).toHaveBeenCalledWith('用户创建成功')
      expect(mockUserService.getUsers).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockCreatedUser })
    })

    it('should handle create user error', async () => {
      const userData = {
        username: 'newuser',
        email: 'newuser@example.com',
        password: 'Password123!',
        phone: '1234567890'
      }

      const error = new Error('Create error')
      mockUserService.createUser.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.createUser(userData)

      expect(mockUserService.createUser).toHaveBeenCalledWith(userData)
      expect(mockMessage.error).toHaveBeenCalledWith('创建用户失败')
      expect(result).toEqual({ success: false, message: '创建用户失败' })
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

      mockUserService.updateUser.mockResolvedValue(mockUpdatedUser)
      mockUserService.getUsers.mockResolvedValue({
        data: [mockUpdatedUser],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      })

      const store = useUserStore()
      const result = await store.updateUser(userId, userData)

      expect(mockUserService.updateUser).toHaveBeenCalledWith(userId, userData)
      expect(mockMessage.success).toHaveBeenCalledWith('用户更新成功')
      expect(mockUserService.getUsers).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockUpdatedUser })
    })

    it('should handle update user error', async () => {
      const userId = '1'
      const userData = {
        username: 'updateduser',
        email: 'updateduser@example.com',
        phone: '0987654321'
      }

      const error = new Error('Update error')
      mockUserService.updateUser.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.updateUser(userId, userData)

      expect(mockUserService.updateUser).toHaveBeenCalledWith(userId, userData)
      expect(mockMessage.error).toHaveBeenCalledWith('更新用户失败')
      expect(result).toEqual({ success: false, message: '更新用户失败' })
    })
  })

  describe('deleteUser', () => {
    it('should successfully delete user', async () => {
      const userId = '1'

      mockUserService.deleteUser.mockResolvedValue(undefined)
      mockUserService.getUsers.mockResolvedValue({
        data: [],
        total: 0,
        page: 1,
        size: 10,
        totalPage: 0,
        hasNext: false,
        hasPrevious: false
      })

      const store = useUserStore()
      const result = await store.deleteUser(userId)

      expect(mockUserService.deleteUser).toHaveBeenCalledWith(userId)
      expect(mockMessage.success).toHaveBeenCalledWith('用户删除成功')
      expect(mockUserService.getUsers).toHaveBeenCalled()
      expect(result).toEqual({ success: true })
    })

    it('should handle delete user error', async () => {
      const userId = '1'

      const error = new Error('Delete error')
      mockUserService.deleteUser.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.deleteUser(userId)

      expect(mockUserService.deleteUser).toHaveBeenCalledWith(userId)
      expect(mockMessage.error).toHaveBeenCalledWith('删除用户失败')
      expect(result).toEqual({ success: false, message: '删除用户失败' })
    })
  })

  describe('unlockUser', () => {
    it('should successfully unlock user', async () => {
      const userId = '1'

      mockUserService.unlockUser.mockResolvedValue(undefined)
      mockUserService.getUsers.mockResolvedValue({
        data: [
          {
            id: userId,
            username: 'lockeduser',
            email: 'lockeduser@example.com',
            status: 'ACTIVE'
          }
        ],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      })

      const store = useUserStore()
      const result = await store.unlockUser(userId)

      expect(mockUserService.unlockUser).toHaveBeenCalledWith(userId)
      expect(mockMessage.success).toHaveBeenCalledWith('用户账户已解锁')
      expect(mockUserService.getUsers).toHaveBeenCalled()
      expect(result).toEqual({ success: true })
    })

    it('should handle unlock user error', async () => {
      const userId = '1'

      const error = new Error('Unlock error')
      mockUserService.unlockUser.mockRejectedValue(error)

      const store = useUserStore()
      const result = await store.unlockUser(userId)

      expect(mockUserService.unlockUser).toHaveBeenCalledWith(userId)
      expect(mockMessage.error).toHaveBeenCalledWith('解锁用户失败')
      expect(result).toEqual({ success: false, message: '解锁用户失败' })
    })
  })

  describe('setCurrentEditUser', () => {
    it('should set current edit user', () => {
      const user = {
        id: '1',
        username: 'admin',
        email: 'admin@example.com',
        status: 'ACTIVE'
      }

      const store = useUserStore()
      store.setCurrentEditUser(user)

      expect(store.currentEditUser).toEqual(user)
    })

    it('should set current edit user to null', () => {
      const store = useUserStore()
      store.setCurrentEditUser(null)

      expect(store.currentEditUser).toBeNull()
    })
  })
})