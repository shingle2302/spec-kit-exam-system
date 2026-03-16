import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/knowledge-points';

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const knowledgePointService = {
  /**
   * 获取知识点列表
   */
  async list(params: { page?: number; size?: number; filters?: Record<string, unknown> } = {}): Promise<PageResponse<any>> {
    const response = await fetch(`${API_BASE_URL}/list`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params.page ?? 1,
        size: params.size ?? 10,
        filters: params.filters ?? {}
      })
    });
    return processApiResponse<PageResponse<any>>(response);
  },

  /**
   * 获取知识点树结构
   */
  async getTree(): Promise<any> {
    const response = await fetch(`${API_BASE_URL}/tree`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 根据学科ID获取知识点树
   */
  async getTreeBySubjectId(subjectId: number): Promise<any> {
    const response = await fetch(`${API_BASE_URL}/tree/${subjectId}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 根据ID获取知识点
   */
  async getById(id: number): Promise<any> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 创建知识点
   */
  async create(knowledgePoint: any): Promise<any> {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(knowledgePoint),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 更新知识点
   */
  async update(id: number, knowledgePoint: any): Promise<any> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(knowledgePoint),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 删除知识点
   */
  async remove(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return processApiResponse<void>(response);
  },

  /**
   * 获取学科列表
   */
  async getSubjects(): Promise<any> {
    const response = await fetch('/api/subjects', {
      headers: getAuthHeaders(),
    });
    return processApiResponse<any>(response);
  },

  /**
   * 获取知识点统计
   */
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch(`${API_BASE_URL}/statistics`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse<Record<string, unknown>>(response);
  },
};