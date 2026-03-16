<template>
  <div class="student-exam-center">
    <a-card class="main-card">
      <template #title>
        <div class="card-title">
          <ReadOutlined class="title-icon" />
          <span>学生考试中心</span>
        </div>
      </template>
      
      <a-space direction="vertical" style="width:100" class="content-space">
        <a-card size="small" class="submit-card">
          <template #title>
            <div class="section-title">
              <EditOutlined class="section-icon" />
              <span>提交答卷</span>
            </div>
          </template>
          <a-space class="form-space">
            <a-input-number 
              v-model:value="form.examSessionId" 
              placeholder="场次ID" 
              class="form-input"
            >
              <template #prefix>
                <NumberOutlined />
              </template>
            </a-input-number>
            <a-input 
              v-model:value="form.studentId" 
              placeholder="学生ID" 
              class="form-input"
            >
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
            <a-input 
              v-model:value="form.answers" 
              placeholder="答题JSON" 
              style="width: 360px" 
              class="form-input"
            >
              <template #prefix>
                <CodeOutlined />
              </template>
            </a-input>
            <a-button type="primary" @click="submit" class="submit-button">
              <template #icon>
                <SendOutlined />
              </template>
              提交
            </a-button>
          </a-space>
        </a-card>
        
        <a-alert 
          :message="result" 
          type="success" 
          show-icon 
          class="result-alert"
        >
          <template #icon>
            <CheckCircleOutlined />
          </template>
        </a-alert>
      </a-space>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { examWorkflowService } from '@/services/examWorkflowService'
import { 
  ReadOutlined, 
  EditOutlined, 
  NumberOutlined, 
  UserOutlined, 
  CodeOutlined, 
  SendOutlined,
  CheckCircleOutlined
} from '@ant-design/icons-vue'

const form = ref({ examSessionId: 1, studentId: '', answers: '{"q1":"A"}' })
const result = ref('等待提交')

const submit = async () => {
  const data = await examWorkflowService.submit(form.value)
  result.value = `提交成功，答卷ID=${data.id}`
}
</script>

<style scoped>
.student-exam-center {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.main-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.title-icon {
  font-size: 20px;
  color: #1890ff;
}

.content-space {
  gap: 20px;
}

.submit-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #1f2937;
}

.section-icon {
  font-size: 16px;
  color: #10b981;
}

.form-space {
  width: 100%;
  flex-wrap: wrap;
  gap: 12px;
}

.form-input {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.form-input:hover {
  border-color: #667eea;
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.submit-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.result-alert {
  border-radius: 8px;
  padding: 16px 20px;
  font-size: 15px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .student-exam-center {
    padding: 12px;
  }

  .form-space {
    flex-direction: column;
    align-items: stretch;
  }

  .form-input,
  .submit-button {
    width: 100%;
  }
}

@media (max-width: 576px) {
  .card-title {
    font-size: 16px;
  }

  .title-icon {
    font-size: 18px;
  }
}
</style>