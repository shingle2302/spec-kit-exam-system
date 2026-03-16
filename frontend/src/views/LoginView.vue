<template>
  <div class="login-container">









    <div class="login-background">
      <div class="background-shape shape-1"></div>
      <div class="background-shape shape-2"></div>
      <div class="background-shape shape-3"></div>
    </div>

    <div class="login-content">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-section">
            <div class="logo-icon">
              <BookOutlined />
            </div>
            <h1 class="logo-title">考试系统</h1>
          </div>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
        </div>

        <a-form
          :model="formState"
          :rules="rules"
          @finish="handleLogin"
          layout="vertical"
          class="login-form"
        >
          <a-form-item name="identifier" label="账户">
            <a-input
              v-model:value="formState.identifier"
              placeholder="用户名 / 邮箱 / 手机号"
              size="large"
              class="modern-input"
            >
              <template #prefix>
                <UserOutlined class="input-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="formState.password"
              placeholder="请输入密码"
              size="large"
              class="modern-input"
            >
              <template #prefix>
                <LockOutlined class="input-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <div class="form-options">
            <a-checkbox v-model:checked="rememberMe">记住我</a-checkbox>
            <router-link to="/forgot-password" class="forgot-link">
              忘记密码？
            </router-link>
          </div>

          <a-form-item class="submit-button">
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              :loading="authStore.loading"
              block
              class="login-button"
            >
              <template #icon><LoginOutlined /></template>
              登录
            </a-button>
          </a-form-item>
        </a-form>

        <div class="login-footer">
          <span class="footer-text">还没有账户？</span>
          <router-link to="/register" class="register-link">
            立即注册
          </router-link>
        </div>

        <a-divider class="demo-divider">测试账户</a-divider>

        <div class="demo-info">
          <div class="demo-item">
            <span class="demo-label">用户名:</span>
            <code class="demo-value">admin</code>
            <a-button 
              type="text" 
              size="small" 
              @click="fillDemoAccount"
              class="copy-button"
            >
              <CopyOutlined />
            </a-button>
          </div>
          <div class="demo-item">
            <span class="demo-label">密码:</span>
            <code class="demo-value">Admin@123</code>
            <a-button 
              type="text" 
              size="small" 
              @click="fillDemoPassword"
              class="copy-button"
            >
              <CopyOutlined />
            </a-button>
          </div>
          <div class="demo-item">
            <span class="demo-label">H2控制台:</span>
            <a href="http://localhost:8080/h2-console" target="_blank" class="demo-link">
              http://localhost:8080/h2-console
              <ExportOutlined />
            </a>
          </div>
        </div>
      </div>

      <div class="login-features">
        <div class="feature-item">
          <SafetyOutlined class="feature-icon" />
          <div class="feature-text">
            <h4>安全可靠</h4>
            <p>企业级数据加密</p>
          </div>
        </div>
        <div class="feature-item">
          <ThunderboltOutlined class="feature-icon" />
          <div class="feature-text">
            <h4>快速高效</h4>
            <p>优化的系统性能</p>
          </div>
        </div>
        <div class="feature-item">
          <TeamOutlined class="feature-icon" />
          <div class="feature-text">
            <h4>易于使用</h4>
            <p>简洁直观的界面</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store'
import { 
  UserOutlined, 
  LockOutlined, 
  BookOutlined,
  LoginOutlined,
  CopyOutlined,
  ExportOutlined,
  SafetyOutlined,
  ThunderboltOutlined,
  TeamOutlined
} from '@ant-design/icons-vue'
import type { Rule } from 'ant-design-vue/es/form'
import { message } from 'ant-design-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const formState = reactive({
  identifier: '',
  password: ''
})

const rememberMe = ref(false)

const rules: Record<string, Rule[]> = {
  identifier: [
    { required: true, message: '请输入用户名/邮箱/手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

async function handleLogin() {
  const result = await authStore.login(formState.identifier, formState.password)
  if (result) {
    const redirect = route.query.redirect as string
    router.push(redirect || '/dashboard')
  }
}

function fillDemoAccount() {
  formState.identifier = 'admin'
  message.success('已填充用户名')
}

function fillDemoPassword() {
  formState.password = 'Admin@123'
  message.success('已填充密码')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 0;
}

.background-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.shape-2 {
  width: 200px;
  height: 200px;
  top: 50%;
  right: -50px;
  animation-delay: 2s;
}

.shape-3 {
  width: 250px;
  height: 250px;
  bottom: -80px;
  left: 30%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}

.login-content {
  display: flex;
  gap: 40px;
  align-items: center;
  z-index: 1;
  max-width: 1200px;
  width: 100%;
}

.login-card {
  flex: 1;
  max-width: 480px;
  padding: 48px;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 24px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  font-size: 24px;
}

.logo-title {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-subtitle {
  margin: 8px 0 0;
  color: #666;
  font-size: 15px;
}

.login-form {
  margin-bottom: 24px;
}

.modern-input :deep(.ant-input),
.modern-input :deep(.ant-input-password) {
  border-radius: 12px;
  border: 2px solid #e8e8e8;
  transition: all 0.3s ease;
  padding: 12px 16px;
}

.modern-input :deep(.ant-input:focus),
.modern-input :deep(.ant-input-password:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-icon {
  color: #999;
  font-size: 16px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.forgot-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.forgot-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

.submit-button {
  margin-bottom: 16px;
}

.login-button {
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(102, 126, 234, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
  margin-bottom: 24px;
  color: #666;
}

.footer-text {
  margin-right: 8px;
}

.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

.demo-divider {
  margin: 24px 0;
  color: #999;
}

.demo-info {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e9ecef;
}

.demo-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.demo-item:last-child {
  margin-bottom: 0;
}

.demo-label {
  color: #666;
  font-size: 14px;
  min-width: 80px;
}

.demo-value {
  flex: 1;
  padding: 6px 12px;
  background: white;
  border-radius: 6px;
  border: 1px solid #dee2e6;
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 13px;
  color: #495057;
}

.demo-link {
  flex: 1;
  color: #667eea;
  text-decoration: none;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.demo-link:hover {
  text-decoration: underline;
}

.copy-button {
  color: #667eea;
  padding: 4px;
}

.copy-button:hover {
  background: rgba(102, 126, 234, 0.1);
}

.login-features {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
  max-width: 400px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateX(8px);
}

.feature-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  color: #667eea;
  font-size: 24px;
}

.feature-text h4 {
  margin: 0 0 4px;
  color: white;
  font-size: 16px;
  font-weight: 600;
}

.feature-text p {
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
}

@media (max-width: 1024px) {
  .login-content {
    flex-direction: column;
    max-width: 500px;
  }

  .login-features {
    display: none;
  }
}

@media (max-width: 768px) {
  .login-container {
    padding: 16px;
  }

  .login-card {
    padding: 32px 24px;
  }

  .logo-title {
    font-size: 24px;
  }

  .background-shape {
    display: none;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px 16px;
  }

  .logo-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }

  .logo-title {
    font-size: 20px;
  }
}
</style>