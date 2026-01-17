import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, RegisterRequest } from '@/types'
import { authService } from '@/services/authService'
import { message } from 'ant-design-vue'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const loading = ref(false)

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => {
    // Debug logging to understand the issue
    console.log('Checking if user is admin:', user.value?.isSuperAdmin, user.value);
    return user.value?.isSuperAdmin === true;
  })
  const currentUser = computed(() => user.value)

  // Initialize from localStorage
  function initialize() {
    const storedToken = localStorage.getItem('accessToken')
    const storedUser = localStorage.getItem('user')
    if (storedToken) {
      token.value = storedToken
    }
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch {
        user.value = null
      }
    }
  }

  // Actions
  async function login(credentials: LoginRequest) {
    loading.value = true
    try {
      const response = await authService.login(credentials)
      if (response && response.accessToken && response.user) {
        token.value = response.accessToken
        user.value = response.user
        localStorage.setItem('accessToken', response.accessToken)
        localStorage.setItem('user', JSON.stringify(response.user))
        message.success('登录成功')
        return { success: true }
      } else {
        message.error(response.message || '登录失败')
        return { success: false, message: response.message }
      }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '登录失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function register(userData: RegisterRequest) {
    loading.value = true
    try {
      const response = await authService.register(userData)
      if (response) {
        message.success('注册成功，请登录')
        return { success: true }
      } else {
        message.error(response.message || '注册失败')
        return { success: false, message: response.message }
      }
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '注册失败'
      message.error(errorMsg)
      return { success: false, message: errorMsg }
    } finally {
      loading.value = false
    }
  }

  async function logout() {
    try {
      await authService.logout()
    } catch {
      // Ignore logout API errors
    }
    user.value = null
    token.value = null
    localStorage.removeItem('accessToken')
    localStorage.removeItem('user')
    message.success('已退出登录')
  }

  return {
    user,
    token,
    loading,
    isAuthenticated,
    isAdmin,
    currentUser,
    initialize,
    login,
    register,
    logout
  }
})
