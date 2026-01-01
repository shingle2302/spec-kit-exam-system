<template>
  <div class="teacher-student-management">
    <a-card title="Teacher Student Management">
      <div class="management-actions">
        <a-button type="primary" @click="showAddModal = true">
          Add Student to Class
        </a-button>
      </div>
      
      <div class="student-list">
        <a-table
          :columns="columns"
          :data-source="students"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'actions'">
              <a-button type="link" danger @click="removeStudent(record.id)">Remove</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- Add Student Modal -->
    <a-modal
      v-model:visible="showAddModal"
      title="Add Student to Class"
      @ok="addStudent"
      @cancel="handleAddCancel"
      :confirm-loading="addingStudent"
    >
      <a-form
        :model="addFormState"
        name="addStudentForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
      >
        <a-form-item
          label="Class"
          name="classId"
          :rules="[{ required: true, message: 'Please select a class!' }]"
        >
          <a-select v-model:value="addFormState.classId" placeholder="Select class">
            <a-select-option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
              {{ classItem.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Student"
          name="studentId"
          :rules="[{ required: true, message: 'Please select a student!' }]"
        >
          <a-select v-model:value="addFormState.studentId" placeholder="Select student">
            <a-select-option v-for="student in availableStudents" :key="student.id" :value="student.id">
              {{ student.firstName }} {{ student.lastName }} ({{ student.studentId }})
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { teacherService } from '@/services/teacherService';
import { classService } from '@/services/classService';
import { studentService } from '@/services/studentService';

// State variables
const students = ref([]);
const classes = ref([]);
const availableStudents = ref([]);
const loading = ref(false);
const addingStudent = ref(false);
const showAddModal = ref(false);

// Form state
const addFormState = reactive({
  classId: null,
  studentId: null
});

// Table columns
const columns = [
  {
    title: 'Student ID',
    dataIndex: 'studentId',
    key: 'studentId',
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    customRender: ({ record }) => `${record.firstName} ${record.lastName}`
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Class',
    dataIndex: 'className',
    key: 'className',
  },
  {
    title: 'Actions',
    dataIndex: 'actions',
    key: 'actions',
  },
];

// Pagination
const pagination = ref({
  pageSize: 10,
  current: 1,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
});

// Initialize component
onMounted(async () => {
  await loadMyStudents();
  await loadClasses();
  await loadAvailableStudents();
});

// Load my students
const loadMyStudents = async () => {
  loading.value = true;
  try {
    const response = await teacherService.getMyStudents();
    students.value = response.data || [];
    pagination.value.total = response.total || students.value.length;
  } catch (error) {
    message.error('Failed to load students: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Load classes
const loadClasses = async () => {
  try {
    classes.value = await classService.getClasses();
  } catch (error) {
    message.error('Failed to load classes: ' + error.message);
  }
};

// Load available students (not yet in teacher's classes)
const loadAvailableStudents = async () => {
  try {
    // Get all students
    const allStudents = await studentService.getStudents();
    
    // Get students already in teacher's classes
    const myStudents = await teacherService.getMyStudents();
    const myStudentIds = myStudents.map(s => s.id);
    
    // Available students are those not in my students
    availableStudents.value = allStudents.filter(s => !myStudentIds.includes(s.id));
  } catch (error) {
    message.error('Failed to load available students: ' + error.message);
  }
};

// Add student to class
const addStudent = async () => {
  addingStudent.value = true;
  try {
    await teacherService.addStudentToClass(addFormState.classId, addFormState.studentId);
    message.success('Student added to class successfully!');
    
    // Close modal and refresh lists
    showAddModal.value = false;
    resetAddForm();
    await loadMyStudents();
    await loadAvailableStudents();
  } catch (error) {
    message.error('Failed to add student to class: ' + error.message);
  } finally {
    addingStudent.value = false;
  }
};

// Remove student from class
const removeStudent = async (studentId) => {
  try {
    await teacherService.removeStudentFromClass(studentId);
    message.success('Student removed from class successfully!');
    await loadMyStudents();
    await loadAvailableStudents();
  } catch (error) {
    message.error('Failed to remove student from class: ' + error.message);
  }
};

// Handle add modal cancel
const handleAddCancel = () => {
  showAddModal.value = false;
  resetAddForm();
};

// Reset add form
const resetAddForm = () => {
  addFormState.classId = null;
  addFormState.studentId = null;
};
</script>

<style scoped>
.teacher-student-management {
  padding: 20px;
}

.management-actions {
  margin-bottom: 20px;
}

.student-list {
  margin-top: 20px;
}
</style>