/**
 * Common type definitions for the application
 */

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export interface ApiError {
  code: string
  message: string
  details?: unknown
}

export interface ApiResponse<T> {
  data: T
  code: string
  msg: string
}

export interface PaginationParams {
  page?: number
  size?: number
  filters?: Record<string, unknown>
}

export interface FilterParams {
  name?: string
  status?: string
  [key: string]: unknown
}

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH'

export interface RequestOptions {
  method?: HttpMethod
  headers?: Record<string, string>
  body?: string | FormData | null
  cache?: RequestCache
  credentials?: RequestCredentials
}