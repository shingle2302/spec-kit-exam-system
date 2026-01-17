<template>
  <div class="role-permission-editor">
    <div class="role-header">
      <h3>Managing permissions for: {{ roleName }}</h3>
    </div>

    <div class="filter-bar">
      <a-input-search
        v-model:value="searchValue"
        placeholder="Search permissions..."
        style="width: 300px;"
        @change="onSearch"
      />
      <a-button @click="refreshPermissions" style="margin-left: 8px;">
        <template #icon>
          <ReloadOutlined />
        </template>
        Refresh
      </a-button>
    </div>

    <div class="permission-grid">
      <a-spin :spinning="loading">
        <a-table
          :columns="columns"
          :data-source="filteredPermissions"
          :row-key="record => record.id"
          :pagination="{ pageSize: 10 }"
          :row-selection="rowSelection"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'menuName'">
              <strong>{{ record.menuName }}</strong>
            </template>
            <template v-else-if="column.key === 'operationType'">
              <a-tag :color="getOperationColor(record.operationType)">
                {{ record.operationType }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="record.status === 'ACTIVE' ? 'green' : 'red'">
                {{ record.status }}
              </a-tag>
            </template>
          </template>
        </a-table>
      </a-spin>
    </div>

    <div class="action-bar">
      <a-button type="primary" @click="savePermissions" :disabled="!hasChanges">
        Save Changes
      </a-button>
      <a-button @click="resetChanges" style="margin-left: 8px;">
        Reset
      </a-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Table as ATable, Input as AInput, Button as AButton, 
         Tag as ATag, Spin as ASpin, message } from 'ant-design-vue';
import { ReloadOutlined } from '@ant-design/icons-vue';
import { permissionService } from '@/services/permissionService';

// Props
const props = defineProps({
  roleId: {
    type: String,
    required: true
  },
  roleName: {
    type: String,
    required: true
  }
});

// Reactive data
const searchValue = ref('');
const allPermissions = ref([]);
const assignedPermissions = ref(new Set());
const loading = ref(false);

// Computed properties
const filteredPermissions = computed(() => {
  if (!searchValue.value) {
    return allPermissions.value.map(perm => ({
      ...perm,
      checked: assignedPermissions.value.has(perm.id)
    }));
  }

  const searchLower = searchValue.value.toLowerCase();
  return allPermissions.value
    .filter(perm => 
      perm.menuName.toLowerCase().includes(searchLower) ||
      perm.buttonName.toLowerCase().includes(searchLower) ||
      perm.operationType.toLowerCase().includes(searchLower) ||
      perm.description.toLowerCase().includes(searchLower)
    )
    .map(perm => ({
      ...perm,
      checked: assignedPermissions.value.has(perm.id)
    }));
});

const hasChanges = computed(() => {
  // Check if current selection differs from original assigned permissions
  const currentIds = new Set(filteredPermissions.value.filter(p => p.checked).map(p => p.id));
  const originalIds = assignedPermissions.value;
  
  if (currentIds.size !== originalIds.size) return true;
  
  for (const id of currentIds) {
    if (!originalIds.has(id)) return true;
  }
  
  return false;
});

// Table columns
const columns = [
  {
    title: 'Menu',
    dataIndex: 'menuName',
    key: 'menuName',
    slots: { customRender: 'menuName' },
  },
  {
    title: 'Button/Action',
    dataIndex: 'buttonName',
    key: 'buttonName',
  },
  {
    title: 'Operation',
    dataIndex: 'operationType',
    key: 'operationType',
    slots: { customRender: 'operationType' },
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
  }
];

// Row selection configuration
const rowSelection = {
  selectedRowKeys: computed(() => Array.from(assignedPermissions.value)),
  onChange: (selectedRowKeys) => {
    assignedPermissions.value = new Set(selectedRowKeys);
  },
  getCheckboxProps: (record) => ({
    disabled: record.status !== 'ACTIVE',
    name: record.name,
  }),
};

// Methods
const getOperationColor = (operationType) => {
  switch (operationType) {
    case 'QUERY': return 'blue';
    case 'CREATE': return 'green';
    case 'UPDATE': return 'orange';
    case 'DELETE': return 'red';
    case 'UNLOCK': return 'gold';
    case 'LOCK': return 'volcano';
    default: return 'default';
  }
};

const loadPermissions = async () => {
  loading.value = true;
  try {
    // Load all available permissions
    const allPermsResponse = await permissionService.getAllPermissions();
    allPermissions.value = allPermsResponse.data || [];
    
    // Load permissions already assigned to this role
    const assignedPermsResponse = await permissionService.getPermissionsByRole(props.roleId);
    const assignedPermIds = new Set(assignedPermsResponse.data?.map(p => p.id) || []);
    assignedPermissions.value = assignedPermIds;
  } catch (error) {
    console.error('Error loading permissions:', error);
    message.error('Failed to load permissions');
  } finally {
    loading.value = false;
  }
};

const onSearch = () => {
  // Filtering is handled by the computed property
  console.log('Searching for:', searchValue.value);
};

const refreshPermissions = () => {
  loadPermissions();
};

const savePermissions = async () => {
  try {
    // Get the list of currently selected permission IDs
    const currentPermissionIds = Array.from(assignedPermissions.value);
    
    // Call the service to assign these permissions to the role
    await permissionService.assignPermissionsToRole(props.roleId, currentPermissionIds);
    
    message.success('Permissions saved successfully');
  } catch (error) {
    console.error('Error saving permissions:', error);
    message.error('Failed to save permissions');
  }
};

const resetChanges = () => {
  // Reload the permissions to reset to the original state
  loadPermissions();
};

// Lifecycle
onMounted(() => {
  loadPermissions();
});
</script>

<style scoped>
.role-permission-editor {
  padding: 16px;
}

.role-header {
  margin-bottom: 16px;
}

.filter-bar {
  margin-bottom: 16px;
  display: flex;
}

.permission-grid {
  margin-bottom: 16px;
}

.action-bar {
  text-align: right;
}
</style>