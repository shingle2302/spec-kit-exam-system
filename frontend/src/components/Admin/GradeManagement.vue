<template>
  <div class="grade-management">
    <a-card title="Grade Management">
      <div class="management-actions">
        <a-button type="primary" @click="showCreateModal = true">
          Add New Grade
        </a-button>
      </div>
      
      <div class="grade-list">
        <a-table
          :columns="columns"
          :data-source="grades"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'isActive'">
              <a-tag :color="record.isActive ? 'green' : 'red'">
                {{ record.isActive ? 'Active' : 'Inactive' }}
              </a-tag>
            </template>
            <template v-if="column.dataIndex === 'actions'">
              <a-button type="link" @click="editGrade(record)">Edit</a-button>
              <a-button type="link" danger @click="deleteGrade(record.id)">Delete</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- Create/Edit Grade Modal -->
    <a-modal
      v-model:visible="showCreateModal"
      :title="editingGrade ? 'Edit Grade' : 'Create Grade'"
      @ok="handleSubmit"
      @cancel="handleCancel"
      :confirm-loading="submitting"
    >
      <a-form
        :model="formState"
        name="gradeForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
      >
        <a-form-item
          label="Grade Name"
          name="name"
          :rules="[{ required: true, message: 'Please input grade name!' }]"
        >
          <a-input v-model:value="formState.name" />
        </a-form-item>

        <a-form-item
          label="Grade Code"
          name="code"
          :rules="[{ required: true, message: 'Please input grade code!' }]"
        >
          <a-input v-model:value="formState.code" />
        </a-form-item>

        <a-form-item
          label="Description"
          name="description"
        >
          <a-textarea v-model:value="formState.description" :rows="4" />
        </a-form-item>

        <a-form-item
          label="Min Age"
          name="minAge"
        >
          <a-input-number v-model:value="formState.minAge" :min="0" style="width: 100%;" />
        </a-form-item>

        <a-form-item
          label="Max Age"
          name="maxAge"
        >
          <a-input-number v-model:value="formState.maxAge" :min="0" style="width: 100%;" />
        </a-form-item>

        <a-form-item
          label="Status"
          name="isActive"
        >
          <a-switch
            v-model:checked="formState.isActive"
            checked-children="Active"
            un-checked-children="Inactive"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { adminService } from '@/services/adminService';

// State variables
const grades = ref([]);
const loading = ref(false);
const submitting = ref(false);
const showCreateModal = ref(false);
const editingGrade = ref(null);

// Form state
const formState = reactive({
  name: '',
  code: '',
  description: '',
  minAge: null,
  maxAge: null,
  isActive: true
});

// Table columns
const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Code',
    dataIndex: 'code',
    key: 'code',
  },
  {
    title: 'Description',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: 'Age Range',
    key: 'ageRange',
    customRender: ({ record }) => `${record.minAge || 0} - ${record.maxAge || 'N/A'}`,
  },
  {
    title: 'Status',
    dataIndex: 'isActive',
    key: 'isActive',
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
  await loadGrades();
});

// Load grades
const loadGrades = async () => {
  loading.value = true;
  try {
    const response = await adminService.getGrades();
    grades.value = response.data || [];
    pagination.value.total = response.total || grades.value.length;
  } catch (error) {
    message.error('Failed to load grades: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Edit grade
const editGrade = (grade) => {
  editingGrade.value = grade;
  Object.assign(formState, {
    name: grade.name,
    code: grade.code,
    description: grade.description,
    minAge: grade.minAge,
    maxAge: grade.maxAge,
    isActive: grade.isActive
  });
  showCreateModal.value = true;
};

// Delete grade
const deleteGrade = async (gradeId) => {
  try {
    await adminService.deleteGrade(gradeId);
    message.success('Grade deleted successfully!');
    await loadGrades(); // Refresh the list
  } catch (error) {
    message.error('Failed to delete grade: ' + error.message);
  }
};

// Handle form submission
const handleSubmit = async () => {
  submitting.value = true;
  try {
    if (editingGrade.value) {
      // Update existing grade
      await adminService.updateGrade(editingGrade.value.id, { ...formState });
      message.success('Grade updated successfully!');
    } else {
      // Create new grade
      await adminService.createGrade({ ...formState });
      message.success('Grade created successfully!');
    }
    
    // Close modal and refresh list
    showCreateModal.value = false;
    editingGrade.value = null;
    resetForm();
    await loadGrades();
  } catch (error) {
    message.error('Failed to save grade: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Handle modal cancel
const handleCancel = () => {
  showCreateModal.value = false;
  editingGrade.value = null;
  resetForm();
};

// Reset form
const resetForm = () => {
  Object.keys(formState).forEach(key => {
    if (key === 'isActive') {
      formState[key] = true;
    } else {
      formState[key] = key === 'minAge' || key === 'maxAge' ? null : '';
    }
  });
};
</script>

<style scoped>
.grade-management {
  padding: 20px;
}

.management-actions {
  margin-bottom: 20px;
}

.grade-list {
  margin-top: 20px;
}
</style>