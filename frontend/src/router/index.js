import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Auth/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Auth/Register.vue')
  },
  // Student routes
  {
    path: '/student/dashboard',
    name: 'StudentDashboard',
    component: () => import('../views/Student/Dashboard.vue')
  },
  {
    path: '/student/error-book',
    name: 'StudentErrorBook',
    component: () => import('../views/Student/ErrorBook.vue')
  },
  // Teacher routes
  {
    path: '/teacher/dashboard',
    name: 'TeacherDashboard',
    component: () => import('../views/Teacher/Dashboard.vue')
  },
  {
    path: '/teacher/questions',
    name: 'TeacherQuestions',
    component: () => import('../views/Teacher/Questions.vue')
  },
  {
    path: '/teacher/questions/create',
    name: 'CreateQuestion',
    component: () => import('../views/Teacher/Questions.vue')
  },
  // Admin routes
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('../views/Admin/Dashboard.vue')
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('../views/Admin/Users.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router