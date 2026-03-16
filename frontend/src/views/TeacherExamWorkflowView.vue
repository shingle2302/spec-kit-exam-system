<template>
  <div class="teacher-exam-workflow">
    <a-card class="main-card">
      <template #title>
        <div class="card-title">
          <FormOutlined class="title-icon" />
          <span>教师工作台</span>
        </div>
      </template>
      
      <a-space direction="vertical" style="width: 100%" class="content-space">
        <a-card size="small" class="workflow-card">
          <template #title>
            <div class="section-title">
              <FileTextOutlined class="section-icon" />
              <span>命题与组卷</span>
            </div>
          </template>
          <a-space class="form-space">
            <a-input 
              v-model:value="paper.name" 
              placeholder="试卷名称" 
              class="form-input"
            >
              <template #prefix>
                <EditOutlined />
              </template>
            </a-input>
            <a-input-number 
              v-model:value="paper.subjectId" 
              placeholder="学科ID" 
              class="form-input"
            >
              <template #prefix>
                <BookOutlined />
              </template>
            </a-input-number>
            <a-input-number 
              v-model:value="paper.classId" 
              placeholder="班级ID" 
              class="form-input"
            >
              <template #prefix>
                <TeamOutlined />
              </template>
            </a-input-number>
            <a-button type="primary" @click="createPaper" class="action-button">
              <template #icon>
                <PlusOutlined />
              </template>
              创建试卷
            </a-button>
          </a-space>
        </a-card>
        
        <a-card size="small" class="workflow-card">
          <template #title>
            <div class="section-title">
              <EyeOutlined class="section-icon" />
              <span>监考与阅卷</span>
            </div>
          </template>
          <a-space wrap class="form-space">
            <a-input-number 
              v-model:value="sessionId" 
              placeholder="考试场次ID" 
              class="form-input"
            >
              <template #prefix>
                <NumberOutlined />
              </template>
            </a-input-number>
            <a-input 
              v-model:value="teacherId" 
              placeholder="教师ID" 
              class="form-input"
            >
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
            <a-button @click="startInvigilation" class="action-button">
              <template #icon>
                <PlayCircleOutlined />
              </template>
              开始监考
            </a-button>
            <a-button @click="endInvigilation" class="action-button">
              <template #icon>
                <StopOutlined />
              </template>
              结束监考
            </a-button>
            <a-input-number 
              v-model:value="submissionId" 
              placeholder="答卷ID" 
              class="form-input"
            >
              <template #prefix>
                <FileTextOutlined />
              </template>
            </a-input-number>
            <a-input-number 
              v-model:value="score" 
              placeholder="分数" 
              class="form-input"
            >
              <template #prefix>
                <StarOutlined />
              </template>
            </a-input-number>
            <a-button type="primary" @click="grade" class="action-button">
              <template #icon>
                <CheckCircleOutlined />
              </template>
              阅卷打分
            </a-button>
          </a-space>
        </a-card>
        
        <a-alert 
          :message="result" 
          type="info" 
          show-icon 
          class="result-alert"
        >
          <template #icon>
            <InfoCircleOutlined />
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
  FormOutlined, 
  FileTextOutlined, 
  EditOutlined, 
  BookOutlined, 
  TeamOutlined, 
  PlusOutlined,
  EyeOutlined,
  NumberOutlined,
  UserOutlined,
  PlayCircleOutlined,
  StopOutlined,
  StarOutlined,
  CheckCircleOutlined,
  InfoCircleOutlined
} from '@ant-design/icons-vue'

const paper = ref({ name: '', subjectId: 1, classId: 1 })
const sessionId = ref<number | null>(null)
const teacherId = ref('')
const submissionId = ref<number | null>(null)
const score = ref<number | null>(null)
const result = ref('等待操作')

const createPaper = async () => {
  const data = await examWorkflowService.createPaper(paper.value)
  result.value = `试卷创建成功，ID=${data.id}`
}
const startInvigilation = async () => {
  if (!sessionId.value) return
  const data = await examWorkflowService.startInvigilation(sessionId.value, teacherId.value)
  result.value = `监考开始，记录ID=${data.id}`
}
const endInvigilation = async () => {
  if (!sessionId.value) return
  const data = await examWorkflowService.endInvigilation(sessionId.value, teacherId.value)
  result.value = `监考结束，状态=${data.status}`
}
const grade = async () => {
  if (!submissionId.value || score.value === null) return
  const data = await examWorkflowService.grade(submissionId.value, score.value, teacherId.value)
  result.value = `阅卷完成，分数=${data.score}`
}
</script>

<style scoped>
.teacher-exam-workflow {
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

.workflow-card {
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

.action-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.result-alert {
  border-radius: 8px;
  padding: 16px 20px;
  font-size: 15px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .teacher-exam-workflow {
    padding: 12px;
  }

  .form-space {
    flex-direction: column;
    align-items: stretch;
  }

  .form-input,
  .action-button {
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