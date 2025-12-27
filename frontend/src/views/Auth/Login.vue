<template>
  <div class="login-container">
    <a-card style="max-width: 400px; margin: 100px auto; padding: 20px;">
      <h2 style="text-align: center; margin-bottom: 30px;">Login to Exam System</h2>
      <a-form :model="formState" @finish="onFinish" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item 
          label="Username" 
          name="username"
          :rules="[{ required: true, message: 'Please input your username!' }]"
        >
          <a-input v-model:value="formState.username" placeholder="Enter username" />
        </a-form-item>
        
        <a-form-item 
          label="Password" 
          name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]"
        >
          <a-input-password v-model:value="formState.password" placeholder="Enter password" />
        </a-form-item>
        
        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center;">
          <a-button type="primary" html-type="submit" :loading="loading" style="margin-right: 10px;">
            Login
          </a-button>
          <a-button @click="resetForm">
            Reset
          </a-button>
        </a-form-item>
      </a-form>
      
      <div style="text-align: center; margin-top: 20px;">
        <p>Try with test accounts:</p>
        <p>Student: student1 / password</p>
        <p>Teacher: teacher1 / password</p>
        <p>Admin: admin1 / password</p>
      </div>
    </a-card>
  </div>
</template>

<script>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import axios from 'axios';

export default {
  name: 'Login',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    
    const formState = reactive({
      username: '',
      password: ''
    });
    
    const onFinish = async (values) => {
      loading.value = true;
      try {
        const response = await axios.post('/api/v1/auth/login', {
          username: values.username,
          password: values.password
        });
        
        if (response.data && response.data.token) {
          // Store token in localStorage
          localStorage.setItem('token', response.data.token);
          localStorage.setItem('user', JSON.stringify(response.data.user));
          
          // Set authorization header for future requests
          axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
          
          message.success('Login successful!');
          
          // Redirect based on user role
          const userRole = response.data.user.role;
          if (userRole === 'STUDENT') {
            router.push('/student/dashboard');
          } else if (userRole === 'TEACHER') {
            router.push('/teacher/dashboard');
          } else if (userRole === 'ADMIN') {
            router.push('/admin/dashboard');
          } else {
            router.push('/');
          }
        } else {
          message.error('Login failed: Invalid response');
        }
      } catch (error) {
        console.error('Login error:', error);
        message.error(error.response?.data?.message || 'Login failed. Please try again.');
      } finally {
        loading.value = false;
      }
    };
    
    const resetForm = () => {
      formState.username = '';
      formState.password = '';
    };
    
    return {
      formState,
      loading,
      onFinish,
      resetForm
    };
  }
};
</script>

<style scoped>
.login-container {
  background: #f0f2f5;
  height: 100vh;
  display: flex;
  align-items: center;
}
</style>