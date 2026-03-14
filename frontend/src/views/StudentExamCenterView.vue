<template>
  <a-card title="学生考试中心（答题/成绩）">
    <a-space direction="vertical" style="width:100%">
      <a-card size="small" title="提交答卷">
        <a-space>
          <a-input-number v-model:value="form.examSessionId" placeholder="场次ID" />
          <a-input v-model:value="form.studentId" placeholder="学生ID" />
          <a-input v-model:value="form.answers" placeholder="答题JSON" style="width: 360px" />
          <a-button type="primary" @click="submit">提交</a-button>
        </a-space>
      </a-card>
      <a-alert :message="result" type="success" show-icon />
    </a-space>
  </a-card>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { examWorkflowService } from '@/services/examWorkflowService'

const form = ref({ examSessionId: 1, studentId: '', answers: '{"q1":"A"}' })
const result = ref('等待提交')

const submit = async () => {
  const data = await examWorkflowService.submit(form.value)
  result.value = `提交成功，答卷ID=${data.id}`
}
</script>
