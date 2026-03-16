import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export interface ClassItem {
  id?: number
  name: string
  gradeId: number
  educationalLevelId?: number
  capacity: number
  description?: string
  status?: string
}

export class ClassService extends BaseService<ClassItem> {
  protected readonly baseUrl = '/api/classes'

  async getGrades(): Promise<Array<{ id: number; name: string }>> {
    return this.request<Array<{ id: number; name: string }>>(`${this.baseUrl}/grades`)
  }
}

export const classService = new ClassService()