import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Role, CreateRoleRequest, UpdateRoleRequest } from '@/types'
import { roleService } from '@/services/roleService'
import { message } from 'ant-design-vue'

export const useRoleStore = defineStore('role', () => {
  // State
  const roles = ref<Role[]>([])
  const currentEditRole = ref<Role | null>(null)
  const loading = ref(false)

  // Actions
  async function fetchRoles() {
    loading.value = true
    try {
      const response = await roleService.getRoles()
      if (response.success && response.data) {
        roles.value = response.data
        return { success: true, data: response.data }
      }
      return { success: false, message: response.message }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取角色列表失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function fetchRoleById(id: string) {
    loading.value = true
    try {
      const response = await roleService.getRoleById(id)
      if (response.success && response.data) {
        currentEditRole.value = response.data
        return { success: true, data: response.data }
      }
      return { success: false, message: response.message }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取角色信息失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function createRole(roleData: CreateRoleRequest) {
    loading.value = true
    try {
      const response = await roleService.createRole(roleData)
      if (response.success) {
        message.success('角色创建成功')
        await fetchRoles()
        return { success: true, data: response.data }
      }
      message.error(response.message || '创建角色失败')
      return { success: false, message: response.message }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '创建角色失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function updateRole(id: string, roleData: UpdateRoleRequest) {
    loading.value = true
    try {
      const response = await roleService.updateRole(id, roleData)
      if (response.success) {
        message.success('角色更新成功')
        await fetchRoles()
        return { success: true, data: response.data }
      }
      message.error(response.message || '更新角色失败')
      return { success: false, message: response.message }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '更新角色失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function deleteRole(id: string) {
    loading.value = true
    try {
      const response = await roleService.deleteRole(id)
      if (response.success) {
        message.success('角色删除成功')
        await fetchRoles()
        return { success: true }
      }
      message.error(response.message || '删除角色失败')
      return { success: false, message: response.message }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '删除角色失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  function setCurrentEditRole(role: Role | null) {
    currentEditRole.value = role
  }

  return {
    roles,
    currentEditRole,
    loading,
    fetchRoles,
    fetchRoleById,
    createRole,
    updateRole,
    deleteRole,
    setCurrentEditRole
  }
})
