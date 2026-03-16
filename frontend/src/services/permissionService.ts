import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/permissions';

export const permissionService = {
  async list() {
    const response = await fetch(`${API_BASE_URL}/list`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  async getRoles() {
    const response = await fetch(`${API_BASE_URL}/roles`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  async getPermissionConfig(roleId: number) {
    const response = await fetch(`${API_BASE_URL}/config/${roleId}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  async savePermissionConfig(config: any) {
    const response = await fetch(`${API_BASE_URL}/config`, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(config),
    });
    return processApiResponse(response);
  },

  async assignPermissions(roleId: number, permissionIds: number[]) {
    const response = await fetch(`${API_BASE_URL}/assign`, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ roleId, permissionIds }),
    });
    return processApiResponse(response);
  },

  async revokePermissions(roleId: number, permissionIds: number[]) {
    const response = await fetch(`${API_BASE_URL}/revoke`, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ roleId, permissionIds }),
    });
    return processApiResponse(response);
  },
};