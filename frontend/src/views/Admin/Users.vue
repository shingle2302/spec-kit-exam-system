<template>
  <div>
    <a-page-header
      title="User Management"
      sub-title="Manage system users"
      @back="() => $router.push('/admin/dashboard')"
    >
      <template #extra>
        <a-button @click="fetchUsers" type="primary">
          <template #icon><ReloadOutlined /></template>
          Refresh
        </a-button>
        <a-button type="primary" @click="showCreateModal">
          <template #icon><PlusOutlined /></template>
          Create User
        </a-button>
      </template>
    </a-page-header>
    
    <a-card title="Users">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <a-space>
          <a-button @click="fetchUsers">
            <template #icon><ReloadOutlined /></template>
            Refresh
          </a-button>
          <a-select v-model:value="filterRole" placeholder="Filter by role" style="width: 150px;" @change="fetchUsers">
            <a-select-option value="">All Roles</a-select-option>
            <a-select-option value="STUDENT">Student</a-select-option>
            <a-select-option value="TEACHER">Teacher</a-select-option>
            <a-select-option value="ADMIN">Admin</a-select-option>
          </a-select>
        </a-space>
        <a-input-search
          v-model:value="searchText"
          placeholder="Search users..."
          style="width: 300px;"
          @search="handleSearch"
        />
      </div>
      
      <a-table 
        :columns="userColumns" 
        :data-source="users" 
        :pagination="{ pageSize: 10 }"
        row-key="id"
        :loading="loading"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'role'">
            <a-tag :color="getRoleColor(record.role)">
              {{ record.role }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="editUser(record)">Edit</a-button>
              <a-button type="link" size="small" danger @click="deleteUser(record)">Delete</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
    
    <!-- Create/Edit User Modal -->
    <a-modal
      v-model:visible="modalVisible"
      :title="editingUser ? 'Edit User' : 'Create User'"
      :footer="null"
      :width="600"
      :destroy-on-close="true"
      @cancel="cancelUser"
    >
      <a-form :model="formState" @finish="onFinish" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item 
          label="Username" 
          name="username"
          :rules="[{ required: true, message: 'Please input the username!' }]"
        >
          <a-input v-model:value="formState.username" placeholder="Enter username" />
        </a-form-item>
        
        <a-form-item 
          label="Email" 
          name="email"
          :rules="[{ required: true, message: 'Please input the email!' }]"
        >
          <a-input v-model:value="formState.email" placeholder="Enter email" />
        </a-form-item>
        
        <a-form-item 
          label="First Name" 
          name="firstName"
          :rules="[{ required: true, message: 'Please input the first name!' }]"
        >
          <a-input v-model:value="formState.firstName" placeholder="Enter first name" />
        </a-form-item>
        
        <a-form-item 
          label="Last Name" 
          name="lastName"
          :rules="[{ required: true, message: 'Please input the last name!' }]"
        >
          <a-input v-model:value="formState.lastName" placeholder="Enter last name" />
        </a-form-item>
        
        <a-form-item 
          label="Role" 
          name="role"
          :rules="[{ required: true, message: 'Please select a role!' }]"
        >
          <a-select v-model:value="formState.role" placeholder="Select role">
            <a-select-option value="STUDENT">Student</a-select-option>
            <a-select-option value="TEACHER">Teacher</a-select-option>
            <a-select-option value="ADMIN">Admin</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item 
          v-if="!editingUser"
          label="Password" 
          name="password"
          :rules="[{ required: !editingUser, message: 'Please input the password!' }]"
        >
          <a-input-password v-model:value="formState.password" placeholder="Enter password" />
        </a-form-item>
        
        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center;">
          <a-button type="primary" html-type="submit" :loading="submitting">
            {{ editingUser ? 'Update' : 'Create' }} User
          </a-button>
          <a-button @click="cancelUser" style="margin-left: 10px;">
            Cancel
          </a-button>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { 
  ReloadOutlined, 
  PlusOutlined 
} from '@ant-design/icons-vue';
import adminService from '@/services/adminService';

export default {
  name: 'AdminUserManagement',
  components: {
    ReloadOutlined,
    PlusOutlined
  },
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const users = ref([]);
    const searchText = ref('');
    const filterRole = ref('');
    const modalVisible = ref(false);
    const editingUser = ref(null);
    const submitting = ref(false);
    const user = ref(null);
    
    // Form state
    const formState = reactive({
      id: null,
      username: '',
      email: '',
      firstName: '',
      lastName: '',
      role: 'STUDENT',
      password: ''
    });
    
    // Load user from localStorage
    onMounted(() => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
        fetchUsers();
      } else {
        router.push('/login');
      }
    });
    
    const userColumns = [
      {
        title: 'Username',
        dataIndex: 'username',
        key: 'username',
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
        title: 'Role',
        dataIndex: 'role',
        key: 'role',
      },
      {
        title: 'Created At',
        dataIndex: 'createdAt',
        key: 'createdAt',
        customRender: ({ text }) => new Date(text).toLocaleDateString()
      },
      {
        title: 'Action',
        key: 'action',
        width: '150px'
      },
    ];
    
    const fetchUsers = async () => {
      loading.value = true;
      try {
        // In real implementation, we'd call adminService.getUsers()
        // For now, we'll use mock data
        users.value = [
          { id: '1', username: 'john_doe', email: 'john@example.com', firstName: 'John', lastName: 'Doe', role: 'STUDENT', createdAt: new Date().toISOString() },
          { id: '2', username: 'jane_smith', email: 'jane@example.com', firstName: 'Jane', lastName: 'Smith', role: 'TEACHER', createdAt: new Date(Date.now() - 86400000).toISOString() },
          { id: '3', username: 'admin_user', email: 'admin@example.com', firstName: 'Admin', lastName: 'User', role: 'ADMIN', createdAt: new Date(Date.now() - 172800000).toISOString() }
        ];
      } catch (error) {
        console.error('Error fetching users:', error);
        message.error('Failed to fetch users');
      } finally {
        loading.value = false;
      }
    };
    
    const getRoleColor = (role) => {
      const colors = {
        'STUDENT': '#1890ff',
        'TEACHER': '#52c41a',
        'ADMIN': '#722ed1'
      };
      return colors[role] || '#8c8c8c';
    };
    
    const showCreateModal = () => {
      resetForm();
      modalVisible.value = true;
    };
    
    const editUser = (user) => {
      editingUser.value = user;
      Object.assign(formState, {
        id: user.id,
        username: user.username,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        role: user.role
      });
      modalVisible.value = true;
    };
    
    const deleteUser = async (user) => {
      try {
        // In real implementation, we'd call adminService.deleteUser(user.id)
        message.success('User deleted successfully');
        fetchUsers();
      } catch (error) {
        console.error('Error deleting user:', error);
        message.error('Failed to delete user');
      }
    };
    
    const onFinish = async () => {
      submitting.value = true;
      try {
        if (editingUser.value) {
          // In real implementation, we'd call adminService.updateUser(formState.id, formState)
          message.success('User updated successfully');
        } else {
          // In real implementation, we'd call adminService.createUser(formState)
          message.success('User created successfully');
        }
        
        modalVisible.value = false;
        fetchUsers();
      } catch (error) {
        console.error('Error saving user:', error);
        message.error(error.response?.data?.message || 'Failed to save user');
      } finally {
        submitting.value = false;
      }
    };
    
    const cancelUser = () => {
      modalVisible.value = false;
      editingUser.value = null;
      resetForm();
    };
    
    const resetForm = () => {
      formState.id = null;
      formState.username = '';
      formState.email = '';
      formState.firstName = '';
      formState.lastName = '';
      formState.role = 'STUDENT';
      formState.password = '';
    };
    
    const handleSearch = () => {
      // Simple client-side search
      if (searchText.value) {
        users.value = users.value.filter(user => 
          user.username.toLowerCase().includes(searchText.value.toLowerCase()) ||
          user.email.toLowerCase().includes(searchText.value.toLowerCase()) ||
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchText.value.toLowerCase())
        );
      } else {
        fetchUsers();
      }
    };
    
    return {
      loading,
      users,
      searchText,
      filterRole,
      modalVisible,
      editingUser,
      submitting,
      user,
      formState,
      userColumns,
      fetchUsers,
      getRoleColor,
      showCreateModal,
      editUser,
      deleteUser,
      onFinish,
      cancelUser,
      handleSearch
    };
  }
};
</script>