import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class ExamWorkflowService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/exam-workflow'

  createPaper(payload: any) {
    return this.request<any>(`${this.baseUrl}/papers`, {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  scheduleSession(payload: any) {
    return this.request<any>(`${this.baseUrl}/sessions`, {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  startInvigilation(sessionId: number, teacherId: string) {
    return this.request<any>(`${this.baseUrl}/sessions/${sessionId}/invigilation/start?teacherId=${teacherId}`, {
      method: 'POST'
    })
  }

  endInvigilation(sessionId: number, teacherId: string) {
    return this.request<any>(`${this.baseUrl}/sessions/${sessionId}/invigilation/end?teacherId=${teacherId}`, {
      method: 'POST'
    })
  }

  submit(payload: any) {
    return this.request<any>(`${this.baseUrl}/submissions`, {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  grade(submissionId: number, score: number, gradedBy: string) {
    return this.request<any>(`${this.baseUrl}/submissions/${submissionId}/grade?score=${score}&gradedBy=${gradedBy}`, {
      method: 'POST'
    })
  }

  publish(sessionId: number) {
    return this.request<any>(`${this.baseUrl}/sessions/${sessionId}/publish`, {
      method: 'POST'
    })
  }

  analysis(sessionId: number) {
    return this.request<any>(`${this.baseUrl}/sessions/${sessionId}/analysis`, {
      method: 'POST'
    })
  }

  dashboard() {
    return this.request<any>(`${this.baseUrl}/dashboard`)
  }
}

export const examWorkflowService = new ExamWorkflowService()