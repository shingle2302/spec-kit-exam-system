import { permissionService } from './permissionService'
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

describe('permissionService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('getPermissionsByRole', () => {
    it('should successfully get permissions by role', async () => {
      const roleId = 'role1'
      const mockPermissions = [
        {
          id: '1',
          menuId: '1',
          buttonName: 'Read',
          operationType: 'READ',
          permissionCode: 'MENU1:READ',
          description: 'Read permission for menu 1'
        },
        {
          id: '2',
          menuId: '1',
          buttonName: 'Write',
          operationType: 'WRITE',
          permissionCode: 'MENU1:WRITE',
          description: 'Write permission for menu 1'
        }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockPermissions, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockPermissions)

      const result = await permissionService.getPermissionsByRole(roleId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/permissions/role/${encodeURIComponent(roleId)}`, {
        method: 'GET',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockPermissions)
    })
  })

  describe('assignPermissionsToRole', () => {
    it('should successfully assign permissions to role', async () => {
      const roleId = 'role1'
      const permissionIds = ['perm1', 'perm2']

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.assignPermissionsToRole(roleId, permissionIds)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/assign', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          roleId,
          permissionIds
        })
      })
    })
  })

  describe('removePermissionsFromRole', () => {
    it('should successfully remove permissions from role', async () => {
      const roleId = 'role1'
      const permissionIds = ['perm1', 'perm2']

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.removePermissionsFromRole(roleId, permissionIds)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/remove', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          roleId,
          permissionIds
        })
      })
    })
  })

  describe('getAllPermissions', () => {
    it('should successfully get all permissions with default params', async () => {
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

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.getAllPermissions()

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 1,
          size: 10,
          filters: {}
        })
      })

      expect(result).toEqual(mockResponse)
    })

    it('should successfully get all permissions with custom params', async () => {
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
        page: 2,
        size: 20,
        totalPage: 1,
        hasNext: false,
        hasPrevious: true
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.getAllPermissions({
        page: 2,
        size: 20,
        filters: { menuId: '1' }
      })

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 2,
          size: 20,
          filters: { menuId: '1' }
        })
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('createPermission', () => {
    it('should successfully create permission', async () => {
      const permissionData = {
        menuId: '1',
        buttonName: 'Create',
        operationType: 'CREATE',
        permissionCode: 'MENU1:CREATE',
        description: 'Create permission for menu 1'
      }

      const mockResponse = {
        id: '3',
        ...permissionData
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.createPermission(permissionData)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/create', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(permissionData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('updatePermission', () => {
    it('should successfully update permission', async () => {
      const permissionData = {
        id: '1',
        menuId: '1',
        buttonName: 'Updated Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ',
        description: 'Updated read permission for menu 1'
      }

      const mockResponse = {
        ...permissionData
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.updatePermission(permissionData)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/update', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(permissionData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('getPermissionById', () => {
    it('should successfully get permission by ID', async () => {
      const permissionId = '1'
      const mockResponse = {
        id: permissionId,
        menuId: '1',
        buttonName: 'Read',
        operationType: 'READ',
        permissionCode: 'MENU1:READ',
        description: 'Read permission for menu 1'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.getPermissionById(permissionId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/permissions/${encodeURIComponent(permissionId)}`, {
        method: 'GET',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('deletePermission', () => {
    it('should successfully delete permission', async () => {
      const permissionId = '1'

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.deletePermission(permissionId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/permissions/delete/${encodeURIComponent(permissionId)}`, {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })
})