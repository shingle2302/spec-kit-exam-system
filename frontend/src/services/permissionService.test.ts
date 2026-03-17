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

  describe('list', () => {
    it('should successfully get permissions with pagination', async () => {
      const mockResponse = {
        records: [
          {
            id: '1',
            name: 'Read',
            code: 'MENU1:READ',
            type: 'BUTTON'
          }
        ],
        total: 1,
        current: 1,
        size: 10,
        pages: 1
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await permissionService.list()

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({ page: 1, size: 10, filters: {} })
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('getRoles', () => {
    it('should successfully get roles', async () => {
      const mockRoles = [
        { id: '1', name: 'Admin', code: 'ADMIN' },
        { id: '2', name: 'User', code: 'USER' }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockRoles, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockRoles)

      const result = await permissionService.getRoles()

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/roles', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockRoles)
    })
  })

  describe('getPermissionConfig', () => {
    it('should successfully get permission config', async () => {
      const roleId = 1
      const mockConfig = {
        roleId: 1,
        permissions: ['PERM1', 'PERM2']
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockConfig, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockConfig)

      const result = await permissionService.getPermissionConfig(roleId)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/config/1', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockConfig)
    })
  })

  describe('savePermissionConfig', () => {
    it('should successfully save permission config', async () => {
      const config = {
        roleId: 1,
        permissions: ['PERM1', 'PERM2']
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.savePermissionConfig(config)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/config', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(config)
      })
    })
  })

  describe('assignPermissions', () => {
    it('should successfully assign permissions to role', async () => {
      const roleId = 1
      const permissionIds = [1, 2, 3]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.assignPermissions(roleId, permissionIds)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/assign', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({ roleId, permissionIds })
      })
    })
  })

  describe('revokePermissions', () => {
    it('should successfully revoke permissions from role', async () => {
      const roleId = 1
      const permissionIds = [1, 2]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await permissionService.revokePermissions(roleId, permissionIds)

      expect(mockFetch).toHaveBeenCalledWith('/api/permissions/revoke', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({ roleId, permissionIds })
      })
    })
  })
})