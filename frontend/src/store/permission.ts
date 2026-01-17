import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Permission, PageResponse } from '@/types'
import { permissionService } from '@/services/permissionService'
import { message } from 'ant-design-vue'

export const usePermissionStore = defineStore('permission', () => {
  // State
  const permissions = ref<Permission[]>([])
  const permissionsPageData = ref<PageResponse<Permission> | null>(null)
  const currentEditPermission = ref<Permission | null>(null)
  const loading = ref(false)
  const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 0
  })

  // Actions
  async function fetchPermissions(params?: { page?: number; limit?: number; status?: string }) {
    loading.value = true
    try {
      const response = await permissionService.getAllPermissions(params)
      permissionsPageData.value = response
      permissions.value = response.data
      pagination.value.current = response.page
      pagination.value.pageSize = response.size
      pagination.value.total = response.total
      return { success: true, data: response }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取权限列表失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function fetchPermissionById(id: string) {
    loading.value = true
    try {
      const data = await permissionService.getPermissionById(id)
      currentEditPermission.value = data
      return { success: true, data }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取权限信息失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function createPermission(permissionData: Partial<Permission>) {
    loading.value = true
    try {
      const data = await permissionService.createPermission(permissionData)
      message.success('权限创建成功')
      await fetchPermissions()
      return { success: true, data }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '创建权限失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function updatePermission(id: string, permissionData: Partial<Permission>) {
    loading.value = true
    try {
      const data = await permissionService.updatePermission({ ...permissionData, id })
      message.success('权限更新成功')
      await fetchPermissions()
      return { success: true, data }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '更新权限失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function deletePermission(id: string) {
    loading.value = true
    try {
      await permissionService.deletePermission(id)
      message.success('权限删除成功')
      await fetchPermissions()
      return { success: true }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '删除权限失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  function setCurrentEditPermission(permission: Permission | null) {
    currentEditPermission.value = permission
  }

  return {
    permissions,
    permissionsPageData,
    currentEditPermission,
    loading,
    pagination,
    fetchPermissions,
    fetchPermissionById,
    createPermission,
    updatePermission,
    deletePermission,
    setCurrentEditPermission
  }
})