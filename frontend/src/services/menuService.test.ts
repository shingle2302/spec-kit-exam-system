import { menuService } from './menuService'
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

describe('menuService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('getMenuTree', () => {
    it('should successfully get menu tree without roleId', async () => {
      const mockMenuTree = [
        {
          id: '1',
          name: 'Dashboard',
          path: '/dashboard',
          icon: 'dashboard',
          children: []
        },
        {
          id: '2',
          name: 'Users',
          path: '/users',
          icon: 'user',
          children: [
            {
              id: '3',
              name: 'User Management',
              path: '/users/list',
              icon: 'user-list'
            }
          ]
        }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockMenuTree, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockMenuTree)

      const result = await menuService.getMenuTree()

      expect(mockFetch).toHaveBeenCalledWith('/api/menus/tree', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockMenuTree)
    })

    it('should successfully get menu tree with roleId', async () => {
      const roleId = 'role1'
      const mockMenuTree = [
        {
          id: '1',
          name: 'Dashboard',
          path: '/dashboard',
          icon: 'dashboard',
          children: []
        }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockMenuTree, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockMenuTree)

      const result = await menuService.getMenuTree(roleId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/menus/tree?roleId=${encodeURIComponent(roleId)}`, {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockMenuTree)
    })
  })

  describe('createMenu', () => {
    it('should successfully create menu', async () => {
      const menuData = {
        name: 'New Menu',
        path: '/new-menu',
        icon: 'plus',
        parentId: '1'
      }

      const mockResponse = {
        id: '3',
        ...menuData,
        children: []
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await menuService.create(menuData)

      expect(mockFetch).toHaveBeenCalledWith('/api/menus', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(menuData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('updateMenu', () => {
    it('should successfully update menu', async () => {
      const menuData = {
        id: '1',
        name: 'Updated Menu',
        path: '/updated-menu',
        icon: 'edit'
      }

      const mockResponse = {
        ...menuData,
        children: []
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await menuService.update('1', menuData)

      expect(mockFetch).toHaveBeenCalledWith('/api/menus/1', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(menuData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('deleteMenu', () => {
    it('should successfully delete menu', async () => {
      const menuId = '1'

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await menuService.remove(menuId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/menus/${encodeURIComponent(menuId)}`, {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })

  describe('getAllMenus', () => {
    it('should successfully get all menus with default params', async () => {
      const mockResponse = {
        data: [
          {
            id: '1',
            name: 'Dashboard',
            path: '/dashboard',
            icon: 'dashboard'
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

      const result = await menuService.list()

      expect(mockFetch).toHaveBeenCalledWith('/api/menus/list', {
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

    it('should successfully get all menus with custom params', async () => {
      const mockResponse = {
        data: [
          {
            id: '1',
            name: 'Dashboard',
            path: '/dashboard',
            icon: 'dashboard'
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

      const result = await menuService.list({
        page: 2,
        size: 20,
        filters: { name: 'Dashboard' }
      })

      expect(mockFetch).toHaveBeenCalledWith('/api/menus/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 2,
          size: 20,
          filters: { name: 'Dashboard' }
        })
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('getMenuById', () => {
    it('should successfully get menu by ID', async () => {
      const menuId = '1'
      const mockResponse = {
        id: menuId,
        name: 'Dashboard',
        path: '/dashboard',
        icon: 'dashboard',
        children: []
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await menuService.getById(menuId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/menus/${encodeURIComponent(menuId)}`, {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })
})