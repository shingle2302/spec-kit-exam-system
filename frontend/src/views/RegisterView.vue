<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h1>用户管理系统</h1>
        <p>创建新账户</p>
      </div>

      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleRegister"
        layout="vertical"
      >
        <a-form-item name="username" label="用户名">
          <a-input
            v-model:value="formState.username"
            placeholder="3-50个字符，支持字母、数字、下划线"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="email" label="邮箱">
          <a-input
            v-model:value="formState.email"
            placeholder="请输入邮箱地址"
            size="large"
          >
            <template #prefix>
              <MailOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="phone" label="手机号">
          <a-input
            v-model:value="formState.phone"
            placeholder="请输入手机号码"
            size="large"
          >
            <template #prefix>
              <PhoneOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password" label="密码">
          <a-input-password
            v-model:value="formState.password"
            placeholder="至少8位，包含大小写字母、数字和特殊字符"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="confirmPassword" label="确认密码">
          <a-input-password
            v-model:value="formState.confirmPassword"
            placeholder="请再次输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            :loading="authStore.loading"
            block
          >
            注册
          </a-button>
        </a-form-item>
      </a-form>

      <div class="register-footer">
        已有账户？
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store'
import { UserOutlined, LockOutlined, MailOutlined, PhoneOutlined } from '@ant-design/icons-vue'
import type { Rule } from 'ant-design-vue/es/form'

const router = useRouter()
const authStore = useAuthStore()

const formState = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validatePassword = (_rule: Rule, value: string) => {
  if (!value) {
    return Promise.reject('请输入密码')
  }
  if (value.length < 8) {
    return Promise.reject('密码至少8位')
  }
  if (!/[A-Z]/.test(value)) {
    return Promise.reject('密码需包含大写字母')
  }
  if (!/[a-z]/.test(value)) {
    return Promise.reject('密码需包含小写字母')
  }
  if (!/[0-9]/.test(value)) {
    return Promise.reject('密码需包含数字')
  }
  if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
    return Promise.reject('密码需包含特殊字符')
  }
  return Promise.resolve()
}

const validateConfirmPassword = (_rule: Rule, value: string) => {
  if (!value) {
    return Promise.reject('请确认密码')
  }
  if (value !== formState.password) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const rules: Record<string, Rule[]> = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度为3-50个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '用户名只能包含字母、数字、下划线和横线', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^(\+?\d{1,15}|\d{10,15})$/, message: '请输入有效的手机号码', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

async function handleRegister() {
  const result = await authStore.register({
    username: formState.username,
    email: formState.email,
    phone: formState.phone,
    password: formState.password
  })
  if (result.success) {
    router.push('/login')
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.register-card {
  width: 100%;
  max-width: 450px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-header h1 {
  margin: 0;
  font-size: 28px;
  color: #1f1f1f;
}

.register-header p {
  margin: 8px 0 0;
  color: #666;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
}
</style>
