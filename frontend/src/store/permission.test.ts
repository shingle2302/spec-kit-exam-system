import { usePermissionStore } from './permission'
import { permissionService } from '@/services/permissionService'
import { message } from 'ant-design-vue'
import { createPinia, setActivePinia } from 'pinia'

// Mock dependencies
jest.mock('@/services/permissionService', () => ({
  permissionService: {
    getAllPermissions: jest.fn(),
    getPermissionById: jest.fn(),
    createPermission: jest.fn(),
    updatePermission: jest.fn(),
    deletePermission: jest.fn()
  }
}))

jest.mock('ant-design-vue', () => ({
  message: {
    success: jest.fn(),
    error: jest.fn()
  }
}))

const mockPermissionService = permissionService as jest.Mocked<typeof permissionService>
const mockMessage = message as jest.Mocked<typeof message>

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
            menuId: '1',
            buttonName: 'Read',
            operationType: 'READ',
            permissionCode: 'MENU1:READ'
          }
        ],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      }

      mockPermissionService.getAllPermissions.mockResolvedValue(mockResponse)

      const store = usePermissionStore()
      const result = await store.fetchPermissions()

      expect(mockPermissionService.getAllPermissions).toHaveBeenCalledWith({
        page: undefined,
        size: undefined,
        filters: { status: undefined }
      })

      expect(result).toEqual({ success: true, data: mockResponse })
      expect(store.permissions).toEqual(mockResponse.data)
      expect(store.permissionsPageData).toEqual(mockResponse)
      expect(store.pagination).toEqual({
        current: mockResponse.page,
        pageSize: mockResponse.size,
        total: mockResponse.total
      })
    })

    it('should handle fetch permissions error', async () => {
      const error = new Error('Fetch error')
      mockPermissionService.getAllPermissions.mockRejectedValue(error)

      const store = usePermissionStore()
      const result = await store.fetchPermissions()

      expect(mockPermissionService.getAllPermissions).toHaveBeenCalled()
      expect(mockMessage.error).toHaveBeenCalledWith('获取权限列表失败')
      expect(result).toEqual({ success: false, message: '获取权限列表失败' })
    })
  })

  describe('fetchPermissionById', () => {
    it('should successfully fetch permission by ID', async () => {
      const permissionId = '1'
      const mockPermission = {
        id: permissionId,
        menuId: '1',
        buttonName: 'Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ'
      }

      mockPermissionService.getPermissionById.mockResolvedValue(mockPermission)

      const store = usePermissionStore()
      const result = await store.fetchPermissionById(permissionId)

      expect(mockPermissionService.getPermissionById).toHaveBeenCalledWith(permissionId)
      expect(result).toEqual({ success: true, data: mockPermission })
      expect(store.currentEditPermission).toEqual(mockPermission)
    })

    it('should handle fetch permission by ID error', async () => {
      const permissionId = '1'
      const error = new Error('Fetch error')
      mockPermissionService.getPermissionById.mockRejectedValue(error)

      const store = usePermissionStore()
      const result = await store.fetchPermissionById(permissionId)

      expect(mockPermissionService.getPermissionById).toHaveBeenCalledWith(permissionId)
      expect(mockMessage.error).toHaveBeenCalledWith('获取权限信息失败')
      expect(result).toEqual({ success: false, message: '获取权限信息失败' })
    })
  })

  describe('createPermission', () => {
    it('should successfully create permission', async () => {
      const permissionData = {
        menuId: '1',
        buttonName: 'Create',
        operationType: 'CREATE',
        permissionCode: 'MENU1:CREATE'
      }

      const mockCreatedPermission = {
        id: '3',
        ...permissionData
      }

      mockPermissionService.createPermission.mockResolvedValue(mockCreatedPermission)
      mockPermissionService.getAllPermissions.mockResolvedValue({
        data: [mockCreatedPermission],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      })

      const store = usePermissionStore()
      const result = await store.createPermission(permissionData)

      expect(mockPermissionService.createPermission).toHaveBeenCalledWith(permissionData)
      expect(mockMessage.success).toHaveBeenCalledWith('权限创建成功')
      expect(mockPermissionService.getAllPermissions).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockCreatedPermission })
    })

    it('should handle create permission error', async () => {
      const permissionData = {
        menuId: '1',
        buttonName: 'Create',
        operationType: 'CREATE',
        permissionCode: 'MENU1:CREATE'
      }

      const error = new Error('Create error')
      mockPermissionService.createPermission.mockRejectedValue(error)

      const store = usePermissionStore()
      const result = await store.createPermission(permissionData)

      expect(mockPermissionService.createPermission).toHaveBeenCalledWith(permissionData)
      expect(mockMessage.error).toHaveBeenCalledWith('创建权限失败')
      expect(result).toEqual({ success: false, message: '创建权限失败' })
    })
  })

  describe('updatePermission', () => {
    it('should successfully update permission', async () => {
      const permissionId = '1'
      const permissionData = {
        menuId: '1',
        buttonName: 'Updated Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ'
      }

      const mockUpdatedPermission = {
        id: permissionId,
        ...permissionData
      }

      mockPermissionService.updatePermission.mockResolvedValue(mockUpdatedPermission)
      mockPermissionService.getAllPermissions.mockResolvedValue({
        data: [mockUpdatedPermission],
        total: 1,
        page: 1,
        size: 10,
        totalPage: 1,
        hasNext: false,
        hasPrevious: false
      })

      const store = usePermissionStore()
      const result = await store.updatePermission(permissionId, permissionData)

      expect(mockPermissionService.updatePermission).toHaveBeenCalledWith({ ...permissionData, id: permissionId })
      expect(mockMessage.success).toHaveBeenCalledWith('权限更新成功')
      expect(mockPermissionService.getAllPermissions).toHaveBeenCalled()
      expect(result).toEqual({ success: true, data: mockUpdatedPermission })
    })

    it('should handle update permission error', async () => {
      const permissionId = '1'
      const permissionData = {
        menuId: '1',
        buttonName: 'Updated Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ'
      }

      const error = new Error('Update error')
      mockPermissionService.updatePermission.mockRejectedValue(error)

      const store = usePermissionStore()
      const result = await store.updatePermission(permissionId, permissionData)

      expect(mockPermissionService.updatePermission).toHaveBeenCalledWith({ ...permissionData, id: permissionId })
      expect(mockMessage.error).toHaveBeenCalledWith('更新权限失败')
      expect(result).toEqual({ success: false, message: '更新权限失败' })
    })
  })

  describe('deletePermission', () => {
    it('should successfully delete permission', async () => {
      const permissionId = '1'

      mockPermissionService.deletePermission.mockResolvedValue(undefined)
      mockPermissionService.getAllPermissions.mockResolvedValue({
        data: [],
        total: 0,
        page: 1,
        size: 10,
        totalPage: 0,
        hasNext: false,
        hasPrevious: false
      })

      const store = usePermissionStore()
      const result = await store.deletePermission(permissionId)

      expect(mockPermissionService.deletePermission).toHaveBeenCalledWith(permissionId)
      expect(mockMessage.success).toHaveBeenCalledWith('权限删除成功')
      expect(mockPermissionService.getAllPermissions).toHaveBeenCalled()
      expect(result).toEqual({ success: true })
    })

    it('should handle delete permission error', async () => {
      const permissionId = '1'

      const error = new Error('Delete error')
      mockPermissionService.deletePermission.mockRejectedValue(error)

      const store = usePermissionStore()
      const result = await store.deletePermission(permissionId)

      expect(mockPermissionService.deletePermission).toHaveBeenCalledWith(permissionId)
      expect(mockMessage.error).toHaveBeenCalledWith('删除权限失败')
      expect(result).toEqual({ success: false, message: '删除权限失败' })
    })
  })

  describe('setCurrentEditPermission', () => {
    it('should set current edit permission', () => {
      const permission = {
        id: '1',
        menuId: '1',
        buttonName: 'Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ'
      }

      const store = usePermissionStore()
      store.setCurrentEditPermission(permission)

      expect(store.currentEditPermission).toEqual(permission)
    })

    it('should set current edit permission to null', () => {
      const store = usePermissionStore()
      store.setCurrentEditPermission(null)

      expect(store.currentEditPermission).toBeNull()
    })
  })
})