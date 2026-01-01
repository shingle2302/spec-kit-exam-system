package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("answer_options")
public class AnswerOption extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("question_id")
    private Long questionId;
    
    @TableField("option_text")
    private String optionText;
    
    @TableField("option_index")
    private Integer optionIndex;
    
    @TableField("is_correct")
    private Boolean isCorrect;
    
    @TableField("created_at")
    private LocalDateTime createdAt;

    public AnswerOption() {
        this.createdAt = LocalDateTime.now();
    }



    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Integer getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(Integer optionIndex) {
        this.optionIndex = optionIndex;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}