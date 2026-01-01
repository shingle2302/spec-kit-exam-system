package com.exam.system.controller;

import com.exam.system.service.StudentTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/students")
public class StudentTransferController {

    @Autowired
    private StudentTransferService studentTransferService;

    @PostMapping("/{studentId}/transfer")
    public ResponseEntity<?> transferStudent(@PathVariable Long studentId, @RequestBody TransferRequest request) {
        boolean success = studentTransferService.transferStudent(studentId, request.getNewClassId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Student transferred successfully" : "Failed to transfer student");
        
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Inner class for transfer request
    public static class TransferRequest {
        private Long newClassId;

        public Long getNewClassId() {
            return newClassId;
        }

        public void setNewClassId(Long newClassId) {
            this.newClassId = newClassId;
        }
    }
}