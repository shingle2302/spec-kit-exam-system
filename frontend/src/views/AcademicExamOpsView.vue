<template>
  <a-card title="教务运营（排考/发布/分析）">
    <a-space direction="vertical" style="width:100%">
      <a-card size="small" title="排考">
        <a-space>
          <a-input-number v-model:value="schedule.paperId" placeholder="试卷ID" />
          <a-input-number v-model:value="schedule.classId" placeholder="班级ID" />
          <a-input-number v-model:value="schedule.subjectId" placeholder="学科ID" />
          <a-date-picker v-model:value="startMoment" show-time />
          <a-date-picker v-model:value="endMoment" show-time />
          <a-button type="primary" @click="createSession">创建场次</a-button>
        </a-space>
      </a-card>
      <a-card size="small" title="发布与分析">
        <a-space>
          <a-input-number v-model:value="sessionId" placeholder="场次ID" />
          <a-button @click="publish">发布成绩</a-button>
          <a-button type="primary" @click="analysis">生成分析</a-button>
          <a-button @click="loadDashboard">刷新看板</a-button>
        </a-space>
      </a-card>
      <a-descriptions bordered size="small" :column="4" title="运营看板">
        <a-descriptions-item label="试卷数">{{ dashboard.papers ?? '-' }}</a-descriptions-item>
        <a-descriptions-item label="场次数">{{ dashboard.sessions ?? '-' }}</a-descriptions-item>
        <a-descriptions-item label="答卷数">{{ dashboard.submissions ?? '-' }}</a-descriptions-item>
        <a-descriptions-item label="分析报告">{{ dashboard.analyses ?? '-' }}</a-descriptions-item>
      </a-descriptions>
      <a-alert :message="result" type="info" show-icon />
    </a-space>
  </a-card>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import dayjs from 'dayjs'
import { examWorkflowService } from '@/services/examWorkflowService'

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
