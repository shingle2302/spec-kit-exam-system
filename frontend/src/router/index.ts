import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/store'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { requiresAuth: false, title: '注册' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/UserManagementView.vue'),
        meta: { title: '用户管理', requiresAdmin: true }
      },
      {
        path: 'roles',
        name: 'RoleManagement',
        component: () => import('@/views/RoleManagementView.vue'),
        meta: { title: '角色管理', requiresAdmin: true }
      },
      {
        path: 'menus',
        name: 'MenuManagement',
        component: () => import('@/views/MenuManagementView.vue'),
        meta: { title: '菜单管理', requiresAdmin: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/ProfileView.vue'),
        meta: { title: '个人中心' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundView.vue'),
    meta: { title: '页面未找到' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  authStore.initialize()

  // Set document title
  document.title = `${to.meta.title || '用户管理系统'} - Exam System`

  // Check if route requires authentication
  if (to.meta.requiresAuth !== false && !authStore.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // Check if route requires admin
  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    next({ name: 'Dashboard' })
    return
  }

  // Redirect to dashboard if already authenticated and trying to access login/register
  if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    next({ name: 'Dashboard' })
    return
  }

  next()
})

export default router
