<template>
  <div class="role-list">
    <div class="toolbar">
      <a-button type="primary" @click="addNewRole">
        <template #icon>
          <PlusOutlined />
        </template>
        Add Role
      </a-button>
      <a-input-search
        v-model:value="searchValue"
        placeholder="Search roles..."
        style="width: 300px; margin-left: 16px;"
        @change="onSearch"
      />
    </div>

    <a-table
      :columns="columns"
      :data-source="roles"
      :row-key="record => record.id"
      :pagination="{ pageSize: 10 }"
      :loading="loading"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 'ACTIVE' ? 'green' : 'red'">
            {{ record.status }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'actions'">
          <a-space>
            <a-button type="link" size="small" @click="editRole(record)">
              Edit
            </a-button>
            <a-button type="link" size="small" danger @click="deleteRole(record)">
              Delete
            </a-button>
            <a-button type="link" size="small" @click="managePermissions(record)">
              Permissions
            </a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- Role Modal -->
    <a-modal
      v-model:visible="modalVisible"
      :title="editingRole ? 'Edit Role' : 'Create Role'"
      @ok="handleOk"
      @cancel="handleCancel"
      :confirm-loading="modalConfirmLoading"
    >
      <a-form :model="roleForm" :rules="roleFormRules" ref="roleFormRef">
        <a-form-item label="Role Name" name="name">
          <a-input v-model:value="roleForm.name" placeholder="Enter role name" />
        </a-form-item>
        <a-form-item label="Role Code" name="code">
          <a-input v-model:value="roleForm.code" placeholder="Enter role code" />
        </a-form-item>
        <a-form-item label="Description" name="description">
          <a-textarea v-model:value="roleForm.description" placeholder="Enter role description" :rows="4" />
        </a-form-item>
        <a-form-item label="Status" name="status">
          <a-select v-model:value="roleForm.status" placeholder="Select status">
            <a-select-option value="ACTIVE">Active</a-select-option>
            <a-select-option value="INACTIVE">Inactive</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { Table as ATable, Button as AButton, Input as AInput, Modal as AModal, 
         Form as AForm, Tag as ATag, Space as ASpace, Select as ASelect, 
         Typography as ATypography } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { roleService } from '@/services/roleService';

// Reactive data
const searchValue = ref('');
const roles = ref([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalConfirmLoading = ref(false);
const editingRole = ref(null);

// Role form
const roleForm = reactive({
  name: '',
  code: '',
  description: '',
  status: 'ACTIVE'
});

const roleFormRef = ref();

// Form validation rules
const roleFormRules = {
  name: [
    { required: true, message: 'Please input role name!' },
    { min: 2, max: 50, message: 'Name must be between 2 and 50 characters', trigger: 'blur' }
  ],
  code: [
    { required: true, message: 'Please input role code!' },
    { pattern: /^[A-Z_]+$/, message: 'Role code must be uppercase letters and underscores only', trigger: 'blur' }
  ]
};

// Table columns
const columns = [
  {
    title: 'Role Name',
    dataIndex: 'name',
    key: 'name',
    sorter: (a, b) => a.name.localeCompare(b.name),
  },
  {
    title: 'Role Code',
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
    dataIndex: 'status',
    key: 'status',
    slots: { customRender: 'status' },
  },
  {
    title: 'Created At',
    dataIndex: 'createdAt',
    key: 'createdAt',
    sorter: (a, b) => new Date(a.createdAt) - new Date(b.createdAt),
  },
  {
    title: 'Actions',
    key: 'actions',
    slots: { customRender: 'actions' },
  }
];

// Methods
const loadRoles = async () => {
  loading.value = true;
  try {
    const response = await roleService.getRoles();
    roles.value = response.data || [];
  } catch (error) {
    console.error('Error loading roles:', error);
  } finally {
    loading.value = false;
  }
};

const addNewRole = () => {
  editingRole.value = null;
  Object.assign(roleForm, {
    name: '',
    code: '',
    description: '',
    status: 'ACTIVE'
  });
  modalVisible.value = true;
};

const editRole = (role) => {
  editingRole.value = role;
  Object.assign(roleForm, {
    name: role.name,
    code: role.code,
    description: role.description,
    status: role.status
  });
  modalVisible.value = true;
};

const deleteRole = async (role) => {
  if (window.confirm(`Are you sure you want to delete role "${role.name}"?`)) {
    try {
      await roleService.deleteRole(role.id);
      loadRoles(); // Refresh the list
    } catch (error) {
      console.error('Error deleting role:', error);
    }
  }
};

const managePermissions = (role) => {
  // Emit event to parent component to handle permission management
  emit('manage-permissions', role);
};

const handleOk = async () => {
  try {
    // Validate form
    await roleFormRef.value.validateFields();
    
    modalConfirmLoading.value = true;
    
    if (editingRole.value) {
      // Update existing role
      await roleService.updateRole(editingRole.value.id, roleForm);
    } else {
      // Create new role
      await roleService.createRole(roleForm);
    }
    
    modalVisible.value = false;
    loadRoles(); // Refresh the list
  } catch (error) {
    console.error('Error saving role:', error);
  } finally {
    modalConfirmLoading.value = false;
  }
};

const handleCancel = () => {
  modalVisible.value = false;
};

const onSearch = () => {
  // Implement search logic if needed
  console.log('Searching for:', searchValue.value);
};

const handleTableChange = (pagination, filters, sorter) => {
  console.log(pagination, filters, sorter);
};

// Emits
const emit = defineEmits(['manage-permissions']);

// Lifecycle
onMounted(() => {
  loadRoles();
});
</script>

<style scoped>
.role-list {
  padding: 16px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}
</style>