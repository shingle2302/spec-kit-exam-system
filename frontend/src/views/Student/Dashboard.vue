<template>
  <div>
    <a-page-header
      title="Student Dashboard"
      :sub-title="`Welcome, ${user?.firstName || user?.username || 'Student'}!`"
      @back="() => $router.go(-1)"
    >
      <template #extra>
        <a-button @click="logout" type="primary" danger>Logout</a-button>
      </template>
    </a-page-header>
    
    <a-row :gutter="[16, 16]">
      <a-col :span="24">
        <a-card title="My Exams" :loading="loadingExams">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
            <a-button type="primary" @click="fetchExams">
              <template #icon><ReloadOutlined /></template>
              Refresh
            </a-button>
            <a-input-search
              v-model:value="searchText"
              placeholder="Search exams..."
              style="width: 300px;"
              @search="handleSearch"
              @change="handleSearch"
            />
          </div>
          
          <a-table 
            :columns="examColumns" 
            :data-source="exams" 
            :pagination="{ pageSize: 5 }"
            row-key="id"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'status'">
                <a-tag :color="getExamStatusColor(record)">
                  {{ getExamStatus(record) }}
                </a-tag>
              </template>
              <template v-if="column.dataIndex === 'action'">
                <a-button 
                  type="primary" 
                  size="small" 
                  :disabled="!isExamAvailable(record)"
                  @click="takeExam(record)"
                >
                  {{ getExamActionText(record) }}
                </a-button>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- Exam Taking Modal -->
    <a-modal
      v-model:visible="examModalVisible"
      :title="currentExam?.title"
      :footer="null"
      :width="800"
      :destroy-on-close="true"
      @cancel="cancelExam"
    >
      <div v-if="currentExam && !examSubmitted">
        <div style="margin-bottom: 20px;">
          <h3>{{ currentExam.description }}</h3>
          <p v-if="currentExam.timeLimitMinutes">Time Limit: {{ currentExam.timeLimitMinutes }} minutes</p>
        </div>
        
        <a-form :model="examAnswers" @finish="submitExam">
          <div v-for="(question, index) in currentExam.questions" :key="question.id" style="margin-bottom: 30px;">
            <h4>{{ index + 1 }}. {{ question.questionText }}</h4>
            
            <!-- Multiple Choice / Single Choice -->
            <a-radio-group 
              v-if="question.questionType === 'MULTIPLE_CHOICE' || question.questionType === 'SINGLE_CHOICE'" 
              v-model:value="examAnswers[question.id]"
              name="question"
            >
              <a-space direction="vertical">
                <a-radio 
                  v-for="(option, optIndex) in question.answerOptions" 
                  :key="optIndex" 
                  :value="optIndex"
                >
                  {{ option.optionText }}
                </a-radio>
              </a-space>
            </a-radio-group>
            
            <!-- True/False -->
            <a-radio-group 
              v-else-if="question.questionType === 'TRUE_FALSE'" 
              v-model:value="examAnswers[question.id]"
            >
              <a-radio :value="0">True</a-radio>
              <a-radio :value="1">False</a-radio>
            </a-radio-group>
            
            <!-- Short Answer / Essay -->
            <a-textarea 
              v-else-if="question.questionType === 'SHORT_ANSWER' || question.questionType === 'ESSAY'" 
              v-model:value="examAnswers[question.id]"
              :rows="4"
              placeholder="Enter your answer here..."
            />
          </div>
          
          <a-form-item style="text-align: center; margin-top: 30px;">
            <a-button type="primary" html-type="submit" :loading="submittingExam">
              Submit Exam
            </a-button>
            <a-button @click="cancelExam" style="margin-left: 10px;">
              Cancel
            </a-button>
          </a-form-item>
        </a-form>
      </div>
      
      <div v-else-if="examSubmitted">
        <a-result
          status="success"
          title="Exam Submitted Successfully!"
          sub-title="Your exam has been submitted and will be graded soon."
        >
          <template #extra>
            <a-button type="primary" @click="examModalVisible = false">
              Close
            </a-button>
            <a-button @click="viewErrorBook">
              View Error Book
            </a-button>
          </template>
        </a-result>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { ReloadOutlined } from '@ant-design/icons-vue';
import testService from '@/services/testService';

