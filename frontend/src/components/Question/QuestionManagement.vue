<template>
  <div>
    <h2>Question Management</h2>
    
    <!-- Filter Section -->
    <a-card title="Filter Questions">
      <a-form layout="inline" :model="filterForm">
        <a-form-item label="Subject">
          <a-select 
            v-model:value="filterForm.subjectId" 
            placeholder="Select subject"
            style="width: 200px"
          >
            <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
              {{ subject.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="Grade">
          <a-select 
            v-model:value="filterForm.gradeId" 
            placeholder="Select grade"
            style="width: 200px"
          >
            <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
              {{ grade.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="Knowledge Point">
          <a-input 
            v-model:value="filterForm.knowledgePoint" 
            placeholder="Enter knowledge point" 
            style="width: 200px"
          />
        </a-form-item>
        
        <a-form-item>
          <a-button type="primary" @click="fetchQuestions">Search</a-button>
        </a-form-item>
      </a-form>
    </a-card>
    
    <!-- Add Question Button -->
    <div style="margin: 16px 0;">
      <a-button type="primary" @click="showAddQuestionForm = true">Add New Question</a-button>
    </div>
    
    <!-- Questions List -->
    <a-table 
      :columns="columns" 
      :data-source="questions" 
      :loading="loading"
      row-key="id"
      :pagination="{ pageSize: 10 }"
    >
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'questionType'">
          {{ formatQuestionType(record.questionType) }}
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <a-button type="link" @click="editQuestion(record)">Edit</a-button>
          <a-button type="link" danger @click="deleteQuestion(record.id)">Delete</a-button>
        </template>
      </template>
    </a-table>
    
    <!-- Add/Edit Question Modal -->
    <a-modal
      v-model:visible="showAddQuestionForm"
      :title="isEditing ? 'Edit Question' : 'Add Question'"
      :footer="null"
      width="800px"
    >
      <QuestionForm 
        :initialData="editingQuestion" 
        @question-saved="onQuestionSaved"
        @cancel="showAddQuestionForm = false"
      />
    </a-modal>
  </div>
</template>

<script>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import QuestionForm from './QuestionForm.vue';
import questionService from '@/services/questionService';

export default {
  name: 'QuestionManagement',
  components: {
    QuestionForm
  },
  setup() {
    const questions = ref([]);
    const subjects = ref([]);
    const grades = ref([]);
    const loading = ref(false);
    const showAddQuestionForm = ref(false);
    const isEditing = ref(false);
    const editingQuestion = ref(null);
    
    const filterForm = reactive({
      subjectId: '',
      gradeId: '',
      knowledgePoint: ''
    });

    const columns = [
      {
        title: 'Question Text',
        dataIndex: 'questionText',
        key: 'questionText',
        ellipsis: true
      },
      {
        title: 'Type',
        dataIndex: 'questionType',
        key: 'questionType',
        slots: { customRender: 'questionType' }
      },
      {
        title: 'Subject',
        dataIndex: 'subjectId',
        key: 'subjectId'
      },
      {
        title: 'Grade',
        dataIndex: 'gradeId',
        key: 'gradeId'
      },
      {
        title: 'Knowledge Point',
        dataIndex: 'knowledgePoint',
        key: 'knowledgePoint'
      },
      {
        title: 'Created By',
        dataIndex: 'createdBy',
        key: 'createdBy'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const fetchQuestions = async () => {
      loading.value = true;
      try {
        const params = {
          subjectId: filterForm.subjectId || undefined,
          gradeId: filterForm.gradeId || undefined,
          knowledgePoint: filterForm.knowledgePoint || undefined
        };
        const response = await questionService.getQuestions(params);
        questions.value = response;
      } catch (error) {
        message.error('Failed to fetch questions: ' + error.message);
      } finally {
        loading.value = false;
      }
    };

    const formatQuestionType = (type) => {
      if (!type) return '';
      return type.split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
    };

    const deleteQuestion = async (id) => {
      try {
        await questionService.deleteQuestion(id);
        message.success('Question deleted successfully');
        fetchQuestions(); // Refresh the list
      } catch (error) {
        message.error('Failed to delete question: ' + error.message);
      }
    };

    const editQuestion = (question) => {
      editingQuestion.value = question;
      isEditing.value = true;
      showAddQuestionForm.value = true;
    };

    const onQuestionSaved = () => {
      showAddQuestionForm.value = false;
      isEditing.value = false;
      editingQuestion.value = null;
      fetchQuestions(); // Refresh the list
    };

    onMounted(() => {
      // Initialize with mock data for subjects and grades
      // In real implementation, you would fetch from API
      subjects.value = [
        { id: '1', name: 'Mathematics' },
        { id: '2', name: 'Science' },
        { id: '3', name: 'English' }
      ];
      
      grades.value = [
        { id: '1', name: 'Grade 1' },
        { id: '2', name: 'Grade 2' },
        { id: '3', name: 'Grade 3' }
      ];
      
      fetchQuestions();
    });

    return {
      questions,
      subjects,
      grades,
      loading,
      showAddQuestionForm,
      isEditing,
      editingQuestion,
      filterForm,
      columns,
      fetchQuestions,
      formatQuestionType,
      deleteQuestion,
      editQuestion,
      onQuestionSaved
    };
  }
};
</script>