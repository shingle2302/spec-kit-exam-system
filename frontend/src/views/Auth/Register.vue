<template>
  <div class="register-container">
    <a-card style="max-width: 500px; margin: 50px auto; padding: 20px;">
      <h2 style="text-align: center; margin-bottom: 30px;">Register New Account</h2>
      <a-form :model="formState" @finish="onFinish" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-form-item 
          label="Username" 
          name="username"
          :rules="[{ required: true, message: 'Please input your username!' }]"
        >
          <a-input v-model:value="formState.username" placeholder="Enter username" />
        </a-form-item>
        
        <a-form-item 
          label="Email" 
          name="email"
          :rules="[{ required: true, message: 'Please input your email!' }]"
        >
          <a-input v-model:value="formState.email" placeholder="Enter email" />
        </a-form-item>
        
        <a-form-item 
          label="Full Name" 
          name="fullName"
          :rules="[{ required: true, message: 'Please input your full name!' }]"
        >
          <a-input v-model:value="formState.fullName" placeholder="Enter full name" />
        </a-form-item>
        
        <a-form-item 
          label="Role" 
          name="role"
          :rules="[{ required: true, message: 'Please select your role!' }]"
        >
          <a-select v-model:value="formState.role" placeholder="Select role">
            <a-select-option value="STUDENT">Student</a-select-option>
            <a-select-option value="TEACHER">Teacher</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item 
          label="Password" 
          name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]"
        >
          <a-input-password v-model:value="formState.password" placeholder="Enter password" />
        </a-form-item>
        
        <a-form-item 
          label="Confirm Password" 
          name="confirmPassword"
          :rules="[{ required: true, message: 'Please confirm your password!' }]"
        >
          <a-input-password v-model:value="formState.confirmPassword" placeholder="Confirm password" />
        </a-form-item>
        
        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center;">
          <a-button type="primary" html-type="submit" :loading="loading" style="margin-right: 10px;">
            Register
          </a-button>
          <a-button @click="resetForm">
            Reset
          </a-button>
          <div style="margin-top: 15px;">
            <span>Already have an account? </span>
            <a @click="goToLogin">Login here</a>
          </div>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import axios from 'axios';

export default {
  name: 'Register',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    
    const formState = reactive({
      username: '',
      email: '',
      fullName: '',
      role: 'STUDENT',
      password: '',
      confirmPassword: ''
    });
    
    const onFinish = async (values) => {
      if (values.password !== values.confirmPassword) {
        message.error('Passwords do not match!');
        return;
      }
      
      loading.value = true;
      try {
        const response = await axios.post('/api/v1/auth/register', {
          username: values.username,
          email: values.email,
          firstName: values.fullName.split(' ')[0] || values.fullName,
          lastName: values.fullName.split(' ').slice(1).join(' ') || '',
          role: values.role,
          password: values.password
        });
        
        message.success('Registration successful! Please login.');
        router.push('/login');
      } catch (error) {
        console.error('Registration error:', error);
        message.error(error.response?.data?.message || 'Registration failed. Please try again.');
      } finally {
        loading.value = false;
      }
    };
    
    const resetForm = () => {
      formState.username = '';
      formState.email = '';
      formState.fullName = '';
      formState.role = 'STUDENT';
      formState.password = '';
      formState.confirmPassword = '';
    };
    
    const goToLogin = () => {
      router.push('/login');
    };
    
    return {
      formState,
      loading,
      onFinish,
      resetForm,
      goToLogin
    };
  }
};
</script>

<style scoped>
.register-container {
  background: #f0f2f5;
  height: 100vh;
  display: flex;
  align-items: center;
}
</style>