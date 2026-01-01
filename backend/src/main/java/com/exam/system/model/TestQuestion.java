package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("t_test_question")
public class TestQuestion extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("test_id")
    private Long testId;
    
    @TableField("question_id")
    private Long questionId;
    
    @TableField("question_order")
    private Integer order; // Order of question in test
    
    @TableField("points")
    private Integer points; // Points for this question

    public TestQuestion() {
        // Default constructor
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}