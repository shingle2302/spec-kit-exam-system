import { getAuthHeaders, processApiResponse } from './api'

export const examWorkflowService = {
  createPaper(payload: any) {
    return fetch('/api/exam-workflow/papers', {
      method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload)
    }).then(processApiResponse<any>)
  },
  scheduleSession(payload: any) {
    return fetch('/api/exam-workflow/sessions', {
      method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload)
    }).then(processApiResponse<any>)
  },
  startInvigilation(sessionId: number, teacherId: string) {
    return fetch(`/api/exam-workflow/sessions/${sessionId}/invigilation/start?teacherId=${teacherId}`, {
      method: 'POST', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  },
  endInvigilation(sessionId: number, teacherId: string) {
    return fetch(`/api/exam-workflow/sessions/${sessionId}/invigilation/end?teacherId=${teacherId}`, {
      method: 'POST', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  },
  submit(payload: any) {
    return fetch('/api/exam-workflow/submissions', {
      method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload)
    }).then(processApiResponse<any>)
  },
  grade(submissionId: number, score: number, gradedBy: string) {
    return fetch(`/api/exam-workflow/submissions/${submissionId}/grade?score=${score}&gradedBy=${gradedBy}`, {
      method: 'POST', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  },
  publish(sessionId: number) {
    return fetch(`/api/exam-workflow/sessions/${sessionId}/publish`, {
      method: 'POST', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  },
  analysis(sessionId: number) {
    return fetch(`/api/exam-workflow/sessions/${sessionId}/analysis`, {
      method: 'POST', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  },
  dashboard() {
    return fetch('/api/exam-workflow/dashboard', {
      method: 'GET', headers: getAuthHeaders()
    }).then(processApiResponse<any>)
  }
}
