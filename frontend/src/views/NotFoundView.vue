<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="error-icon">
        <ExclamationCircleOutlined />
      </div>
      <a-result
        status="404"
        title="404"
        sub-title="抱歉，您访问的页面不存在"
        class="error-result"
      >
        <template #extra>
          <a-button type="primary" @click="$router.push('/')" class="home-button">
            <template #icon>
              <HomeOutlined />
            </template>
            返回首页
          </a-button>
        </template>
      </a-result>
      <div class="suggestions">
        <h4>您可能想要：</h4>
        <a-space direction="vertical" style="width: 100%;">
          <a-button block @click="$router.push('/dashboard')">
            <template #icon>
              <DashboardOutlined />
            </template>
            前往控制台
          </a-button>
          <a-button block @click="$router.push('/login')">
            <template #icon>
              <LoginOutlined />
            </template>
            前往登录页面
          </a-button>
        </a-space>
      </div>
    </div>
  </div>
</template>

<script setup>
import { 
  ExclamationCircleOutlined, 
  HomeOutlined, 
  DashboardOutlined, 
  LoginOutlined 
} from '@ant-design/icons-vue';
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.not-found-content {
  background: white;
  border-radius: 16px;
  padding: 48px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  max-width: 600px;
  width: 100%;
  text-align: center;
}

.error-icon {
  font-size: 80px;
  color: #ff4d4f;
  margin-bottom: 24px;
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  75% { transform: translateX(10px); }
}

.error-result {
  margin-bottom: 32px;
}

.error-result :deep(.ant-result-title) {
  font-size: 72px;
  font-weight: 700;
  color: #1f2937;
}

.error-result :deep(.ant-result-subtitle) {
  font-size: 18px;
  color: #6b7280;
  margin-top: 16px;
}

.home-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  transition: all 0.3s ease;
}

.home-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.4);
}

.suggestions {
  margin-top: 32px;
  padding-top: 32px;
  border-top: 1px solid #e5e7eb;
}

.suggestions h4 {
  margin: 0 0 16px;
  font-size: 16px;
  font-weight: 600;
  color: #374151;
}

.suggestions :deep(.ant-btn) {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
  height: 40px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.suggestions :deep(.ant-btn:hover) {
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .not-found-content {
    padding: 32px 24px;
  }

  .error-icon {
    font-size: 60px;
  }

  .error-result :deep(.ant-result-title) {
    font-size: 56px;
  }

  .error-result :deep(.ant-result-subtitle) {
    font-size: 16px;
  }

  .home-button {
    height: 40px;
    font-size: 14px;
  }
}

@media (max-width: 576px) {
  .not-found-content {
    padding: 24px 16px;
  }

  .error-icon {
    font-size: 48px;
  }

  .error-result :deep(.ant-result-title) {
    font-size: 48px;
  }

  .suggestions h4 {
    font-size: 14px;
  }
}
</style>