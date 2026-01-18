import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User, CreateUserRequest, UpdateUserRequest, PageResponse } from '@/types'
import { userService } from '@/services/userService'
import { message } from 'ant-design-vue'

export const useUserStore = defineStore('user', () => {
  // State
  const users = ref<User[]>([])
  const usersPageData = ref<PageResponse<User> | null>(null)
  const currentEditUser = ref<User | null>(null)
  const loading = ref(false)
  const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 0
  })

  // Actions
  async function fetchUsers(params?: { page?: number; limit?: number; status?: string }) {
    loading.value = true
    try {
      const response = await userService.getUsers(params)
      usersPageData.value = response
      users.value = response.data
      pagination.value.current = response.page
      pagination.value.pageSize = response.size
      pagination.value.total = response.total
      return { success: true, data: response }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取用户列表失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function fetchUserById(id: string) {
    loading.value = true
    try {
      const response = await userService.getUserById(id)
      currentEditUser.value = response
      return { success: true, data: response }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '获取用户信息失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function createUser(userData: CreateUserRequest) {
    loading.value = true
    try {
      const response = await userService.createUser(userData)
      message.success('用户创建成功')
      await fetchUsers()
      return { success: true, data: response }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '创建用户失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function updateUser(id: string, userData: UpdateUserRequest) {
    loading.value = true
    try {
      const response = await userService.updateUser(id, userData)
      message.success('用户更新成功')
      await fetchUsers()
      return { success: true, data: response }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '更新用户失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function deleteUser(id: string) {
    loading.value = true
    try {
      await userService.deleteUser(id)
      message.success('用户删除成功')
      await fetchUsers()
      return { success: true }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '删除用户失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function unlockUser(id: string) {
    loading.value = true
    try {
      await userService.unlockUser(id)
      message.success('用户账户已解锁')
      await fetchUsers()
      return { success: true }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '解锁用户失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  function setCurrentEditUser(user: User | null) {
    currentEditUser.value = user
  }

  return {
    users,
    usersPageData,
    currentEditUser,
    loading,
    pagination,
    fetchUsers,
    fetchUserById,
    createUser,
    updateUser,
    deleteUser,
    unlockUser,
    setCurrentEditUser
  }
})
