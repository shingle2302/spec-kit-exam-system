import { defineStore } from 'pinia';
import { ref } from 'vue';
import { permissionService } from '../services/permissionService';

interface Permission {
  id: number;
  name: string;
  code: string;
  type: string;
}

interface Role {
  id: number;
  name: string;
  code: string;
  permissions: Permission[];
}

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref<Permission[]>([]);
  const roles = ref<Role[]>([]);
  const currentRole = ref<Role | null>(null);
  const loading = ref(false);

  async function fetchPermissions() {
    loading.value = true;
    try {
      const response = await permissionService.list();
      permissions.value = response.data || [];
    } catch (error) {
      console.error('Failed to fetch permissions:', error);
    } finally {
      loading.value = false;
    }
  }

  async function fetchRoles() {
    loading.value = true;
    try {
      const response = await permissionService.getRoles();
      roles.value = response.data || [];
    } catch (error) {
      console.error('Failed to fetch roles:', error);
    } finally {
      loading.value = false;
    }
  }

  function hasPermission(permissionCode: string): boolean {
    if (!currentRole.value) {
      return false;
    }
    
    return currentRole.value.permissions.some(
      p => p.code === permissionCode
    );
  }

  function hasAnyPermission(permissionCodes: string[]): boolean {
    return permissionCodes.some(code => hasPermission(code));
  }

  function hasAllPermissions(permissionCodes: string[]): boolean {
    return permissionCodes.every(code => hasPermission(code));
  }

  function setCurrentRole(role: Role) {
    currentRole.value = role;
    localStorage.setItem('currentRole', JSON.stringify(role));
  }

  function clearCurrentRole() {
    currentRole.value = null;
    localStorage.removeItem('currentRole');
  }

  function loadRoleFromStorage() {
    const storedRole = localStorage.getItem('currentRole');
    if (storedRole) {
      currentRole.value = JSON.parse(storedRole);
    }
  }

  return {
    permissions,
    roles,
    currentRole,
    loading,
    fetchPermissions,
    fetchRoles,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    setCurrentRole,
    clearCurrentRole,
    loadRoleFromStorage
  };
});