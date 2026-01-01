<template>
  <div class="teacher-management">
    <a-card title="Teacher Management">
      <div class="management-actions">
        <a-button type="primary" @click="showCreateModal = true">
          Add New Teacher
        </a-button>
      </div>
      
      <div class="teacher-list">
        <a-table
          :columns="columns"
          :data-source="teachers"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'actions'">
              <a-button type="link" @click="editTeacher(record)">Edit</a-button>
              <a-button type="link" danger @click="deleteTeacher(record.id)">Delete</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- Create/Edit Teacher Modal -->
    <a-modal
      v-model:visible="showCreateModal"
      :title="editingTeacher ? 'Edit Teacher' : 'Create Teacher'"
      @ok="handleSubmit"
      @cancel="handleCancel"
      :confirm-loading="submitting"
    >
      <a-form
        :model="formState"
        name="teacherForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
      >
        <a-form-item
          label="Employee ID"
          name="employeeId"
          :rules="[{ required: true, message: 'Please input employee ID!' }]"
        >
          <a-input v-model:value="formState.employeeId" :disabled="!!editingTeacher" />
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
          label="Department"
          name="department"
          :rules="[{ required: true, message: 'Please input department!' }]"
        >
          <a-input v-model:value="formState.department" />
        </a-form-item>

        <a-form-item
          label="Hire Date"
          name="hireDate"
        >
          <a-date-picker v-model:value="formState.hireDate" style="width: 100%;" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { adminService } from '@/services/adminService';
import dayjs from 'dayjs';

// State variables
const teachers = ref([]);
const loading = ref(false);
const submitting = ref(false);
const showCreateModal = ref(false);
const editingTeacher = ref(null);

// Form state
const formState = reactive({
  employeeId: '',
  firstName: '',
  lastName: '',
  email: '',
  department: '',
  hireDate: null
});

// Table columns
const columns = [
  {
    title: 'Employee ID',
    dataIndex: 'employeeId',
    key: 'employeeId',
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
    title: 'Department',
    dataIndex: 'department',
    key: 'department',
  },
  {
    title: 'Hire Date',
    dataIndex: 'hireDate',
    key: 'hireDate',
    customRender: ({ text }) => text ? dayjs(text).format('YYYY-MM-DD') : ''
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
  await loadTeachers();
});

// Load teachers
const loadTeachers = async () => {
  loading.value = true;
  try {
    const response = await adminService.getTeachers();
    teachers.value = response.data || [];
    pagination.value.total = response.total || teachers.value.length;
  } catch (error) {
    message.error('Failed to load teachers: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Edit teacher
const editTeacher = (teacher) => {
  editingTeacher.value = teacher;
  Object.assign(formState, {
    employeeId: teacher.employeeId,
    firstName: teacher.firstName,
    lastName: teacher.lastName,
    email: teacher.email,
    department: teacher.department,
    hireDate: teacher.hireDate ? dayjs(teacher.hireDate) : null
  });
  showCreateModal.value = true;
};

// Delete teacher
const deleteTeacher = async (teacherId) => {
  try {
    await adminService.deleteTeacher(teacherId);
    message.success('Teacher deleted successfully!');
    await loadTeachers(); // Refresh the list
  } catch (error) {
    message.error('Failed to delete teacher: ' + error.message);
  }
};

// Handle form submission
const handleSubmit = async () => {
  submitting.value = true;
  try {
    // Format the date for submission
    const submitData = {
      ...formState,
      hireDate: formState.hireDate ? formState.hireDate.format('YYYY-MM-DD') : null
    };
    
    if (editingTeacher.value) {
      // Update existing teacher
      await adminService.updateTeacher(editingTeacher.value.id, submitData);
      message.success('Teacher updated successfully!');
    } else {
      // Create new teacher
      await adminService.createTeacher(submitData);
      message.success('Teacher created successfully!');
    }
    
    // Close modal and refresh list
    showCreateModal.value = false;
    editingTeacher.value = null;
    resetForm();
    await loadTeachers();
  } catch (error) {
    message.error('Failed to save teacher: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Handle modal cancel
const handleCancel = () => {
  showCreateModal.value = false;
  editingTeacher.value = null;
  resetForm();
};

// Reset form
const resetForm = () => {
  Object.keys(formState).forEach(key => {
    if (key === 'hireDate') {
      formState[key] = null;
    } else {
      formState[key] = '';
    }
  });
};
</script>

<style scoped>
.teacher-management {
  padding: 20px;
}

.management-actions {
  margin-bottom: 20px;
}

.teacher-list {
  margin-top: 20px;
}
</style>