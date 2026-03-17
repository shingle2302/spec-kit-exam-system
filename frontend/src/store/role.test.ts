import { useRoleStore } from './role'
import { roleService } from '@/services/roleService'
import { message } from 'ant-design-vue'
import { createPinia, setActivePinia } from 'pinia'

// Mock dependencies
jest.mock('@/services/roleService', () => ({
  roleService: {
    list: jest.fn(),
    getById: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
    remove: jest.fn()
  }
}))

jest.mock('ant-design-vue', () => ({
  message: {
    success: jest.fn(),
    error: jest.fn()
  }
}))

const mockRoleService = roleService as jest.Mocked<typeof roleService>
const mockMessage = message as jest.Mocked<typeof message>

describe('useRoleStore', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    const pinia = createPinia()
    setActivePinia(pinia)
  })

  describe('fetchRoles', () => {
    it('should successfully fetch roles', async () => {
      const mockResponse = {
        records: [
          {
            id: '1',
            name: 'Admin',
            code: 'ADMIN',
            description: 'Administrator role',
            permissions: []
          },
          {
            id: '2',
            name: 'User',
            code: 'USER',
            description: 'Regular user role',
            permissions: []
          }
        ],
        total: 2,
        current: 1,
        size: 10,
        pages: 1
      }

      mockRoleService.list.mockResolvedValue(mockResponse)

      const store = useRoleStore()
      const result = await store.fetchRoles()

      expect(mockRoleService.list).toHaveBeenCalledWith({
        page: undefined,
        size: undefined,
        filters: {}
      })

      expect(result).toEqual({ success: true, data: mockResponse })
      expect(store.roles).toEqual(mockResponse.records)
      expect(store.rolesPageData).toEqual(mockResponse)
      expect(store.pagination).toEqual({
        current: mockResponse.current,
        pageSize: mockResponse.size,
        total: mockResponse.total
      })
    })

    it('should handle fetch roles error', async () => {
      const error = new Error('Fetch error')
      mockRoleService.list.mockRejectedValue(error)

      const store = useRoleStore()
      const result = await store.fetchRoles()

      expect(mockRoleService.list).toHaveBeenCalled()
      expect(mockMessage.error).toHaveBeenCalledWith('获取角色列表失败')
      expect(result).toEqual({ success: false, message: '获取角色列表失败' })
    })
  })

  describe('fetchRoleById', () => {
    it('should successfully fetch role by ID', async () => {
      const roleId = '1'
      const mockRole = {
        id: roleId,
        name: 'Admin',
        code: 'ADMIN',
        description: 'Administrator role',
        permissions: []
      }

      mockRoleService.getById.mockResolvedValue(mockRole)

      const store = useRoleStore()
      const result = await store.fetchRoleById(roleId)

      expect(mockRoleService.getById).toHaveBeenCalledWith(roleId)
      expect(result).toEqual({ success: true, data: mockRole })
      expect(store.currentEditRole).toEqual(mockRole)
    })

    it('should handle fetch role by ID error', async () => {
      const roleId = '1'
      const error = new Error('Fetch error')
      mockRoleService.getById.mockRejectedValue(error)

      const store = useRoleStore()
      const result = await store.fetchRoleById(roleId)

      expect(mockRoleService.getById).toHaveBeenCalledWith(roleId)
      expect(mockMessage.error).toHaveBeenCalledWith('获取角色信息失败')
      expect(result).toEqual({ success: false, message: '获取角色信息失败' })
    })
  })

  describe('createRole', () => {
    it('should successfully create role', async () => {
      const roleData = {
        name: 'New Role',
        code: 'NEW_ROLE',
        description: 'New role description',
        permissions: []
      }

      const mockCreatedRole = {
        id: '3',
        ...roleData
      }

      mockRoleService.create.mockResolvedValue(mockCreatedRole)
      mockRoleService.list.mockResolvedValue({
        records: [mockCreatedRole],
        total: 1,
        current: 1,
        size: 10,
        pages: 1
      })

      const store = useRoleStore()
      const result = await store.createRole(roleData)

      expect(mockRoleService.create).toHaveBeenCalledWith(roleData)
      expect(mockMessage.success).toHaveBeenCalledWith('角色创建成功')
      expect(mockRoleService.list).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockCreatedRole })
    })

    it('should handle create role error', async () => {
      const roleData = {
        name: 'New Role',
        code: 'NEW_ROLE',
        description: 'New role description',
        permissions: []
      }

      const error = new Error('Create error')
      mockRoleService.create.mockRejectedValue(error)

      const store = useRoleStore()
      const result = await store.createRole(roleData)

      expect(mockRoleService.create).toHaveBeenCalledWith(roleData)
      expect(mockMessage.error).toHaveBeenCalledWith('创建角色失败')
      expect(result).toEqual({ success: false, message: '创建角色失败' })
    })
  })

  describe('updateRole', () => {
    it('should successfully update role', async () => {
      const roleId = '1'
      const roleData = {
        name: 'Updated Role',
        code: 'UPDATED_ROLE',
        description: 'Updated role description',
        permissions: []
      }

      const mockUpdatedRole = {
        id: roleId,
        ...roleData
      }

      mockRoleService.update.mockResolvedValue(mockUpdatedRole)
      mockRoleService.list.mockResolvedValue({
        records: [mockUpdatedRole],
        total: 1,
        current: 1,
        size: 10,
        pages: 1
      })

      const store = useRoleStore()
      const result = await store.updateRole(roleId, roleData)

      expect(mockRoleService.update).toHaveBeenCalledWith(roleId, roleData)
      expect(mockMessage.success).toHaveBeenCalledWith('角色更新成功')
      expect(mockRoleService.list).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockUpdatedRole })
    })

    it('should handle update role error', async () => {
      const roleId = '1'
      const roleData = {
        name: 'Updated Role',
        code: 'UPDATED_ROLE',
        description: 'Updated role description',
        permissions: []
      }

      const error = new Error('Update error')
      mockRoleService.update.mockRejectedValue(error)

      const store = useRoleStore()
      const result = await store.updateRole(roleId, roleData)

      expect(mockRoleService.update).toHaveBeenCalledWith(roleId, roleData)
      expect(mockMessage.error).toHaveBeenCalledWith('更新角色失败')
      expect(result).toEqual({ success: false, message: '更新角色失败' })
    })
  })

  describe('deleteRole', () => {
    it('should successfully delete role', async () => {
      const roleId = '1'

      mockRoleService.remove.mockResolvedValue(undefined)
      mockRoleService.list.mockResolvedValue({
        records: [],
        total: 0,
        current: 1,
        size: 10,
        pages: 0
      })

      const store = useRoleStore()
      const result = await store.deleteRole(roleId)

      expect(mockRoleService.remove).toHaveBeenCalledWith(roleId)
      expect(mockMessage.success).toHaveBeenCalledWith('角色删除成功')
      expect(mockRoleService.list).toHaveBeenCalled()
      expect(result).toEqual({ success: true })
    })

    it('should handle delete role error', async () => {
      const roleId = '1'

      const error = new Error('Delete error')
      mockRoleService.remove.mockRejectedValue(error)

      const store = useRoleStore()
      const result = await store.deleteRole(roleId)

      expect(mockRoleService.remove).toHaveBeenCalledWith(roleId)
      expect(mockMessage.error).toHaveBeenCalledWith('删除角色失败')
      expect(result).toEqual({ success: false, message: '删除角色失败' })
    })
  })

  describe('setCurrentEditRole', () => {
    it('should set current edit role', () => {
      const role = {
        id: '1',
        name: 'Admin',
        code: 'ADMIN',
        description: 'Administrator role',
        permissions: []
      }

      const store = useRoleStore()
      store.setCurrentEditRole(role)

      expect(store.currentEditRole).toEqual(role)
    })

    it('should set current edit role to null', () => {
      const store = useRoleStore()
      store.setCurrentEditRole(null)

      expect(store.currentEditRole).toBeNull()
    })
  })
})