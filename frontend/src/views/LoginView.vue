<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>用户管理系统</h1>
        <p>登录您的账户</p>
      </div>

      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleLogin"
        layout="vertical"
      >
        <a-form-item name="identifier" label="账户">
          <a-input
            v-model:value="formState.identifier"
            placeholder="用户名 / 邮箱 / 手机号"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password" label="密码">
          <a-input-password
            v-model:value="formState.password"
            placeholder="请输入密码"
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
            登录
          </a-button>
        </a-form-item>
      </a-form>

      <div class="login-footer">
        还没有账户？
        <router-link to="/register">立即注册</router-link>
      </div>

      <div class="demo-info">
        <a-divider>默认管理员账户</a-divider>
        <p>用户名: <code>admin</code></p>
        <p>密码: <code>Admin@123</code></p>
        <p>H2 控制台: <code>http://localhost:8080/h2-console</code></p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import type { Rule } from 'ant-design-vue/es/form'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const formState = reactive({
  identifier: '',
  password: ''
})

const rules: Record<string, Rule[]> = {
  identifier: [
    { required: true, message: '请输入用户名/邮箱/手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

async function handleLogin() {
  const result = await authStore.login(formState)
  if (result.success) {
    const redirect = route.query.redirect as string
    router.push(redirect || '/dashboard')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h1 {
  margin: 0;
  font-size: 28px;
  color: #1f1f1f;
}

.login-header p {
  margin: 8px 0 0;
  color: #666;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
}

.demo-info {
  margin-top: 24px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
  font-size: 13px;
}

.demo-info p {
  margin: 4px 0;
  color: #666;
}

.demo-info code {
  padding: 2px 6px;
  background: #e6e6e6;
  border-radius: 4px;
  font-family: monospace;
}
</style>
