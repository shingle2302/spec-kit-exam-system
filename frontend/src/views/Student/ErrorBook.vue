<template>
  <div>
    <a-page-header
      title="Error Book"
      sub-title="Review and practice questions you got wrong"
      @back="() => $router.push('/student/dashboard')"
    >
      <template #extra>
        <a-button @click="fetchErrorBook" type="primary">
          <template #icon><ReloadOutlined /></template>
          Refresh
        </a-button>
      </template>
    </a-page-header>
    
    <a-row :gutter="[16, 16]">
      <a-col :span="24">
        <a-card title="My Error Book" :loading="loading">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
            <a-button @click="fetchErrorBook">
              <template #icon><ReloadOutlined /></template>
              Refresh
            </a-button>
            <a-input-search
              v-model:value="searchText"
              placeholder="Search questions..."
              style="width: 300px;"
              @search="handleSearch"
            />
          </div>
          
          <a-table 
            :columns="errorBookColumns" 
            :data-source="errorBook" 
            :pagination="{ pageSize: 10 }"
            row-key="id"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'mastered'">
                <a-tag :color="record.mastered ? 'green' : 'orange'">
                  {{ record.mastered ? 'Mastered' : 'Not Mastered' }}
                </a-tag>
              </template>
              <template v-if="column.dataIndex === 'action'">
                <a-button 
                  type="primary" 
                  size="small" 
                  @click="practiceQuestion(record)"
                >
                  Practice
                </a-button>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- Practice Modal -->
    <a-modal
      v-model:visible="practiceModalVisible"
      :title="currentQuestion?.questionText"
      :footer="null"
      :width="800"
      :destroy-on-close="true"
      @cancel="cancelPractice"
    >
      <div v-if="currentQuestion">
        <div style="margin-bottom: 20px;">
          <p><strong>Knowledge Point:</strong> {{ currentQuestion.knowledgePoint }}</p>
          <p><strong>Subject:</strong> {{ currentQuestion.subjectId }}</p>
        </div>
        
        <!-- Question content based on type -->
        <div v-if="currentQuestion.questionType === 'MULTIPLE_CHOICE' || currentQuestion.questionType === 'SINGLE_CHOICE'">
          <a-radio-group v-model:value="practiceAnswer" name="practice-answer">
            <a-space direction="vertical">
              <a-radio 
                v-for="(option, index) in currentQuestion.answerOptions" 
                :key="index" 
                :value="index"
              >
                {{ option.optionText }}
              </a-radio>
            </a-space>
          </a-radio-group>
        </div>
        <div v-else-if="currentQuestion.questionType === 'TRUE_FALSE'">
          <a-radio-group v-model:value="practiceAnswer">
            <a-radio :value="0">True</a-radio>
            <a-radio :value="1">False</a-radio>
          </a-radio-group>
        </div>
        <div v-else-if="currentQuestion.questionType === 'SHORT_ANSWER' || currentQuestion.questionType === 'ESSAY'">
          <a-textarea 
            v-model:value="practiceAnswer" 
            :rows="4"
            placeholder="Enter your answer here..."
          />
        </div>
        
        <div style="margin-top: 20px;">
          <a-button 
            type="primary" 
            @click="submitPractice"
            :loading="submittingPractice"
          >
            Submit Answer
          </a-button>
          <a-button @click="cancelPractice" style="margin-left: 10px;">
            Cancel
          </a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { ReloadOutlined } from '@ant-design/icons-vue';
import errorBookService from '@/services/errorBookService';
import questionService from '@/services/questionService';

export default {
  name: 'StudentErrorBook',
  components: {
    ReloadOutlined
  },
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const errorBook = ref([]);
    const searchText = ref('');
    const practiceModalVisible = ref(false);
    const currentQuestion = ref(null);
    const practiceAnswer = ref(null);
    const submittingPractice = ref(false);
    const user = ref(null);
    
    // Load user from localStorage
    onMounted(() => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
        fetchErrorBook();
      } else {
        router.push('/login');
      }
    });
    
    const errorBookColumns = [
      {
        title: 'Question',
        dataIndex: 'questionText',
        key: 'questionText',
      },
      {
        title: 'Subject',
        dataIndex: 'subjectId',
        key: 'subjectId',
      },
      {
        title: 'Knowledge Point',
        dataIndex: 'knowledgePoint',
        key: 'knowledgePoint',
      },
      {
        title: 'Error Count',
        dataIndex: 'errorCount',
        key: 'errorCount',
      },
      {
        title: 'Mastery Count',
        dataIndex: 'masteryCount',
        key: 'masteryCount',
      },
      {
        title: 'Status',
        dataIndex: 'mastered',
        key: 'mastered',
      },
      {
        title: 'Action',
        key: 'action',
        width: '120px'
      },
    ];
    
    const fetchErrorBook = async () => {
      loading.value = true;
      try {
        const userId = user.value?.id;
        if (userId) {
          const data = await errorBookService.getErrorBookForStudent(userId);
          errorBook.value = data;
        }
      } catch (error) {
        console.error('Error fetching error book:', error);
        message.error('Failed to fetch error book');
      } finally {
        loading.value = false;
      }
    };
    
    const practiceQuestion = async (errorBookItem) => {
      try {
        const question = await questionService.getQuestionById(errorBookItem.questionId);
        currentQuestion.value = question;
        practiceModalVisible.value = true;
        practiceAnswer.value = null;
      } catch (error) {
        console.error('Error fetching question:', error);
        message.error('Failed to load question');
      }
    };
    
    const submitPractice = async () => {
      if (practiceAnswer.value === null) {
        message.warning('Please provide an answer');
        return;
      }
      
      submittingPractice.value = true;
      try {
        // In a real implementation, we would submit the answer and get feedback
        // For now, we'll just close the modal and refresh the error book
        message.success('Answer submitted! Keep practicing!');
        practiceModalVisible.value = false;
        fetchErrorBook();
      } catch (error) {
        console.error('Error submitting practice:', error);
        message.error('Failed to submit answer');
      } finally {
        submittingPractice.value = false;
      }
    };
    
    const cancelPractice = () => {
      practiceModalVisible.value = false;
      currentQuestion.value = null;
      practiceAnswer.value = null;
    };
    
    const handleSearch = () => {
      // Simple client-side search
      if (searchText.value) {
        errorBook.value = errorBook.value.filter(item => 
          item.questionText.toLowerCase().includes(searchText.value.toLowerCase()) ||
          item.subjectId.toLowerCase().includes(searchText.value.toLowerCase()) ||
          item.knowledgePoint.toLowerCase().includes(searchText.value.toLowerCase())
        );
      } else {
        fetchErrorBook();
      }
    };
    
    return {
      loading,
      errorBook,
      searchText,
      practiceModalVisible,
      currentQuestion,
      practiceAnswer,
      submittingPractice,
      user,
      errorBookColumns,
      fetchErrorBook,
      practiceQuestion,
      submitPractice,
      cancelPractice,
      handleSearch
    };
  }
};
</script>