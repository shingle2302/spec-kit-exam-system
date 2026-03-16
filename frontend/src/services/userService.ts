import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export class UserService extends BaseService<User, CreateUserRequest, UpdateUserRequest> {
  protected readonly baseUrl = '/api/users'

  async unlock(id: string): Promise<void> {
    return this.request<void>(`${this.baseUrl}/unlock/${id}`, { method: 'POST' })
  }

  async search(keyword: string): Promise<User[]> {
    return this.request<User[]>(`${this.baseUrl}/search?keyword=${encodeURIComponent(keyword)}`)
  }
}

export const userService = new UserService()