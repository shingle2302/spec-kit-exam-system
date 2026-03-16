import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'
import type { Role, CreateRoleRequest, UpdateRoleRequest } from '@/types'

function prepareRoleData(roleData: CreateRoleRequest | UpdateRoleRequest) {
  return {
    ...roleData,
    permissions: roleData.permissions ? JSON.stringify(roleData.permissions) : undefined
  }
}

function parseRoleResponse(role: any): Role {
  return {
    ...role,
    permissions: typeof role.permissions === 'string' ? JSON.parse(role.permissions) : role.permissions
  }
}

export class RoleService extends BaseService<Role, CreateRoleRequest, UpdateRoleRequest> {
  protected readonly baseUrl = '/api/roles'

  async list(params?: { page?: number; size?: number; filters?: Record<string, unknown> }): Promise<PageResponse<Role>> {
    const data = await super.list(params)
    if (data && data.records) {
      data.records = data.records.map(parseRoleResponse)
    }
    return data
  }

  async getById(id: string): Promise<Role> {
    const data = await super.getById(id)
    return parseRoleResponse(data)
  }

  async create(roleData: CreateRoleRequest): Promise<Role> {
    const data = await super.create(prepareRoleData(roleData))
    return parseRoleResponse(data)
  }

  async update(id: string, roleData: UpdateRoleRequest): Promise<Role> {
    const data = await super.update(id, {...prepareRoleData(roleData), id})
    return parseRoleResponse(data)
  }

  async getByCode(code: string): Promise<Role> {
    const data = await this.request<Role>(`${this.baseUrl}/code/${code}`)
    return parseRoleResponse(data)
  }
}

export const roleService = new RoleService()