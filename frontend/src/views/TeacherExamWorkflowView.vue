<template>
  <a-card title="教师工作台（命题/监考/阅卷）">
    <a-space direction="vertical" style="width: 100%">
      <a-card size="small" title="命题与组卷">
        <a-space>
          <a-input v-model:value="paper.name" placeholder="试卷名称" />
          <a-input-number v-model:value="paper.subjectId" placeholder="学科ID" />
          <a-input-number v-model:value="paper.classId" placeholder="班级ID" />
          <a-button type="primary" @click="createPaper">创建试卷</a-button>
        </a-space>
      </a-card>
      <a-card size="small" title="监考与阅卷">
        <a-space wrap>
          <a-input-number v-model:value="sessionId" placeholder="考试场次ID" />
          <a-input v-model:value="teacherId" placeholder="教师ID" />
          <a-button @click="startInvigilation">开始监考</a-button>
          <a-button @click="endInvigilation">结束监考</a-button>
          <a-input-number v-model:value="submissionId" placeholder="答卷ID" />
          <a-input-number v-model:value="score" placeholder="分数" />
          <a-button type="primary" @click="grade">阅卷打分</a-button>
        </a-space>
      </a-card>
      <a-alert :message="result" type="info" show-icon />
    </a-space>
  </a-card>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { examWorkflowService } from '@/services/examWorkflowService'

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
