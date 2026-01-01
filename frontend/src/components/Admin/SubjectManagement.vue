<template>
  <div class="subject-management">
    <a-card title="Subject Management">
      <div class="management-actions">
        <a-button type="primary" @click="showCreateModal = true">
          Add New Subject
        </a-button>
      </div>
      
      <div class="subject-list">
        <a-table
          :columns="columns"
          :data-source="subjects"
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
              <a-button type="link" @click="editSubject(record)">Edit</a-button>
              <a-button type="link" danger @click="deleteSubject(record.id)">Delete</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- Create/Edit Subject Modal -->
    <a-modal
      v-model:visible="showCreateModal"
      :title="editingSubject ? 'Edit Subject' : 'Create Subject'"
      @ok="handleSubmit"
      @cancel="handleCancel"
      :confirm-loading="submitting"
    >
      <a-form
        :model="formState"
        name="subjectForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
      >
        <a-form-item
          label="Subject Name"
          name="name"
          :rules="[{ required: true, message: 'Please input subject name!' }]"
        >
          <a-input v-model:value="formState.name" />
        </a-form-item>

        <a-form-item
          label="Subject Code"
          name="code"
          :rules="[{ required: true, message: 'Please input subject code!' }]"
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
const subjects = ref([]);
const loading = ref(false);
const submitting = ref(false);
const showCreateModal = ref(false);
const editingSubject = ref(null);

// Form state
const formState = reactive({
  name: '',
  code: '',
  description: '',
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
  await loadSubjects();
});

// Load subjects
const loadSubjects = async () => {
  loading.value = true;
  try {
    const response = await adminService.getSubjects();
    subjects.value = response.data || [];
    pagination.value.total = response.total || subjects.value.length;
  } catch (error) {
    message.error('Failed to load subjects: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Edit subject
const editSubject = (subject) => {
  editingSubject.value = subject;
  Object.assign(formState, {
    name: subject.name,
    code: subject.code,
    description: subject.description,
    isActive: subject.isActive
  });
  showCreateModal.value = true;
};

// Delete subject
const deleteSubject = async (subjectId) => {
  try {
    await adminService.deleteSubject(subjectId);
    message.success('Subject deleted successfully!');
    await loadSubjects(); // Refresh the list
  } catch (error) {
    message.error('Failed to delete subject: ' + error.message);
  }
};

// Handle form submission
const handleSubmit = async () => {
  submitting.value = true;
  try {
    if (editingSubject.value) {
      // Update existing subject
      await adminService.updateSubject(editingSubject.value.id, { ...formState });
      message.success('Subject updated successfully!');
    } else {
      // Create new subject
      await adminService.createSubject({ ...formState });
      message.success('Subject created successfully!');
    }
    
    // Close modal and refresh list
    showCreateModal.value = false;
    editingSubject.value = null;
    resetForm();
    await loadSubjects();
  } catch (error) {
    message.error('Failed to save subject: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Handle modal cancel
const handleCancel = () => {
  showCreateModal.value = false;
  editingSubject.value = null;
  resetForm();
};

// Reset form
const resetForm = () => {
  Object.keys(formState).forEach(key => {
    if (key === 'isActive') {
      formState[key] = true;
    } else {
      formState[key] = '';
    }
  });
};
</script>

<style scoped>
.subject-management {
  padding: 20px;
}

.management-actions {
  margin-bottom: 20px;
}

.subject-list {
  margin-top: 20px;
}
</style>