import { roleService } from './roleService'
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

describe('roleService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('getRoles', () => {
    it('should successfully get roles with default params', async () => {
      const mockResponse = {
        records: [
          {
            id: '1',
            name: 'Admin',
            code: 'ADMIN',
            description: 'Administrator role',
            permissions: '[]'
          },
          {
            id: '2',
            name: 'User',
            code: 'USER',
            description: 'Regular user role',
            permissions: '[]'
          }
        ],
        total: 2,
        page: 1,
        size: 10
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.list()

      expect(mockFetch).toHaveBeenCalledWith('/api/roles/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 1,
          size: 10,
          filters: {}
        })
      })

      expect(result).toEqual({
        ...mockResponse,
        records: mockResponse.records.map((role: any) => ({
          ...role,
          permissions: []
        }))
      })
    })

    it('should successfully get roles with custom params', async () => {
      const mockResponse = {
        records: [
          {
            id: '1',
            name: 'Admin',
            code: 'ADMIN',
            description: 'Administrator role',
            permissions: '[]'
          }
        ],
        total: 1,
        page: 2,
        size: 20
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.list({
        page: 2,
        size: 20,
        filters: { name: 'Admin' }
      })

      expect(mockFetch).toHaveBeenCalledWith('/api/roles/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 2,
          size: 20,
          filters: { name: 'Admin' }
        })
      })

      expect(result).toEqual({
        ...mockResponse,
        records: mockResponse.records.map((role: any) => ({
          ...role,
          permissions: []
        }))
      })
    })
  })

  describe('getRoleById', () => {
    it('should successfully get role by ID', async () => {
      const roleId = '1'
      const mockResponse = {
        id: roleId,
        name: 'Admin',
        code: 'ADMIN',
        description: 'Administrator role',
        permissions: '[]'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.getById(roleId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/roles/${roleId}`, {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual({
        ...mockResponse,
        permissions: []
      })
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

      const mockResponse = {
        id: '3',
        ...roleData,
        permissions: '[]'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.create(roleData)

      expect(mockFetch).toHaveBeenCalledWith('/api/roles', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          ...roleData,
          permissions: JSON.stringify(roleData.permissions)
        })
      })

      expect(result).toEqual({
        ...mockResponse,
        permissions: []
      })
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

      const mockResponse = {
        id: roleId,
        ...roleData,
        permissions: '[]'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.update(roleId, roleData)

      expect(mockFetch).toHaveBeenCalledWith(`/api/roles/${roleId}`, {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          ...roleData,
          permissions: JSON.stringify(roleData.permissions),
          id: roleId
        })
      })

      expect(result).toEqual({
        ...mockResponse,
        permissions: []
      })
    })
  })

  describe('deleteRole', () => {
    it('should successfully delete role', async () => {
      const roleId = '1'

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await roleService.remove(roleId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/roles/${roleId}`, {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })

  describe('getRoleByCode', () => {
    it('should successfully get role by code', async () => {
      const roleCode = 'ADMIN'
      const mockResponse = {
        id: '1',
        name: 'Admin',
        code: roleCode,
        description: 'Administrator role',
        permissions: '[]'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await roleService.getByCode(roleCode)

      expect(mockFetch).toHaveBeenCalledWith(`/api/roles/code/${roleCode}`, {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual({
        ...mockResponse,
        permissions: JSON.parse(mockResponse.permissions)
      })
    })
  })
})