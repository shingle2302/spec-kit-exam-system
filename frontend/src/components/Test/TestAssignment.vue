<template>
  <div class="test-assignment">
    <h2>Assign Test to Students</h2>
    
    <a-form
      :model="formState"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 14 }"
      layout="horizontal"
      @finish="onSubmit"
    >
      <a-form-item label="Test" name="testId" :rules="[{ required: true, message: 'Please select a test' }]">
        <a-select
          v-model:value="formState.testId"
          placeholder="Select a test"
          @change="onTestChange"
        >
          <a-select-option
            v-for="test in tests"
            :key="test.id"
            :value="test.id"
          >
            {{ test.title }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Students" name="studentIds" :rules="[{ required: true, message: 'Please select at least one student' }]">
        <a-select
          v-model:value="formState.studentIds"
          mode="multiple"
          placeholder="Select students"
          :filter-option="false"
          @search="fetchStudents"
        >
          <a-select-option
            v-for="student in students"
            :key="student.id"
            :value="student.id"
          >
            {{ student.firstName }} {{ student.lastName }} ({{ student.username }})
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Due Date" name="dueDate">
        <a-date-picker
          v-model:value="formState.dueDate"
          show-time
          format="YYYY-MM-DD HH:mm:ss"
          placeholder="Select due date"
          :disabled-date="disabledDate"
        />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 4, span: 14 }">
        <a-button type="primary" html-type="submit" :loading="loading">
          Assign Test
        </a-button>
        <a-button style="margin-left: 8px" @click="resetForm">
          Reset
        </a-button>
      </a-form-item>
    </a-form>

    <div v-if="assignmentResult" class="assignment-result">
      <a-alert
        :message="`Successfully assigned test to ${assignmentResult.length} students`"
        type="success"
        show-icon
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { testAssignmentService } from '@/services/testAssignmentService';
import { testService } from '@/services/testService';
import { adminService } from '@/services/adminService';
import { message } from 'ant-design-vue';
import type { Dayjs } from 'dayjs';

interface Test {
  id: number;
  title: string;
  description: string;
}

interface Student {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
}

interface FormState {
  testId: number | null;
  studentIds: number[];
  dueDate: Dayjs | null;
}

const formState = reactive<FormState>({
  testId: null,
  studentIds: [],
  dueDate: null,
});

const tests = ref<Test[]>([]);
const students = ref<Student[]>([]);
const loading = ref(false);
const assignmentResult = ref<any[]>([]);

const disabledDate = (current: Dayjs) => {
  return current && current < new Date().setHours(0, 0, 0, 0);
};

const fetchTests = async () => {
  try {
    const response = await testService.getTests();
    tests.value = response;
  } catch (error) {
    message.error('Failed to load tests');
    console.error('Error fetching tests:', error);
  }
};

const fetchStudents = async (search?: string) => {
  try {
    // If search is provided, filter students; otherwise load all students
    const params: any = {};
    if (search) {
      params.search = search;
    }
    const response = await adminService.getStudents(params);
    students.value = response;
  } catch (error) {
    message.error('Failed to load students');
    console.error('Error fetching students:', error);
  }
};

const onTestChange = (value: number) => {
  console.log('Selected test:', value);
};

const onSubmit = async () => {
  if (!formState.testId || formState.studentIds.length === 0) {
    message.warning('Please select a test and at least one student');
    return;
  }

  loading.value = true;
  assignmentResult.value = [];

  try {
    const requestData = {
      testId: formState.testId,
      studentIds: formState.studentIds,
      assignedBy: 1, // This should come from the current user context
      dueDate: formState.dueDate ? formState.dueDate.toISOString() : undefined,
    };

    const result = await testAssignmentService.bulkAssignTest(requestData);
    assignmentResult.value = result;
    message.success('Test assigned successfully to students');
    
    // Reset form after successful assignment
    resetForm();
  } catch (error) {
    message.error('Failed to assign test');
    console.error('Error assigning test:', error);
  } finally {
    loading.value = false;
  }
};

const resetForm = () => {
  formState.testId = null;
  formState.studentIds = [];
  formState.dueDate = null;
  assignmentResult.value = [];
};

onMounted(() => {
  fetchTests();
  fetchStudents();
});
</script>

<style scoped>
.test-assignment {
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.assignment-result {
  margin-top: 20px;
}
</style>