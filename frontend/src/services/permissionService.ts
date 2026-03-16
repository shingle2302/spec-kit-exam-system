import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class PermissionService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/permissions'

  async getRoles() {
    return this.request<any>(`${this.baseUrl}/roles`)
  }

  async getPermissionConfig(roleId: number) {
    return this.request<any>(`${this.baseUrl}/config/${roleId}`)
  }

  async savePermissionConfig(config: any) {
    return this.request<any>(`${this.baseUrl}/config`, {
      method: 'POST',
      body: JSON.stringify(config)
    })
  }

  async assignPermissions(roleId: number, permissionIds: number[]) {
    return this.request<any>(`${this.baseUrl}/assign`, {
      method: 'POST',
      body: JSON.stringify({ roleId, permissionIds })
    })
  }

  async revokePermissions(roleId: number, permissionIds: number[]) {
    return this.request<any>(`${this.baseUrl}/revoke`, {
      method: 'POST',
      body: JSON.stringify({ roleId, permissionIds })
    })
  }
}

export const permissionService = new PermissionService()