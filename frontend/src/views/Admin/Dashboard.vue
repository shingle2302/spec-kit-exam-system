<template>
  <div>
    <a-page-header
      title="Admin Dashboard"
      :sub-title="`Welcome, ${user?.firstName || user?.username || 'Admin'}!`"
      @back="() => $router.go(-1)"
    >
      <template #extra>
        <a-button @click="logout" type="primary" danger>Logout</a-button>
      </template>
    </a-page-header>
    
    <a-row :gutter="[16, 16]">
      <a-col :span="6">
        <a-card title="System Stats" :body-style="{ padding: '20px' }">
          <div style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #1890ff;">{{ stats.totalUsers }}</div>
            <div style="color: #8c8c8c;">Total Users</div>
          </div>
          <div style="text-align: center; margin-top: 15px;">
            <div style="font-size: 24px; font-weight: bold; color: #52c41a;">{{ stats.totalTeachers }}</div>
            <div style="color: #8c8c8c;">Teachers</div>
          </div>
          <div style="text-align: center; margin-top: 15px;">
            <div style="font-size: 24px; font-weight: bold; color: #fa8c16;">{{ stats.totalStudents }}</div>
            <div style="color: #8c8c8c;">Students</div>
          </div>
          <div style="text-align: center; margin-top: 15px;">
            <div style="font-size: 24px; font-weight: bold; color: #722ed1;">{{ stats.totalQuestions }}</div>
            <div style="color: #8c8c8c;">Questions</div>
          </div>
        </a-card>
      </a-col>
      
      <a-col :span="18">
        <a-card title="Quick Actions">
          <a-row :gutter="[16, 16]">
            <a-col :span="6">
              <a-card 
                hoverable 
                @click="goToManageUsers"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #1890ff; margin-bottom: 10px;">
                  <UserOutlined />
                </div>
                <div>Manage Users</div>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card 
                hoverable 
                @click="goToManageClasses"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #52c41a; margin-bottom: 10px;">
                  <TeamOutlined />
                </div>
                <div>Manage Classes</div>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card 
                hoverable 
                @click="goToSystemStats"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #fa8c16; margin-bottom: 10px;">
                  <BarChartOutlined />
                </div>
                <div>System Stats</div>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card 
                hoverable 
                @click="goToSystemConfig"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #722ed1; margin-bottom: 10px;">
                  <SettingOutlined />
                </div>
                <div>System Config</div>
              </a-card>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="[16, 16]" style="margin-top: 16px;">
      <a-col :span="12">
        <a-card title="Recent Users">
          <a-list
            item-layout="horizontal"
            :data-source="recentUsers"
            :loading="loadingUsers"
          >
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta
                  :description="`Role: ${item.role} | Created: ${new Date(item.createdAt).toLocaleDateString()}`"
                >
                  <template #title>
                    <a>{{ item.firstName }} {{ item.lastName }} ({{ item.username }})</a>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: getRoleColor(item.role) }">
                      {{ item.firstName?.charAt(0) || item.username?.charAt(0) || 'U' }}
                    </a-avatar>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="System Overview">
          <a-descriptions :column="1">
            <a-descriptions-item label="Server Status">
              <a-tag color="green">Online</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="Database">
              <a-tag color="blue">Connected</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="Active Users">
              <span>{{ stats.activeUsers }}</span>
            </a-descriptions-item>
            <a-descriptions-item label="System Uptime">
              <span>24 hours</span>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { 
  UserOutlined, 
  TeamOutlined, 
  BarChartOutlined, 
  SettingOutlined 
} from '@ant-design/icons-vue';
import adminService from '@/services/adminService';

export default {
  name: 'AdminDashboard',
  components: {
    UserOutlined,
    TeamOutlined,
    BarChartOutlined,
    SettingOutlined
  },
  setup() {
    const router = useRouter();
    const user = ref(null);
    const stats = ref({
      totalUsers: 0,
      totalTeachers: 0,
      totalStudents: 0,
      totalQuestions: 0,
      activeUsers: 0
    });
    const recentUsers = ref([]);
    const loadingUsers = ref(false);
    
    // Load user from localStorage
    onMounted(() => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
        loadDashboardData();
      } else {
        router.push('/login');
      }
    });
    
    const loadDashboardData = async () => {
      try {
        // Load system stats
        stats.value.totalUsers = 150;
        stats.value.totalTeachers = 15;
        stats.value.totalStudents = 130;
        stats.value.totalQuestions = 500;
        stats.value.activeUsers = 25;
        
        // Load recent users
        loadingUsers.value = true;
        // For now, we'll use mock data - in real implementation, we'd call adminService.getRecentUsers()
        recentUsers.value = [
          { id: '1', username: 'john_doe', firstName: 'John', lastName: 'Doe', role: 'STUDENT', createdAt: new Date().toISOString() },
          { id: '2', username: 'jane_smith', firstName: 'Jane', lastName: 'Smith', role: 'TEACHER', createdAt: new Date(Date.now() - 86400000).toISOString() },
          { id: '3', username: 'bob_wilson', firstName: 'Bob', lastName: 'Wilson', role: 'STUDENT', createdAt: new Date(Date.now() - 172800000).toISOString() },
          { id: '4', username: 'alice_brown', firstName: 'Alice', lastName: 'Brown', role: 'ADMIN', createdAt: new Date(Date.now() - 259200000).toISOString() }
        ];
        loadingUsers.value = false;
      } catch (error) {
        console.error('Error loading dashboard data:', error);
        message.error('Failed to load dashboard data');
      }
    };
    
    const getRoleColor = (role) => {
      const colors = {
        'STUDENT': '#1890ff',
        'TEACHER': '#52c41a',
        'ADMIN': '#722ed1'
      };
      return colors[role] || '#8c8c8c';
    };
    
    const goToManageUsers = () => {
      router.push('/admin/users');
    };
    
    const goToManageClasses = () => {
      router.push('/admin/classes');
    };
    
    const goToSystemStats = () => {
      router.push('/admin/stats');
    };
    
    const goToSystemConfig = () => {
      router.push('/admin/config');
    };
    
    const logout = () => {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete axios.defaults.headers.common['Authorization'];
      router.push('/login');
    };
    
    return {
      user,
      stats,
      recentUsers,
      loadingUsers,
      getRoleColor,
      goToManageUsers,
      goToManageClasses,
      goToSystemStats,
      goToSystemConfig,
      logout
    };
  }
};
</script>