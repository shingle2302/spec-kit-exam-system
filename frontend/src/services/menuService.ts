import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'
import type { Menu } from '@/types'

export class MenuService extends BaseService<Menu, Partial<Menu>, Partial<Menu>> {
  protected readonly baseUrl = '/api/menus'

  async getMenuTree(roleId: string | null = null): Promise<Menu[]> {
    let url = `${this.baseUrl}/tree`
    if (roleId) {
      url += `?roleId=${encodeURIComponent(roleId)}`
    }
    return this.request<Menu[]>(url)
  }

  async update(menuId: string, menu: Partial<Menu>): Promise<Menu> {
    return super.update(menuId, {...menu, id: menuId})
  }
}

export const menuService = new MenuService()