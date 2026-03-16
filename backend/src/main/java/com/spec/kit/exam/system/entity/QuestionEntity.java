package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试题实体类
 */
@Data
@TableName("questions")
public class QuestionEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private String type;

    private String difficulty;

    private Integer score;

    private String answer;

    private String analysis;

    private Long subjectId;

    private Long gradeId;

    private Long knowledgePointId;

    private String status;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}