<template>
  <div class="academic-exam-ops">
    <a-card class="main-card">
      <template #title>
        <div class="card-title">
          <DashboardOutlined class="title-icon" />
          <span>教务运营</span>
        </div>
      </template>
      
      <a-space direction="vertical" style="width:100" class="content-space">
        <a-card size="small" class="ops-card">
          <template #title>
            <div class="section-title">
              <CalendarOutlined class="section-icon" />
              <span>排考</span>
            </div>
          </template>
          <a-space class="form-space">
            <a-input-number 
              v-model:value="schedule.paperId" 
              placeholder="试卷ID" 
              class="form-input"
            >
              <template #prefix>
                <FileTextOutlined />
              </template>
            </a-input-number>
            <a-input-number 
              v-model:value="schedule.classId" 
              placeholder="班级ID" 
              class="form-input"
            >
              <template #prefix>
                <TeamOutlined />
              </template>
            </a-input-number>
            <a-input-number 
              v-model:value="schedule.subjectId" 
              placeholder="学科ID" 
              class="form-input"
            >
              <template #prefix>
                <BookOutlined />
              </template>
            </a-input-number>
            <a-date-picker 
              v-model:value="startMoment" 
              show-time 
              class="form-input"
            >
              <template #suffixIcon>
                <ClockCircleOutlined />
              </template>
            </a-date-picker>
            <a-date-picker 
              v-model:value="endMoment" 
              show-time 
              class="form-input"
            >
              <template #suffixIcon>
                <ClockCircleOutlined />
              </template>
            </a-date-picker>
            <a-button type="primary" @click="createSession" class="action-button">
              <template #icon>
                <PlusOutlined />
              </template>
              创建场次
            </a-button>
          </a-space>
        </a-card>
        
        <a-card size="small" class="ops-card">
          <template #title>
            <div class="section-title">
              <RiseOutlined class="section-icon" />
              <span>发布与分析</span>
            </div>
          </template>
          <a-space class="form-space">
            <a-input-number 
              v-model:value="sessionId" 
              placeholder="场次ID" 
              class="form-input"
            >
              <template #prefix>
                <NumberOutlined />
              </template>
            </a-input-number>
            <a-button @click="publish" class="action-button">
              <template #icon>
                <SendOutlined />
              </template>
              发布成绩
            </a-button>
            <a-button type="primary" @click="analysis" class="action-button">
              <template #icon>
                <LineChartOutlined />
              </template>
              生成分析
            </a-button>
            <a-button @click="loadDashboard" class="action-button">
              <template #icon>
                <ReloadOutlined />
              </template>
              刷新看板
            </a-button>
          </a-space>
        </a-card>
        
        <a-card size="small" class="dashboard-card">
          <template #title>
            <div class="section-title">
              <BarChartOutlined class="section-icon" />
              <span>运营看板</span>
            </div>
          </template>
          <a-descriptions bordered size="small" :column="4" class="dashboard-descriptions">
            <a-descriptions-item label="试卷数">
              <div class="dashboard-value">
                <FileTextOutlined class="dashboard-icon" />
                {{ dashboard.papers ?? '-' }}
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="场次数">
              <div class="dashboard-value">
                <CalendarOutlined class="dashboard-icon" />
                {{ dashboard.sessions ?? '-' }}
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="答卷数">
              <div class="dashboard-value">
                <FileDoneOutlined class="dashboard-icon" />
                {{ dashboard.submissions ?? '-' }}
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="分析报告">
              <div class="dashboard-value">
                <LineChartOutlined class="dashboard-icon" />
                {{ dashboard.analyses ?? '-' }}
              </div>
            </a-descriptions-item>
          </a-descriptions>
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
import dayjs from 'dayjs'
import { examWorkflowService } from '@/services/examWorkflowService'
import { 
  DashboardOutlined, 
  CalendarOutlined, 
  RiseOutlined, 
  BarChartOutlined,
  FileTextOutlined,
  TeamOutlined,
  BookOutlined,
  ClockCircleOutlined,
  PlusOutlined,
  NumberOutlined,
  SendOutlined,
  LineChartOutlined,
  ReloadOutlined,
  FileDoneOutlined,
  InfoCircleOutlined
} from '@ant-design/icons-vue'

const schedule = ref({ paperId: 1, classId: 1, subjectId: 1 })
const startMoment = ref(dayjs())
const endMoment = ref(dayjs().add(2, 'hour'))
const sessionId = ref<number | null>(null)
const dashboard = ref<any>({})
const result = ref('等待操作')

const createSession = async () => {
  const payload = {
    ...schedule.value,
    startTime: startMoment.value.toISOString(),
    endTime: endMoment.value.toISOString()
  }
  const data = await examWorkflowService.scheduleSession(payload)
  result.value = `排考成功，场次ID=${data.id}`
}

const publish = async () => {
  if (!sessionId.value) return
  const data = await examWorkflowService.publish(sessionId.value)
  result.value = `成绩发布完成，状态=${data.status}`
}

const analysis = async () => {
  if (!sessionId.value) return
  const data = await examWorkflowService.analysis(sessionId.value)
  result.value = `分析完成：均分${data.avgScore}，及格率${data.passRate}%`
}

const loadDashboard = async () => {
  dashboard.value = await examWorkflowService.dashboard()
}
</script>

<style scoped>
.academic-exam-ops {
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

.ops-card,
.dashboard-card {
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

.dashboard-descriptions {
  border-radius: 6px;
  overflow: hidden;
}

.dashboard-value {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #1f2937;
}

.dashboard-icon {
  color: #6b7280;
  font-size: 16px;
}

.result-alert {
  border-radius: 8px;
  padding: 16px 20px;
  font-size: 15px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .academic-exam-ops {
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

  .dashboard-descriptions {
    :deep(.ant-descriptions-item-label) {
      padding: 8px;
    }
    :deep(.ant-descriptions-item-content) {
      padding: 8px;
    }
  }
}

@media (max-width: 576px) {
  .card-title {
    font-size: 16px;
  }

  .title-icon {
    font-size: 18px;
  }

  .dashboard-descriptions {
    :deep(.ant-descriptions-item) {
      display: block;
    }
  }
}
</style>