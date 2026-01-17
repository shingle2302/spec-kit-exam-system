<template>
  <div class="profile">
    <a-page-header title="个人中心" sub-title="管理您的账户信息" />

    <a-row :gutter="24">
      <a-col :xs="24" :lg="12">
        <a-card title="基本信息">
          <a-descriptions :column="1" bordered>
            <a-descriptions-item label="用户名">
              {{ authStore.currentUser?.username }}
            </a-descriptions-item>
            <a-descriptions-item label="邮箱">
              {{ authStore.currentUser?.email }}
            </a-descriptions-item>
            <a-descriptions-item label="手机">
              {{ authStore.currentUser?.phone || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="状态">
              <a-tag :color="getStatusColor(authStore.currentUser?.status)">
                {{ getStatusText(authStore.currentUser?.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="角色">
              <a-tag v-if="authStore.isAdmin" color="gold">超级管理员</a-tag>
              <a-tag v-else color="blue">普通用户</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="注册时间">
              {{ formatDate(authStore.currentUser?.createdAt) }}
            </a-descriptions-item>
            <a-descriptions-item label="最后登录">
              {{ formatDate(authStore.currentUser?.lastLoginAt) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <a-col :xs="24" :lg="12">
        <a-card title="安全设置" style="margin-bottom: 24px;">
          <a-space direction="vertical" style="width: 100%;">
            <a-button block @click="showPasswordModal = true">
              <template #icon><LockOutlined /></template>
              修改密码
            </a-button>
          </a-space>
        </a-card>

        <a-card title="账户操作">
          <a-space direction="vertical" style="width: 100%;">
            <a-button block danger @click="handleLogout">
              <template #icon><LogoutOutlined /></template>
              退出登录
            </a-button>
          </a-space>
        </a-card>
      </a-col>
    </a-row>

    <!-- Change Password Modal -->
    <a-modal
      v-model:open="showPasswordModal"
      title="修改密码"
      @ok="handleChangePassword"
      :confirm-loading="loading"
    >
      <a-form :model="passwordForm" layout="vertical">
        <a-form-item label="当前密码" required>
          <a-input-password v-model:value="passwordForm.currentPassword" placeholder="请输入当前密码" />
        </a-form-item>
        <a-form-item label="新密码" required>
          <a-input-password v-model:value="passwordForm.newPassword" placeholder="请输入新密码" />
        </a-form-item>
        <a-form-item label="确认新密码" required>
          <a-input-password v-model:value="passwordForm.confirmPassword" placeholder="请再次输入新密码" />
        </a-form-item>
      </a-form>
      <a-alert type="info" show-icon style="margin-top: 16px;">
        <template #message>
          密码要求：至少8位，包含大小写字母、数字和特殊字符
        </template>
      </a-alert>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store'
import { LockOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

const router = useRouter()
const authStore = useAuthStore()

const showPasswordModal = ref(false)
const loading = ref(false)

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

function getStatusColor(status?: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'green',
    'INACTIVE': 'default',
    'SUSPENDED': 'orange',
    'LOCKED': 'red'
  }
  return colors[status || ''] || 'default'
}

function getStatusText(status?: string) {
  const texts: Record<string, string> = {
    'ACTIVE': '活跃',
    'INACTIVE': '未激活',
    'SUSPENDED': '暂停',
    'LOCKED': '锁定'
  }
  return texts[status || ''] || status
}

function formatDate(dateStr?: string | null) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function handleChangePassword() {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    message.error('两次输入的密码不一致')
    return
  }
  // TODO: Implement password change API
  message.info('密码修改功能即将上线')
  showPasswordModal.value = false
}

async function handleLogout() {
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.profile {
  padding: 0;
}
</style>
