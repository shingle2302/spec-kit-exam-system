<template>
  <div class="permission-manager">
    <a-card title="数据权限配置">
      <a-form :model="form" layout="vertical">
        <a-form-item label="角色">
          <a-select v-model="form.roleId" @change="loadPermissionConfig">
            <a-select-option v-for="role in roles" :key="role.id" :value="role.id">
              {{ role.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="权限类型">
          <a-radio-group v-model="form.permissionType">
            <a-radio value="ALL">全部数据</a-radio>
            <a-radio value="DEPT">本部门数据</a-radio>
            <a-radio value="CUSTOM">自定义数据</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item v-if="form.permissionType === 'DEPT'" label="选择部门">
          <a-tree-select
            v-model="form.deptIds"
            :tree-data="deptTree"
            multiple
            tree-checkable
            placeholder="请选择部门"
          />
        </a-form-item>

        <a-form-item v-if="form.permissionType === 'CUSTOM'" label="自定义SQL">
          <a-textarea
            v-model="form.customSql"
            placeholder="请输入自定义SQL条件"
            :rows="4"
          />
        </a-form-item>

        <a-form-item>
          <a-space>
            <a-button type="primary" @click="savePermission">保存配置</a-button>
            <a-button @click="resetForm">重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { roleService } from '../services/roleService';

interface Role {
  id: number;
  name: string;
}

interface DeptNode {
  title: string;
  value: string;
  key: string;
  children?: DeptNode[];
}

const form = ref({
  roleId: undefined,
  permissionType: 'ALL',
  deptIds: [],
  customSql: ''
});

const roles = ref<Role[]>([]);
const deptTree = ref<DeptNode[]>([]);

const loadPermissionConfig = async () => {
  try {
    if (form.value.roleId) {
      const config = await roleService.getPermissionConfig(form.value.roleId);
      form.value.permissionType = config.permissionType;
      form.value.deptIds = config.deptIds || [];
      form.value.customSql = config.customSql || '';
    }
  } catch (error) {
    message.error('加载权限配置失败');
  }
};

const savePermission = async () => {
  try {
    await roleService.savePermissionConfig(form.value);
    message.success('权限配置保存成功');
  } catch (error) {
    message.error('保存权限配置失败');
  }
};

const resetForm = () => {
  form.value = {
    roleId: undefined,
    permissionType: 'ALL',
    deptIds: [],
    customSql: ''
  };
};

onMounted(async () => {
  try {
    roles.value = await roleService.list();
    deptTree.value = [
      {
        title: '学校',
        value: 'school',
        key: 'school',
        children: [
          { title: '教务处', value: 'academic', key: 'academic' },
          { title: '学生处', value: 'student', key: 'student' },
          { title: '教师处', value: 'teacher', key: 'teacher' }
        ]
      }
    ];
  } catch (error) {
    message.error('加载数据失败');
  }
});
</script>

<style scoped>
.permission-manager {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

@media (max-width: 768px) {
  .permission-manager {
    padding: 16px;
  }
}
</style>