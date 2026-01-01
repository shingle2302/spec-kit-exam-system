<template>
  <div class="test-form">
    <a-card title="Create Test">
      <a-form
        :model="formState"
        name="testForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 14 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="Title"
          name="title"
          :rules="[{ required: true, message: 'Please input test title!' }]"
        >
          <a-input v-model:value="formState.title" />
        </a-form-item>

        <a-form-item
          label="Description"
          name="description"
        >
          <a-textarea v-model:value="formState.description" />
        </a-form-item>

        <a-form-item
          label="Grade"
          name="gradeId"
          :rules="[{ required: true, message: 'Please select grade!' }]"
        >
          <a-select
            v-model:value="formState.gradeId"
            placeholder="Select grade"
            @change="onGradeChange"
          >
            <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
              {{ grade.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Subject"
          name="subjectId"
          :rules="[{ required: true, message: 'Please select subject!' }]"
        >
          <a-select
            v-model:value="formState.subjectId"
            placeholder="Select subject"
          >
            <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
              {{ subject.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Time Limit (minutes)"
          name="timeLimitMinutes"
        >
          <a-input-number v-model:value="formState.timeLimitMinutes" :min="0" />
        </a-form-item>

        <a-form-item
          label="Question Selection"
          name="questions"
        >
          <div class="question-selection">
            <a-button type="primary" @click="showQuestionModal = true">
              Select Questions
            </a-button>
            <div class="selected-questions">
              <a-tag 
                v-for="question in selectedQuestions" 
                :key="question.id"
                closable
                @close="removeQuestion(question.id)"
              >
                {{ question.content.substring(0, 30) }}...
              </a-tag>
            </div>
          </div>
        </a-form-item>

        <a-form-item
          label="Student Assignment"
          name="assignedStudents"
        >
          <div class="student-assignment">
            <a-button type="primary" @click="showStudentModal = true">
              Assign Students
            </a-button>
            <div class="assigned-students">
              <a-tag 
                v-for="student in assignedStudents" 
                :key="student.id"
                closable
                @close="removeStudent(student.id)"
              >
                {{ student.firstName }} {{ student.lastName }}
              </a-tag>
            </div>
          </div>
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 4, span: 14 }">
          <a-button type="primary" html-type="submit">
            Submit
          </a-button>
          <a-button style="margin-left: 8px;" @click="resetForm">
            Reset
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- Question Selection Modal -->
    <a-modal
      v-model:visible="showQuestionModal"
      title="Select Questions"
      :footer="null"
      width="80%"
    >
      <a-table
        :columns="questionColumns"
        :data-source="availableQuestions"
        :row-selection="{ selectedRowKeys: selectedQuestionIds, onChange: onQuestionSelect }"
        row-key="id"
        :pagination="{ pageSize: 10 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'content'">
            {{ record.content.substring(0, 50) }}...
          </template>
        </template>
      </a-table>
    </a-modal>

    <!-- Student Assignment Modal -->
    <a-modal
      v-model:visible="showStudentModal"
      title="Assign Students"
      :footer="null"
      width="60%"
    >
      <a-table
        :columns="studentColumns"
        :data-source="availableStudents"
        :row-selection="{ selectedRowKeys: assignedStudentIds, onChange: onStudentSelect }"
        row-key="id"
        :pagination="{ pageSize: 10 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'name'">
            {{ record.firstName }} {{ record.lastName }}
          </template>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { testService } from '@/services/testService';
import { questionService } from '@/services/questionService';
import { studentService } from '@/services/studentService';
import { gradeService } from '@/services/gradeService';
import { subjectService } from '@/services/subjectService';

// Form state
const formState = reactive({
  title: '',
  description: '',
  gradeId: null,
  subjectId: null,
  timeLimitMinutes: null,
  questions: [],
  assignedStudents: []
});

// Lists for dropdowns
const grades = ref([]);
const subjects = ref([]);
const availableQuestions = ref([]);
const availableStudents = ref([]);

// Selected items
const selectedQuestions = ref([]);
const assignedStudents = ref([]);
const selectedQuestionIds = ref([]);
const assignedStudentIds = ref([]);

// Modal visibility
const showQuestionModal = ref(false);
const showStudentModal = ref(false);

// Table columns
const questionColumns = [
  {
    title: 'Content',
    dataIndex: 'content',
    key: 'content',
  },
  {
    title: 'Type',
    dataIndex: 'questionType',
    key: 'questionType',
  },
  {
    title: 'Difficulty',
    dataIndex: 'difficultyLevel',
    key: 'difficultyLevel',
  },
];

const studentColumns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Student ID',
    dataIndex: 'studentId',
    key: 'studentId',
  },
  {
    title: 'Grade',
    dataIndex: 'gradeName',
    key: 'gradeName',
  },
];

// Fetch data on component mount
onMounted(async () => {
  await fetchGrades();
  await fetchSubjects();
});

// Fetch grades
const fetchGrades = async () => {
  try {
    grades.value = await gradeService.getGrades();
  } catch (error) {
    message.error('Failed to load grades');
  }
};

// Fetch subjects
const fetchSubjects = async () => {
  try {
    subjects.value = await subjectService.getSubjects();
  } catch (error) {
    message.error('Failed to load subjects');
  }
};

// Fetch questions based on grade and subject
const fetchQuestions = async () => {
  if (formState.gradeId && formState.subjectId) {
    try {
      availableQuestions.value = await questionService.getQuestions({
        gradeId: formState.gradeId,
        subjectId: formState.subjectId
      });
    } catch (error) {
      message.error('Failed to load questions');
    }
  }
};

// Fetch students based on grade
const fetchStudents = async () => {
  if (formState.gradeId) {
    try {
      availableStudents.value = await studentService.getStudentsByGrade(formState.gradeId);
    } catch (error) {
      message.error('Failed to load students');
    }
  }
};

// Handle grade change
const onGradeChange = async () => {
  await fetchQuestions();
  await fetchStudents();
};

// Handle question selection
const onQuestionSelect = (selectedRowKeys) => {
  selectedQuestionIds.value = selectedRowKeys;
  
  // Update selected questions based on selected IDs
  selectedQuestions.value = availableQuestions.value.filter(q => 
    selectedRowKeys.includes(q.id)
  );
};

// Remove question from selection
const removeQuestion = (questionId) => {
  selectedQuestionIds.value = selectedQuestionIds.value.filter(id => id !== questionId);
  selectedQuestions.value = selectedQuestions.value.filter(q => q.id !== questionId);
};

// Handle student selection
const onStudentSelect = (selectedRowKeys) => {
  assignedStudentIds.value = selectedRowKeys;
  
  // Update assigned students based on selected IDs
  assignedStudents.value = availableStudents.value.filter(s => 
    selectedRowKeys.includes(s.id)
  );
};

// Remove student from assignment
const removeStudent = (studentId) => {
  assignedStudentIds.value = assignedStudentIds.value.filter(id => id !== studentId);
  assignedStudents.value = assignedStudents.value.filter(s => s.id !== studentId);
};

// Form submission
const onFinish = async (values) => {
  try {
    const testData = {
      title: formState.title,
      description: formState.description,
      gradeId: formState.gradeId,
      subjectId: formState.subjectId,
      timeLimitMinutes: formState.timeLimitMinutes,
      isActive: true,
      isPublished: false
    };
    
    // Create the test
    const createdTest = await testService.createTest(testData);
    
    // Create test assignments for selected students
    if (assignedStudents.value.length > 0) {
      for (const student of assignedStudents.value) {
        await testService.assignTestToStudent(createdTest.id, student.id);
      }
    }
    
    message.success('Test created successfully!');
    resetForm();
  } catch (error) {
    message.error('Failed to create test: ' + error.message);
  }
};

// Form submission failed
const onFinishFailed = (errorInfo) => {
  console.log('Failed:', errorInfo);
};

// Reset form
const resetForm = () => {
  Object.keys(formState).forEach(key => {
    if (Array.isArray(formState[key])) {
      formState[key] = [];
    } else {
      formState[key] = key === 'timeLimitMinutes' ? null : '';
    }
  });
  selectedQuestions.value = [];
  assignedStudents.value = [];
  selectedQuestionIds.value = [];
  assignedStudentIds.value = [];
};
</script>

<style scoped>
.test-form {
  padding: 20px;
}

.question-selection, .student-assignment {
  display: flex;
  flex-direction: column;
}

.selected-questions, .assigned-students {
  margin-top: 10px;
}

.selected-questions .ant-tag, .assigned-students .ant-tag {
  margin-bottom: 5px;
}
</style>