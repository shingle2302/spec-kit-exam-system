/**
 * API utility functions
 */

// Define constants
const API_CONFIG = {
  SUCCESS_CODE: '0000',
  DEFAULT_PAGE: 1,
  DEFAULT_SIZE: 10
} as const

// Define backend Result format
interface BackendResult<T> {
  data: T
  code: string
  msg: string
}

// Define API Error class
export class ApiError extends Error {
  constructor(
    public code: string,
    message: string,
    public details?: unknown
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

/**
 * Process backend API response according to Result format {data, code, msg}
 * @param response - The fetch response
 * @returns The actual data from response
 */
export async function processApiResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw new ApiError(
      response.status.toString(),
      `HTTP error! status: ${response.status}`,
      { status: response.status, statusText: response.statusText }
    )
  }

  const result: BackendResult<T> = await response.json()
  
  if (result.code !== API_CONFIG.SUCCESS_CODE) {
    throw new ApiError(result.code, result.msg || `Backend error with code: ${result.code}`)
  }

  return result.data
}

/**
 * Get authorization header with token
 */
export function getAuthHeaders(): Record<string, string> {
  const token = localStorage.getItem('accessToken')
  const headers: Record<string, string> = {
    'Content-Type': 'application/json'
  }
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  
  return headers
}

/**
 * Build query string from params
 */
export function buildQueryString(params: Record<string, unknown>): string {
  const searchParams = new URLSearchParams()
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      searchParams.append(key, String(value))
    }
  })
  return searchParams.toString()
}

/**
 * Get API config constants
 */
export function getApiConfig() {
  return API_CONFIG
}