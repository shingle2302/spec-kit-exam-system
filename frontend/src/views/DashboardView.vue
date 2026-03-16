<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <div class="header-content">
        <div class="welcome-section">
          <h1 class="welcome-title">
            <span class="greeting">{{ greeting }}</span>
            <span class="username">{{ authStore.currentUser?.username }}</span>
          </h1>
          <p class="welcome-subtitle">{{ currentDate }}</p>
        </div>
        <div class="header-actions">
          <a-button type="primary" @click="refreshData" :loading="loading">
            <template #icon><ReloadOutlined /></template>
            刷新数据
          </a-button>
        </div>
      </div>
    </div>

    <div class="dashboard-content">
      <!-- Statistics Cards -->
      <div class="stats-grid">
        <div class="stat-card stat-primary">
          <div class="stat-icon">
            <UserOutlined />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalUsers }}</div>
            <div class="stat-label">用户总数</div>
            <div class="stat-trend positive">
              <TrendingUpOutlined />
              <span>+12%</span>
            </div>
          </div>
        </div>

        <div class="stat-card stat-success">
          <div class="stat-icon">
            <CheckCircleOutlined />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.activeUsers }}</div>
            <div class="stat-label">活跃用户</div>
            <div class="stat-trend positive">
              <TrendingUpOutlined />
              <span>+8%</span>
            </div>
          </div>
        </div>

        <div class="stat-card stat-warning">
          <div class="stat-icon">
            <LockOutlined />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.lockedUsers }}</div>
            <div class="stat-label">锁定用户</div>
            <div class="stat-trend negative">
              <TrendingDownOutlined />
              <span>-3%</span>
            </div>
          </div>
        </div>

        <div class="stat-card stat-info">
          <div class="stat-icon">
            <TeamOutlined />
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalRoles }}</div>
            <div class="stat-label">角色数量</div>
            <div class="stat-trend positive">
              <TrendingUpOutlined />
              <span>+5%</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Main Content Grid -->
      <div class="main-grid">
        <!-- User Info Card -->
        <div class="info-card user-info-card">
          <div class="card-header">
            <div class="card-title">
              <UserOutlined />
              <span>当前用户信息</span>
            </div>
            <a-button type="text" @click="$router.push('/profile')">
              <template #icon><EditOutlined /></template>
              编辑
            </a-button>
          </div>
          <div class="card-content">
            <div class="user-profile">
              <div class="user-avatar">
                <UserOutlined />
              </div>
              <div class="user-details">
                <div class="user-name">{{ authStore.currentUser?.username }}</div>
                <div class="user-email">{{ authStore.currentUser?.email }}</div>
              </div>
            </div>
            <a-divider />
            <div class="info-list">
              <div class="info-item">
                <span class="info-label">手机号</span>
                <span class="info-value">{{ authStore.currentUser?.phone || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">状态</span>
                <a-tag :color="getStatusColor(authStore.currentUser?.status)" class="info-tag">
                  {{ getStatusText(authStore.currentUser?.status) }}
                </a-tag>
              </div>
              <div class="info-item">
                <span class="info-label">角色</span>
                <a-tag v-if="authStore.isAdmin" color="gold" class="info-tag">
                  <CrownOutlined />
                  超级管理员
                </a-tag>
                <a-tag v-else color="blue" class="info-tag">
                  普通用户
                </a-tag>
              </div>
              <div class="info-item">
                <span class="info-label">最后登录</span>
                <span class="info-value">{{ formatDate(authStore.currentUser?.lastLoginAt) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Quick Actions Card -->
        <div class="info-card quick-actions-card">
          <div class="card-header">
            <div class="card-title">
              <ThunderboltOutlined />
              <span>快速操作</span>
            </div>
          </div>
          <div class="card-content">
            <div class="quick-actions-grid">
              <div 
                v-if="authStore.isAdmin" 
                class="quick-action-item"
                @click="$router.push('/users')"
              >
                <div class="action-icon action-blue">
                  <UserOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">管理用户</div>
                  <div class="action-desc">管理系统用户</div>
                </div>
              </div>

              <div 
                v-if="authStore.isAdmin" 
                class="quick-action-item"
                @click="$router.push('/roles')"
              >
                <div class="action-icon action-purple">
                  <TeamOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">管理角色</div>
                  <div class="action-desc">管理系统角色</div>
                </div>
              </div>

              <div class="quick-action-item" @click="$router.push('/profile')">
                <div class="action-icon action-green">
                  <SettingOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">个人设置</div>
                  <div class="action-desc">修改个人信息</div>
                </div>
              </div>

              <div class="quick-action-item" @click="$router.push('/exam-papers')">
                <div class="action-icon action-orange">
                  <FileTextOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">试卷管理</div>
                  <div class="action-desc">管理考试试卷</div>
                </div>
              </div>

              <div class="quick-action-item" @click="$router.push('/exam-plans')">
                <div class="action-icon action-pink">
                  <CalendarOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">考试安排</div>
                  <div class="action-desc">安排考试计划</div>
                </div>
              </div>

              <div class="quick-action-item" @click="$router.push('/questions')">
                <div class="action-icon action-cyan">
                  <QuestionCircleOutlined />
                </div>
                <div class="action-text">
                  <div class="action-title">题库管理</div>
                  <div class="action-desc">管理题目库</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Recent Activity Card -->
        <div class="info-card activity-card">
          <div class="card-header">
            <div class="card-title">
              <ClockCircleOutlined />
              <span>最近活动</span>
            </div>
            <a-button type="text" size="small">
              查看全部
              <RightOutlined />
            </a-button>
          </div>
          <div class="card-content">
            <div class="activity-list">
              <div v-for="(activity, index) in recentActivities" :key="index" class="activity-item">
                <div class="activity-icon" :class="activity.type">
                  <component :is="activity.icon" />
                </div>
                <div class="activity-content">
                  <div class="activity-title">{{ activity.title }}</div>
                  <div class="activity-time">{{ activity.time }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- System Status Card -->
        <div class="info-card status-card">
          <div class="card-header">
            <div class="card-title">
              <MonitorOutlined />
              <span>系统状态</span>
            </div>
            <a-tag color="success" class="status-badge">
              <CheckCircleOutlined />
              正常运行
            </a-tag>
          </div>
          <div class="card-content">
            <div class="status-list">
              <div class="status-item">
                <div class="status-label">数据库</div>
                <div class="status-indicator status-success">
                  <div class="indicator-dot"></div>
                  <span>正常</span>
                </div>
              </div>
              <div class="status-item">
                <div class="status-label">缓存服务</div>
                <div class="status-indicator status-success">
                  <div class="indicator-dot"></div>
                  <span>正常</span>
                </div>
              </div>
              <div class="status-item">
                <div class="status-label">API服务</div>
                <div class="status-indicator status-success">
                  <div class="indicator-dot"></div>
                  <span>正常</span>
                </div>
              </div>
              <div class="status-item">
                <div class="status-label">存储空间</div>
                <div class="status-indicator status-warning">
                  <div class="indicator-dot"></div>
                  <span>75%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAuthStore, useUserStore, useRoleStore } from '@/store'
import {
  UserOutlined,
  CheckCircleOutlined,
  LockOutlined,
  TeamOutlined,
  ReloadOutlined,
  EditOutlined,
  ThunderboltOutlined,
  SettingOutlined,
  FileTextOutlined,
  CalendarOutlined,
  QuestionCircleOutlined,
  ClockCircleOutlined,
  RightOutlined,
  MonitorOutlined,
  CrownOutlined,
  TrendingUpOutlined,
  TrendingDownOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

const authStore = useAuthStore()
const userStore = useUserStore()
const roleStore = useRoleStore()

const loading = ref(false)
const stats = ref({
  totalUsers: 0,
  activeUsers: 0,
  lockedUsers: 0,
  totalRoles: 0
})

const recentActivities = ref([
  {
    icon: UserOutlined,
    title: '用户张三登录系统',
    time: '5分钟前',
    type: 'login'
  },
  {
    icon: FileTextOutlined,
    title: '创建了新试卷"数学期末考试"',
    time: '1小时前',
    type: 'create'
  },
  {
    icon: TeamOutlined,
    title: '更新了角色权限配置',
    time: '2小时前',
    type: 'update'
  },
  {
    icon: LockOutlined,
    title: '用户李四被锁定',
    time: '3小时前',
    type: 'lock'
  }
])

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '早上好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const currentDate = computed(() => {
  const now = new Date()
  const options: Intl.DateTimeFormatOptions = { 
    weekday: 'long', 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  }
  return now.toLocaleDateString('zh-CN', options)
})

onMounted(async () => {
  await loadData()
})

async function loadData() {
  loading.value = true
  try {
    if (authStore.isAdmin) {
      await Promise.all([
        userStore.fetchUsers(),
        roleStore.fetchRoles()
      ])
      
      stats.value.totalUsers = userStore.users.length
      stats.value.activeUsers = userStore.users.filter(u => u.status === 'ACTIVE').length
      stats.value.lockedUsers = userStore.users.filter(u => u.status === 'LOCKED').length
      stats.value.totalRoles = roleStore.roles.length
    }
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

async function refreshData() {
  await loadData()
  message.success('数据已刷新')
}

function getStatusColor(status?: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'success',
    'INACTIVE': 'default',
    'SUSPENDED': 'warning',
    'LOCKED': 'error'
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
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 24px;
}

.dashboard-header {
  margin-bottom: 32px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-section {
  flex: 1;
}

.welcome-title {
  margin: 0 0 8px;
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
}

.greeting {
  color: #667eea;
  margin-right: 8px;
}

.username {
  color: #1a1a1a;
}

.welcome-subtitle {
  margin: 0;
  color: #666;
  font-size: 15px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.dashboard-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.stat-primary .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-success .stat-icon {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.stat-warning .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-info .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
}

.stat-trend.positive {
  color: #52c41a;
}

.stat-trend.negative {
  color: #ff4d4f;
}

.main-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 24px;
}

.info-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.card-title :deep(.anticon) {
  color: #667eea;
  font-size: 18px;
}

.card-content {
  padding: 24px;
}

.user-info-card {
  grid-column: span 1;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.user-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.user-email {
  font-size: 14px;
  color: #666;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 14px;
  color: #1a1a1a;
  font-weight: 500;
}

.info-tag {
  font-weight: 500;
}

.quick-actions-card {
  grid-column: span 1;
}

.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.quick-action-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-radius: 12px;
  background: #f8f9fa;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
}

.quick-action-item:hover {
  background: white;
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: white;
}

.action-blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-purple {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
}

.action-green {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.action-orange {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.action-pink {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.action-cyan {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.action-text {
  flex: 1;
}

.action-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.action-desc {
  font-size: 12px;
  color: #666;
}

.activity-card {
  grid-column: span 1;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.activity-item:hover {
  background: #f0f0f0;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.activity-icon.login {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.activity-icon.create {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.activity-icon.update {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.activity-icon.lock {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.activity-time {
  font-size: 12px;
  color: #999;
}

.status-card {
  grid-column: span 1;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 500;
}

.status-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-label {
  font-size: 14px;
  color: #666;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 500;
}

.status-indicator.status-success {
  color: #52c41a;
}

.status-indicator.status-warning {
  color: #faad14;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

@media (max-width: 1200px) {
  .main-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: 16px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .welcome-title {
    font-size: 24px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .quick-actions-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .dashboard {
    padding: 12px;
  }

  .stat-card {
    padding: 16px;
  }

  .stat-icon {
    width: 48px;
    height: 48px;
    font-size: 22px;
  }

  .stat-value {
    font-size: 24px;
  }

  .welcome-title {
    font-size: 20px;
  }
}
</style>