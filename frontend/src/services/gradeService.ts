import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/grades';

export const gradeService = {
  /**
   * 获取年级列表
   */
  async list(page: number = 1, size: number = 10) {
    const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 获取活跃的年级列表
   */
  async listActive() {
    const response = await fetch(`${API_BASE_URL}/active`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据ID获取年级
   */
  async getById(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 创建年级
   */
  async create(grade: any) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(grade),
    });
    return processApiResponse(response);
  },

  /**
   * 更新年级
   */
  async update(id: number, grade: any) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(grade),
    });
    return processApiResponse(response);
  },

  /**
   * 删除年级
   */
  async delete(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },
};