export default {
  name: 'StudentDashboard',
  components: {
    ReloadOutlined
  },
  setup() {
    const router = useRouter();
    const loadingExams = ref(false);
    const exams = ref([]);
    const searchText = ref('');
    const examModalVisible = ref(false);
    const currentExam = ref(null);
    const examAnswers = reactive({});
    const examSubmitted = ref(false);
    const submittingExam = ref(false);
    const user = ref(null);
    
    // Load user from localStorage
    onMounted(() => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
        fetchExams();
      } else {
        router.push('/login');
      }
    });
    
    const examColumns = [
      {
        title: 'Title',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: 'Description',
        dataIndex: 'description',
        key: 'description',
      },
      {
        title: 'Due Date',
        dataIndex: 'dueDate',
        key: 'dueDate',
        customRender: ({ text }) => new Date(text).toLocaleDateString()
      },
      {
        title: 'Status',
        dataIndex: 'status',
        key: 'status',
      },
      {
        title: 'Action',
        key: 'action',
        width: '150px'
      },
    ];
    
    const fetchExams = async () => {
      loadingExams.value = true;
      try {
        const userId = user.value?.id;
        if (userId) {
          const data = await testService.getTestsForStudent(userId);
          exams.value = data;
        }
      } catch (error) {
        console.error('Error fetching exams:', error);
        message.error('Failed to fetch exams');
      } finally {
        loadingExams.value = false;
      }
    };
    
    const getExamStatus = (exam) => {
      const now = new Date();
      const dueDate = new Date(exam.dueDate);
      
      if (exam.isGraded) return 'Graded';
      if (now > dueDate) return 'Overdue';
      if (exam.isActive) return 'Available';
      return 'Inactive';
    };
    
    const getExamStatusColor = (exam) => {
      const status = getExamStatus(exam);
      switch(status) {
        case 'Graded': return 'green';
        case 'Available': return 'blue';
        case 'Overdue': return 'red';
        default: return 'gray';
      }
    };
    
    const isExamAvailable = (exam) => {
      const status = getExamStatus(exam);
      return status === 'Available' && !exam.isGraded;
    };
    
    const getExamActionText = (exam) => {
      if (exam.isGraded) return 'Review';
      return 'Take Exam';
    };
    
    const takeExam = (exam) => {
      currentExam.value = exam;
      examModalVisible.value = true;
      examSubmitted.value = false;
      
      // Initialize answers object
      exam.questions.forEach(question => {
        examAnswers[question.id] = null;
      });
    };
    
    const submitExam = async () => {
      submittingExam.value = true;
      try {
        const responses = Object.keys(examAnswers).map(questionId => {
          const answer = examAnswers[questionId];
          return {
            questionId,
            responseText: typeof answer === 'string' ? answer : null,
            selectedOptionIndex: typeof answer === 'number' ? answer : null
          };
        }).filter(resp => resp.responseText !== null || resp.selectedOptionIndex !== null);
        
        await testService.submitTest(currentExam.value.id, responses);
        examSubmitted.value = true;
        message.success('Exam submitted successfully!');
        
        // Refresh exams list
        fetchExams();
      } catch (error) {
        console.error('Error submitting exam:', error);
        message.error('Failed to submit exam');
      } finally {
        submittingExam.value = false;
      }
    };
    
    const cancelExam = () => {
      examModalVisible.value = false;
      currentExam.value = null;
    };
    
    const handleSearch = () => {
      // Simple client-side search
      if (searchText.value) {
        exams.value = exams.value.filter(exam => 
          exam.title.toLowerCase().includes(searchText.value.toLowerCase()) ||
          exam.description.toLowerCase().includes(searchText.value.toLowerCase())
        );
      } else {
        fetchExams();
      }
    };
    
    const viewErrorBook = () => {
      router.push('/student/error-book');
    };
    
    const logout = () => {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete axios.defaults.headers.common['Authorization'];
      router.push('/login');
    };
    
    return {
      loadingExams,
      exams,
      searchText,
      examModalVisible,
      currentExam,
      examAnswers,
      examSubmitted,
      submittingExam,
      user,
      examColumns,
      fetchExams,
      getExamStatus,
      getExamStatusColor,
      isExamAvailable,
      getExamActionText,
      takeExam,
      submitExam,
      cancelExam,
      handleSearch,
      viewErrorBook,
      logout
    };
  }
};
</script>