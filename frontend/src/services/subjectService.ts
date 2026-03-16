import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export interface SubjectItem {
  id?: number
  name: string
  classId: number
  educationalLevelId: number
  description?: string
  specialization?: string
  status?: string
}

export class SubjectService extends BaseService<SubjectItem> {
  protected readonly baseUrl = '/api/subjects'

  async getClasses(): Promise<Array<{ id: number; name: string }>> {
    return this.request<Array<{ id: number; name: string }>>(`${this.baseUrl}/classes`)
  }

  async getLevels(): Promise<Array<{ id: number; name: string }>> {
    return this.request<Array<{ id: number; name: string }>>(`${this.baseUrl}/levels`)
  }
}

export const subjectService = new SubjectService()