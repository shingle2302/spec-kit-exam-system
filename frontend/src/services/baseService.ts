/**
 * Base service class for common API operations
 */

import { getAuthHeaders, processApiResponse } from '@/services/api'
import type { PageResponse, PaginationParams } from '@/types/common'

export abstract class BaseService<T, CreateT = Partial<T>, UpdateT = Partial<T>> {
  protected abstract readonly baseUrl: string

  protected async request<TResponse>(
    url: string,
    options: RequestInit = {}
  ): Promise<TResponse> {
    return processApiResponse<TResponse>(
      await fetch(url, {
        headers: getAuthHeaders(),
        ...options
      })
    )
  }

  async list(params: PaginationParams = {}): Promise<PageResponse<T>> {
    return this.request<PageResponse<T>>(`${this.baseUrl}/list`, {
      method: 'POST',
      body: JSON.stringify({
        page: params.page ?? 1,
        size: params.size ?? 10,
        filters: params.filters ?? {}
      })
    })
  }

  async getById(id: string | number): Promise<T> {
    return this.request<T>(`${this.baseUrl}/${id}`)
  }

  async create(data: CreateT): Promise<T> {
    return this.request<T>(this.baseUrl, {
      method: 'POST',
      body: JSON.stringify(data)
    })
  }

  async update(id: string | number, data: UpdateT): Promise<T> {
    return this.request<T>(`${this.baseUrl}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data)
    })
  }

  async remove(id: string | number): Promise<void> {
    return this.request<void>(`${this.baseUrl}/${id}`, {
      method: 'DELETE'
    })
  }

  async getStatistics(): Promise<Record<string, unknown>> {
    return this.request<Record<string, unknown>>(`${this.baseUrl}/statistics`)
  }
}