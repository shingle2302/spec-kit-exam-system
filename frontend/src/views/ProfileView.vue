<template>
  <div class="profile">
    <a-page-header class="page-header" title="个人中心" sub-title="管理您的账户信息" />

    <div class="content-wrapper">
      <a-row :gutter="24">
        <a-col :xs="24" :lg="12">
          <a-card class="info-card" title="基本信息">
            <template #title>
              <div class="card-title">
                <UserOutlined class="title-icon" />
                <span>基本信息</span>
              </div>
            </template>
            <a-descriptions :column="1" bordered class="info-descriptions">
              <a-descriptions-item label="用户名">
                <div class="info-value">
                  <UserOutlined class="info-icon" />
                  {{ authStore.currentUser?.username }}
                </div>
              </a-descriptions-item>
              <a-descriptions-item label="邮箱">
                <div class="info-value">
                  <MailOutlined class="info-icon" />
                  {{ authStore.currentUser?.email }}
                </div>
              </a-descriptions-item>
              <a-descriptions-item label="手机">
                <div class="info-value">
                  <PhoneOutlined class="info-icon" />
                  {{ authStore.currentUser?.phone || '-' }}
                </div>
              </a-descriptions-item>
              <a-descriptions-item label="状态">
                <a-tag :color="getStatusColor(authStore.currentUser?.status)" class="status-tag">
                  {{ getStatusText(authStore.currentUser?.status) }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="角色">
                <a-tag v-if="authStore.isAdmin" color="gold" class="role-tag">
                  <CrownOutlined class="role-icon" />
                  超级管理员
                </a-tag>
                <a-tag v-else color="blue" class="role-tag">
                  <UserOutlined class="role-icon" />
                  普通用户
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="注册时间">
                <div class="info-value">
                  <ClockCircleOutlined class="info-icon" />
                  {{ formatDate(authStore.currentUser?.createdAt) }}
                </div>
              </a-descriptions-item>
              <a-descriptions-item label="最后登录">
                <div class="info-value">
                  <LoginOutlined class="info-icon" />
                  {{ formatDate(authStore.currentUser?.lastLoginAt) }}
                </div>
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>

        <a-col :xs="24" :lg="12">
          <a-card class="security-card" title="安全设置">
            <template #title>
              <div class="card-title">
                <SafetyOutlined class="title-icon" />
                <span>安全设置</span>
              </div>
            </template>
            <a-space direction="vertical" style="width: 100%;" class="action-buttons">
              <a-button block @click="showPasswordModal = true" class="security-button">
                <template #icon><LockOutlined /></template>
                修改密码
              </a-button>
            </a-space>
          </a-card>

          <a-card class="account-card" title="账户操作">
            <template #title>
              <div class="card-title">
                <SettingOutlined class="title-icon" />
                <span>账户操作</span>
              </div>
            </template>
            <a-space direction="vertical" style="width: 100%;" class="action-buttons">
              <a-button block danger @click="handleLogout" class="logout-button">
                <template #icon><LogoutOutlined /></template>
                退出登录
              </a-button>
            </a-space>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <a-modal
      v-model:open="showPasswordModal"
      title="修改密码"
      @ok="handleChangePassword"
      :confirm-loading="loading"
      class="form-modal"
      width="500px"
    >
      <a-form :model="passwordForm" layout="vertical">
        <a-form-item label="当前密码" required>
          <a-input-password v-model:value="passwordForm.currentPassword" placeholder="请输入当前密码" class="form-input">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item label="新密码" required>
          <a-input-password v-model:value="passwordForm.newPassword" placeholder="请输入新密码" class="form-input">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item label="确认新密码" required>
          <a-input-password v-model:value="passwordForm.confirmPassword" placeholder="请再次输入新密码" class="form-input">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
      </a-form>
      <a-alert type="info" show-icon class="password-alert">
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
import { 
  LockOutlined, 
  LogoutOutlined, 
  UserOutlined, 
  MailOutlined, 
  PhoneOutlined, 
  ClockCircleOutlined, 
  LoginOutlined,
  SafetyOutlined,
  SettingOutlined,
  CrownOutlined
} from '@ant-design/icons-vue'
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
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 20px 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.content-wrapper {
  padding: 0 24px 24px;
}

.info-card,
.security-card,
.account-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
  margin-bottom: 24px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #1f2937;
}

.title-icon {
  font-size: 18px;
  color: #1890ff;
}

.info-descriptions {
  border-radius: 6px;
  overflow: hidden;
}

.info-value {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-icon {
  color: #6b7280;
  font-size: 14px;
}

.status-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
}

.role-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.role-icon {
  font-size: 14px;
}

.action-buttons {
  gap: 12px;
}

.security-button,
.logout-button {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 44px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.security-button {
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
}

.security-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.logout-button {
  box-shadow: 0 2px 4px rgba(255, 77, 79, 0.2);
}

.logout-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 77, 79, 0.3);
}

.form-modal :deep(.ant-modal-header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px 8px 0 0;
  padding: 16px 24px;
}

.form-modal :deep(.ant-modal-title) {
  color: white;
  font-weight: 600;
}

.form-modal :deep(.ant-modal-close) {
  color: white;
}

.form-modal :deep(.ant-modal-close:hover) {
  color: rgba(255, 255, 255, 0.8);
}

.form-modal :deep(.ant-modal-body) {
  padding: 24px;
}

.form-input {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.password-alert {
  margin-top: 16px;
  border-radius: 6px;
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 0 12px 12px;
  }

  .info-card,
  .security-card,
  .account-card {
    border-radius: 6px;
    margin-bottom: 16px;
  }

  .security-button,
  .logout-button {
    height: 40px;
    font-size: 14px;
  }
}

@media (max-width: 576px) {
  .page-header {
    padding: 16px;
  }

  .info-value {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>