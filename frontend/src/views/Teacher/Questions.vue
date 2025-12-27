<template>
  <div>
    <a-page-header
      title="Question Management"
      sub-title="Create and manage questions for exams"
      @back="() => $router.push('/teacher/dashboard')"
    >
      <template #extra>
        <a-button @click="fetchQuestions" type="primary">
          <template #icon><ReloadOutlined /></template>
          Refresh
        </a-button>
        <a-button type="primary" @click="showCreateModal">
          <template #icon><PlusOutlined /></template>
          Create Question
        </a-button>
      </template>
    </a-page-header>
    
    <a-card title="Questions">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <a-button @click="fetchQuestions">
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
        :columns="questionColumns" 
        :data-source="questions" 
        :pagination="{ pageSize: 10 }"
        row-key="id"
        :loading="loading"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'questionType'">
            <a-tag :color="getQuestionTypeColor(record.questionType)">
              {{ record.questionType }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'isActive'">
            <a-tag :color="record.isActive ? 'green' : 'red'">
              {{ record.isActive ? 'Active' : 'Inactive' }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="editQuestion(record)">Edit</a-button>
              <a-button type="link" size="small" danger @click="deleteQuestion(record)">Delete</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
    
    <!-- Create/Edit Question Modal -->
    <a-modal
      v-model:visible="modalVisible"
      :title="editingQuestion ? 'Edit Question' : 'Create Question'"
      :footer="null"
      :width="800"
      :destroy-on-close="true"
      @cancel="cancelQuestion"
    >
      <a-form :model="formState" @finish="onFinish" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item 
          label="Question Text" 
          name="questionText"
          :rules="[{ required: true, message: 'Please input the question text!' }]"
        >
          <a-textarea 
            v-model:value="formState.questionText" 
            placeholder="Enter the question text"
            :rows="4"
          />
        </a-form-item>
        
        <a-form-item 
          label="Question Type" 
          name="questionType"
          :rules="[{ required: true, message: 'Please select a question type!' }]"
        >
          <a-select v-model:value="formState.questionType" placeholder="Select question type">
            <a-select-option value="MULTIPLE_CHOICE">Multiple Choice</a-select-option>
            <a-select-option value="SINGLE_CHOICE">Single Choice</a-select-option>
            <a-select-option value="TRUE_FALSE">True/False</a-select-option>
            <a-select-option value="SHORT_ANSWER">Short Answer</a-select-option>
            <a-select-option value="ESSAY">Essay</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item 
          label="Subject" 
          name="subjectId"
          :rules="[{ required: true, message: 'Please input the subject!' }]"
        >
          <a-input v-model:value="formState.subjectId" placeholder="Enter subject (e.g., Math, Science)" />
        </a-form-item>
        
        <a-form-item 
          label="Grade" 
          name="gradeId"
          :rules="[{ required: true, message: 'Please input the grade!' }]"
        >
          <a-input v-model:value="formState.gradeId" placeholder="Enter grade (e.g., Grade 10)" />
        </a-form-item>
        
        <a-form-item 
          label="Knowledge Point" 
          name="knowledgePoint"
          :rules="[{ required: true, message: 'Please input the knowledge point!' }]"
        >
          <a-input v-model:value="formState.knowledgePoint" placeholder="Enter knowledge point" />
        </a-form-item>
        
        <a-form-item 
          label="Standard Explanation" 
          name="standardExplanation"
        >
          <a-textarea 
            v-model:value="formState.standardExplanation" 
            placeholder="Enter standard explanation or answer"
            :rows="3"
          />
        </a-form-item>
        
        <a-form-item 
          label="Active" 
          name="isActive"
        >
          <a-switch v-model:checked="formState.isActive" />
        </a-form-item>
        
        <!-- Answer Options for Multiple Choice/Single Choice -->
        <a-form-item 
          v-if="formState.questionType === 'MULTIPLE_CHOICE' || formState.questionType === 'SINGLE_CHOICE'"
          label="Answer Options"
        >
          <div v-for="(option, index) in formState.answerOptions" :key="index" style="margin-bottom: 10px;">
            <a-input-group compact>
              <a-input 
                v-model:value="option.optionText" 
                :style="{ width: 'calc(100% - 80px)' }"
                :placeholder="`Option ${index + 1}`"
              />
              <a-button 
                type="dashed" 
                @click="removeOption(index)"
                style="width: 60px;"
              >
                <template #icon><MinusOutlined /></template>
              </a-button>
            </a-input-group>
          </div>
          <a-button type="dashed" @click="addOption" block style="margin-top: 10px;">
            <template #icon><PlusOutlined /></template>
            Add Option
          </a-button>
        </a-form-item>
        
        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center;">
          <a-button type="primary" html-type="submit" :loading="submitting">
            {{ editingQuestion ? 'Update' : 'Create' }} Question
          </a-button>
          <a-button @click="cancelQuestion" style="margin-left: 10px;">
            Cancel
          </a-button>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { 
  ReloadOutlined, 
  PlusOutlined, 
  MinusOutlined 
} from '@ant-design/icons-vue';
import questionService from '@/services/questionService';

export default {
  name: 'TeacherQuestionManagement',
  components: {
    ReloadOutlined,
    PlusOutlined,
    MinusOutlined
  },
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const questions = ref([]);
    const searchText = ref('');
    const modalVisible = ref(false);
    const editingQuestion = ref(null);
    const submitting = ref(false);
    const user = ref(null);
    
    // Form state
    const formState = reactive({
      id: null,
      questionText: '',
      questionType: 'MULTIPLE_CHOICE',
      subjectId: '',
      gradeId: '',
      knowledgePoint: '',
      standardExplanation: '',
      isActive: true,
      answerOptions: [
        { optionText: '' },
        { optionText: '' }
      ]
    });
    
    // Load user from localStorage
    onMounted(() => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
        fetchQuestions();
      } else {
        router.push('/login');
      }
    });
    
    const questionColumns = [
      {
        title: 'Question',
        dataIndex: 'questionText',
        key: 'questionText',
        ellipsis: true,
      },
      {
        title: 'Type',
        dataIndex: 'questionType',
        key: 'questionType',
      },
      {
        title: 'Subject',
        dataIndex: 'subjectId',
        key: 'subjectId',
      },
      {
        title: 'Grade',
        dataIndex: 'gradeId',
        key: 'gradeId',
      },
      {
        title: 'Knowledge Point',
        dataIndex: 'knowledgePoint',
        key: 'knowledgePoint',
      },
      {
        title: 'Status',
        dataIndex: 'isActive',
        key: 'isActive',
      },
      {
        title: 'Action',
        key: 'action',
        width: '150px'
      },
    ];
    
    const fetchQuestions = async () => {
      loading.value = true;
      try {
        // Get questions created by this teacher
        const data = await questionService.getQuestions({ createdBy: user.value?.id });
        questions.value = data;
      } catch (error) {
        console.error('Error fetching questions:', error);
        message.error('Failed to fetch questions');
      } finally {
        loading.value = false;
      }
    };
    
    const getQuestionTypeColor = (type) => {
      const colors = {
        'MULTIPLE_CHOICE': 'blue',
        'SINGLE_CHOICE': 'cyan',
        'TRUE_FALSE': 'orange',
        'SHORT_ANSWER': 'green',
        'ESSAY': 'purple'
      };
      return colors[type] || 'default';
    };
    
    const showCreateModal = () => {
      resetForm();
      modalVisible.value = true;
    };
    
    const editQuestion = (question) => {
      editingQuestion.value = question;
      Object.assign(formState, {
        id: question.id,
        questionText: question.questionText,
        questionType: question.questionType,
        subjectId: question.subjectId,
        gradeId: question.gradeId,
        knowledgePoint: question.knowledgePoint,
        standardExplanation: question.standardExplanation,
        isActive: question.isActive,
        answerOptions: question.answerOptions ? [...question.answerOptions] : [{ optionText: '' }, { optionText: '' }]
      });
      modalVisible.value = true;
    };
    
    const deleteQuestion = async (question) => {
      try {
        await questionService.deleteQuestion(question.id);
        message.success('Question deleted successfully');
        fetchQuestions();
      } catch (error) {
        console.error('Error deleting question:', error);
        message.error('Failed to delete question');
      }
    };
    
    const onFinish = async () => {
      submitting.value = true;
      try {
        if (editingQuestion.value) {
          await questionService.updateQuestion(formState.id, {
            ...formState,
            createdBy: user.value?.id
          });
          message.success('Question updated successfully');
        } else {
          await questionService.createQuestion({
            ...formState,
            createdBy: user.value?.id
          });
          message.success('Question created successfully');
        }
        
        modalVisible.value = false;
        fetchQuestions();
      } catch (error) {
        console.error('Error saving question:', error);
        message.error(error.response?.data?.message || 'Failed to save question');
      } finally {
        submitting.value = false;
      }
    };
    
    const cancelQuestion = () => {
      modalVisible.value = false;
      editingQuestion.value = null;
      resetForm();
    };
    
    const resetForm = () => {
      formState.id = null;
      formState.questionText = '';
      formState.questionType = 'MULTIPLE_CHOICE';
      formState.subjectId = '';
      formState.gradeId = '';
      formState.knowledgePoint = '';
      formState.standardExplanation = '';
      formState.isActive = true;
      formState.answerOptions = [
        { optionText: '' },
        { optionText: '' }
      ];
    };
    
    const addOption = () => {
      formState.answerOptions.push({ optionText: '' });
    };
    
    const removeOption = (index) => {
      if (formState.answerOptions.length > 2) {
        formState.answerOptions.splice(index, 1);
      } else {
        message.warning('At least 2 options are required');
      }
    };
    
    const handleSearch = () => {
      // Simple client-side search
      if (searchText.value) {
        questions.value = questions.value.filter(question => 
          question.questionText.toLowerCase().includes(searchText.value.toLowerCase()) ||
          question.subjectId.toLowerCase().includes(searchText.value.toLowerCase()) ||
          question.knowledgePoint.toLowerCase().includes(searchText.value.toLowerCase())
        );
      } else {
        fetchQuestions();
      }
    };
    
    return {
      loading,
      questions,
      searchText,
      modalVisible,
      editingQuestion,
      submitting,
      user,
      formState,
      questionColumns,
      fetchQuestions,
      getQuestionTypeColor,
      showCreateModal,
      editQuestion,
      deleteQuestion,
      onFinish,
      cancelQuestion,
      addOption,
      removeOption,
      handleSearch
    };
  }
};
</script>