<template>
  <div>
    <a-page-header
      title="Teacher Dashboard"
      :sub-title="`Welcome, ${user?.firstName || user?.username || 'Teacher'}!`"
      @back="() => $router.go(-1)"
    >
      <template #extra>
        <a-button @click="logout" type="primary" danger>Logout</a-button>
      </template>
    </a-page-header>
    
    <a-row :gutter="[16, 16]">
      <a-col :span="6">
        <a-card title="Quick Stats" :body-style="{ padding: '20px' }">
          <div style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #1890ff;">{{ stats.totalQuestions }}</div>
            <div style="color: #8c8c8c;">Total Questions</div>
          </div>
          <div style="text-align: center; margin-top: 15px;">
            <div style="font-size: 24px; font-weight: bold; color: #52c41a;">{{ stats.totalStudents }}</div>
            <div style="color: #8c8c8c;">Students</div>
          </div>
          <div style="text-align: center; margin-top: 15px;">
            <div style="font-size: 24px; font-weight: bold; color: #722ed1;">{{ stats.totalTests }}</div>
            <div style="color: #8c8c8c;">Created Tests</div>
          </div>
        </a-card>
      </a-col>
      
      <a-col :span="18">
        <a-card title="Quick Actions">
          <a-row :gutter="[16, 16]">
            <a-col :span="8">
              <a-card 
                hoverable 
                @click="goToCreateQuestion"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #1890ff; margin-bottom: 10px;">
                  <FileAddOutlined />
                </div>
                <div>Create Question</div>
              </a-card>
            </a-col>
            <a-col :span="8">
              <a-card 
                hoverable 
                @click="goToCreateTest"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #52c41a; margin-bottom: 10px;">
                  <EditOutlined />
                </div>
                <div>Create Test</div>
              </a-card>
            </a-col>
            <a-col :span="8">
              <a-card 
                hoverable 
                @click="goToManageStudents"
                style="cursor: pointer; text-align: center;"
              >
                <div style="font-size: 24px; color: #fa8c16; margin-bottom: 10px;">
                  <TeamOutlined />
                </div>
                <div>Manage Students</div>
              </a-card>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="[16, 16]" style="margin-top: 16px;">
      <a-col :span="12">
        <a-card title="Recent Questions">
          <a-list
            item-layout="horizontal"
            :data-source="recentQuestions"
            :loading="loadingQuestions"
          >
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta
                  :description="item.questionText.substring(0, 50) + (item.questionText.length > 50 ? '...' : '')"
                >
                  <template #title>
                    <a>{{ item.title || `Question ${item.id}` }}</a>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: '#1890ff' }">
                      {{ item.questionType.charAt(0) }}
                    </a-avatar>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="Recent Tests">
          <a-list
            item-layout="horizontal"
            :data-source="recentTests"
            :loading="loadingTests"
          >
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta
                  :description="item.description.substring(0, 50) + (item.description.length > 50 ? '...' : '')"
                >
                  <template #title>
                    <a>{{ item.title }}</a>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: '#52c41a' }">
                      T
                    </a-avatar>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
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
  FileAddOutlined, 
  EditOutlined, 
  TeamOutlined 
} from '@ant-design/icons-vue';
import questionService from '@/services/questionService';
import testService from '@/services/testService';

export default {
  name: 'TeacherDashboard',
  components: {
    FileAddOutlined,
    EditOutlined,
    TeamOutlined
  },
  setup() {
    const router = useRouter();
    const user = ref(null);
    const stats = ref({
      totalQuestions: 0,
      totalStudents: 0,
      totalTests: 0
    });
    const recentQuestions = ref([]);
    const recentTests = ref([]);
    const loadingQuestions = ref(false);
    const loadingTests = ref(false);
    
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
        // Load recent questions created by this teacher
        loadingQuestions.value = true;
        const questions = await questionService.getQuestions({ createdBy: user.value?.id });
        recentQuestions.value = questions.slice(0, 5);
        stats.value.totalQuestions = questions.length;
        loadingQuestions.value = false;
        
        // Load recent tests created by this teacher
        loadingTests.value = true;
        // Note: We would need to implement getTestsByTeacher in the backend service
        // For now, we'll use a mock implementation
        recentTests.value = [
          { id: '1', title: 'Math Quiz 1', description: 'Basic algebra questions' },
          { id: '2', title: 'Science Test', description: 'Physics concepts' }
        ];
        stats.value.totalTests = recentTests.value.length;
        loadingTests.value = false;
        
        // Mock student count
        stats.value.totalStudents = 25;
      } catch (error) {
        console.error('Error loading dashboard data:', error);
        message.error('Failed to load dashboard data');
      }
    };
    
    const goToCreateQuestion = () => {
      router.push('/teacher/questions/create');
    };
    
    const goToCreateTest = () => {
      router.push('/teacher/tests/create');
    };
    
    const goToManageStudents = () => {
      router.push('/teacher/students');
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
      recentQuestions,
      recentTests,
      loadingQuestions,
      loadingTests,
      goToCreateQuestion,
      goToCreateTest,
      goToManageStudents,
      logout
    };
  }
};
</script>