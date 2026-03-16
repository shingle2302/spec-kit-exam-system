import { userService } from './userService'
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

describe('userService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('getUsers', () => {
    it('should successfully get users with pagination', async () => {
      const params = {
        page: 1,
        size: 10,
        filters: { status: 'ACTIVE' }
      }

      const mockResponse = {
        records: [
          { id: '1', username: 'user1', email: 'user1@example.com' },
          { id: '2', username: 'user2', email: 'user2@example.com' }
        ],
        total: 2,
        current: 1,
        size: 10,
        pages: 1
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await userService.getUsers(params)

      expect(mockFetch).toHaveBeenCalledWith('/api/users/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 1,
          size: 10,
          filters: { status: 'ACTIVE' }
        })
      })

      expect(result).toEqual(mockResponse)
    })

    it('should use default parameters when none provided', async () => {
      const mockResponse = {
        records: [],
        total: 0,
        current: 1,
        size: 10,
        pages: 0
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      await userService.getUsers()

      expect(mockFetch).toHaveBeenCalledWith('/api/users/list', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 1,
          size: 10,
          filters: {}
        })
      })
    })
  })

  describe('getUserById', () => {
    it('should successfully get user by ID', async () => {
      const mockUser = { id: '1', username: 'testuser', email: 'test@example.com' }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockUser, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockUser)

      const result = await userService.getUserById('1')

      expect(mockFetch).toHaveBeenCalledWith('/api/users/1', {
        method: 'GET',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockUser)
    })
  })

  describe('createUser', () => {
    it('should successfully create user', async () => {
      const userData = {
        username: 'newuser',
        password: 'Password123!',
        email: 'new@example.com',
        phone: '1234567890'
      }

      const mockResponse = {
        id: '1',
        username: 'newuser',
        email: 'new@example.com'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await userService.createUser(userData)

      expect(mockFetch).toHaveBeenCalledWith('/api/users/create', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(userData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('updateUser', () => {
    it('should successfully update user', async () => {
      const userData = {
        username: 'updateduser',
        email: 'updated@example.com'
      }

      const mockResponse = {
        id: '1',
        username: 'updateduser',
        email: 'updated@example.com'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await userService.updateUser('1', userData)

      expect(mockFetch).toHaveBeenCalledWith('/api/users/update', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({ ...userData, id: '1' })
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('deleteUser', () => {
    it('should successfully delete user', async () => {
      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await userService.deleteUser('1')

      expect(mockFetch).toHaveBeenCalledWith('/api/users/delete/1', {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })

  describe('unlockUser', () => {
    it('should successfully unlock user', async () => {
      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await userService.unlockUser('1')

      expect(mockFetch).toHaveBeenCalledWith('/api/users/unlock/1', {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })
    })
  })
})