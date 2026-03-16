package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.*;
import com.spec.kit.exam.system.service.ExamWorkflowService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-workflow")
public class ExamWorkflowController extends BaseController {
    @Autowired private ExamWorkflowService examWorkflowService;

    @PermissionRequired(menu = "exam-management", button = "create-paper", operation = "CREATE")
    @PostMapping("/papers")
    public Result<ExamPaper> createPaper(@RequestBody ExamPaper request) {
        return successCreate(examWorkflowService.createPaper(request));
    }

    @PermissionRequired(menu = "exam-management", button = "schedule-exam", operation = "CREATE")
    @PostMapping("/sessions")
    public Result<ExamSession> schedule(@RequestBody ExamSession request) {
        return successCreate(examWorkflowService.scheduleExam(request));
    }

    @PermissionRequired(menu = "exam-management", button = "start-invigilation", operation = "UPDATE")
    @PostMapping("/sessions/{id}/invigilation/start")
    public Result<ExamInvigilation> startInvigilation(@PathVariable("id") Long sessionId, @RequestParam String teacherId) {
        return successUpdate(examWorkflowService.startInvigilation(sessionId, teacherId));
    }

    @PermissionRequired(menu = "exam-management", button = "end-invigilation", operation = "UPDATE")
    @PostMapping("/sessions/{id}/invigilation/end")
    public Result<ExamInvigilation> endInvigilation(@PathVariable("id") Long sessionId, @RequestParam String teacherId) {
        return successUpdate(examWorkflowService.endInvigilation(sessionId, teacherId));
    }

    @PermissionRequired(menu = "exam-management", button = "submit-answer", operation = "CREATE")
    @PostMapping("/submissions")
    public Result<ExamSubmission> submit(@RequestBody ExamSubmission request) {
        return successCreate(examWorkflowService.submitAnswer(request));
    }

    @PermissionRequired(menu = "exam-management", button = "grade-paper", operation = "UPDATE")
    @PostMapping("/submissions/{id}/grade")
    public Result<ExamSubmission> grade(@PathVariable("id") Long submissionId,
                                        @RequestParam BigDecimal score,
                                        @RequestParam String gradedBy) {
        return successUpdate(examWorkflowService.gradeSubmission(submissionId, score, gradedBy));
    }

    @PermissionRequired(menu = "exam-management", button = "publish-score", operation = "UPDATE")
    @PostMapping("/sessions/{id}/publish")
    public Result<ExamSession> publish(@PathVariable("id") Long sessionId) {
        return successUpdate(examWorkflowService.publishScore(sessionId));
    }

    @PermissionRequired(menu = "exam-management", button = "analyze-exam", operation = "READ")
    @PostMapping("/sessions/{id}/analysis")
    public Result<ExamAnalysis> analysis(@PathVariable("id") Long sessionId) {
        return successDetail(examWorkflowService.generateAnalysis(sessionId));
    }

    @PermissionRequired(menu = "exam-management", button = "view-dashboard", operation = "READ")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return success(examWorkflowService.dashboard(), "仪表盘数据查询成功");
    }
}