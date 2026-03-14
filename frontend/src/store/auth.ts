import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, RegisterRequest } from '@/types'
import { authService } from '@/services/authService'
import { message } from 'ant-design-vue'

const ADMIN_ROLE_IDENTIFIERS = new Set(['ADMIN', 'PRINCIPAL', 'SUPER_ADMIN'])

function extractRoleIdentifiers(user: User): string[] {
  const identifiers = new Set<string>()

  if (user.roleCode) {
    identifiers.add(String(user.roleCode).toUpperCase())
  }

  if (user.roleName) {
    identifiers.add(String(user.roleName).toUpperCase())
  }

  user.roles?.forEach((role) => {
    if (typeof role === 'string') {
      identifiers.add(role.toUpperCase())
      return
    }

    if (role.code) {
      identifiers.add(String(role.code).toUpperCase())
    }

    if (role.name) {
      identifiers.add(String(role.name).toUpperCase())
    }
  })

  return Array.from(identifiers)
}

function normalizeUser(rawUser: User | null): User | null {
  if (!rawUser) {
    return null
  }

  const normalizedUser: User = {
    ...rawUser,
    roles: rawUser.roles ?? [],
    roleCode: rawUser.roleCode ?? null,
    roleName: rawUser.roleName ?? null
  }

  if (!normalizedUser.roleCode || !normalizedUser.roleName) {
    const firstRole = normalizedUser.roles.find((role) => typeof role !== 'string')

    if (!normalizedUser.roleCode && firstRole && firstRole.code) {
      normalizedUser.roleCode = firstRole.code
    }

    if (!normalizedUser.roleName && firstRole && firstRole.name) {
      normalizedUser.roleName = firstRole.name
    }
  }

  return normalizedUser
}

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const loading = ref(false)

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => {
    if (!user.value) {
      return false
    }

    const roleIdentifiers = extractRoleIdentifiers(user.value)
    return roleIdentifiers.some((identifier) => ADMIN_ROLE_IDENTIFIERS.has(identifier))
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
        user.value = normalizeUser(JSON.parse(storedUser))
        if (user.value) {
          localStorage.setItem('user', JSON.stringify(user.value))
        }
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
        user.value = normalizeUser(response.user)
        localStorage.setItem('accessToken', response.accessToken)
        localStorage.setItem('user', JSON.stringify(user.value))
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
