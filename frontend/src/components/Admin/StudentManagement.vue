<template>
  <div class="student-management">
    <a-card title="Student Management">
      <div class="management-actions">
        <a-button type="primary" @click="showCreateModal = true">
          Add New Student
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
              <a-button type="link" @click="editStudent(record)">Edit</a-button>
              <a-button type="link" danger @click="deleteStudent(record.id)">Delete</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- Create/Edit Student Modal -->
    <a-modal
      v-model:visible="showCreateModal"
      :title="editingStudent ? 'Edit Student' : 'Create Student'"
      @ok="handleSubmit"
      @cancel="handleCancel"
      :confirm-loading="submitting"
    >
      <a-form
        :model="formState"
        name="studentForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
      >
        <a-form-item
          label="Student ID"
          name="studentId"
          :rules="[{ required: true, message: 'Please input student ID!' }]"
        >
          <a-input v-model:value="formState.studentId" :disabled="!!editingStudent" />
        </a-form-item>

        <a-form-item
          label="First Name"
          name="firstName"
          :rules="[{ required: true, message: 'Please input first name!' }]"
        >
          <a-input v-model:value="formState.firstName" />
        </a-form-item>

        <a-form-item
          label="Last Name"
          name="lastName"
          :rules="[{ required: true, message: 'Please input last name!' }]"
        >
          <a-input v-model:value="formState.lastName" />
        </a-form-item>

        <a-form-item
          label="Email"
          name="email"
          :rules="[{ required: true, type: 'email', message: 'Please input valid email!' }]"
        >
          <a-input v-model:value="formState.email" />
        </a-form-item>

        <a-form-item
          label="Grade"
          name="gradeId"
          :rules="[{ required: true, message: 'Please select grade!' }]"
        >
          <a-select v-model:value="formState.gradeId" placeholder="Select grade">
            <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
              {{ grade.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Class"
          name="classId"
          :rules="[{ required: true, message: 'Please select class!' }]"
        >
          <a-select v-model:value="formState.classId" placeholder="Select class">
            <a-select-option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
              {{ classItem.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Parent ID"
          name="parentId"
        >
          <a-input v-model:value="formState.parentId" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { adminService } from '@/services/adminService';
import { gradeService } from '@/services/gradeService';
import { classService } from '@/services/classService';

// State variables
const students = ref([]);
const grades = ref([]);
const classes = ref([]);
const loading = ref(false);
const submitting = ref(false);
const showCreateModal = ref(false);
const editingStudent = ref(null);

// Form state
const formState = reactive({
  studentId: '',
  firstName: '',
  lastName: '',
  email: '',
  gradeId: null,
  classId: null,
  parentId: null
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
    title: 'Grade',
    dataIndex: 'gradeName',
    key: 'gradeName',
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
  await loadStudents();
  await loadGrades();
  await loadClasses();
});

// Load students
const loadStudents = async () => {
  loading.value = true;
  try {
    const response = await adminService.getStudents();
    students.value = response.data || [];
    pagination.value.total = response.total || students.value.length;
  } catch (error) {
    message.error('Failed to load students: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Load grades
const loadGrades = async () => {
  try {
    grades.value = await gradeService.getGrades();
  } catch (error) {
    message.error('Failed to load grades: ' + error.message);
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

// Edit student
const editStudent = (student) => {
  editingStudent.value = student;
  Object.assign(formState, {
    studentId: student.studentId,
    firstName: student.firstName,
    lastName: student.lastName,
    email: student.email,
    gradeId: student.gradeId,
    classId: student.classId,
    parentId: student.parentId
  });
  showCreateModal.value = true;
};

// Delete student
const deleteStudent = async (studentId) => {
  try {
    await adminService.deleteStudent(studentId);
    message.success('Student deleted successfully!');
    await loadStudents(); // Refresh the list
  } catch (error) {
    message.error('Failed to delete student: ' + error.message);
  }
};

// Handle form submission
const handleSubmit = async () => {
  submitting.value = true;
  try {
    if (editingStudent.value) {
      // Update existing student
      await adminService.updateStudent(editingStudent.value.id, { ...formState });
      message.success('Student updated successfully!');
    } else {
      // Create new student
      await adminService.createStudent({ ...formState });
      message.success('Student created successfully!');
    }
    
    // Close modal and refresh list
    showCreateModal.value = false;
    editingStudent.value = null;
    resetForm();
    await loadStudents();
  } catch (error) {
    message.error('Failed to save student: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Handle modal cancel
const handleCancel = () => {
  showCreateModal.value = false;
  editingStudent.value = null;
  resetForm();
};

// Reset form
const resetForm = () => {
  Object.keys(formState).forEach(key => {
    formState[key] = key === 'parentId' ? null : '';
  });
};
</script>

<style scoped>
.student-management {
  padding: 20px;
}

.management-actions {
  margin-bottom: 20px;
}

.student-list {
  margin-top: 20px;
}
</style>