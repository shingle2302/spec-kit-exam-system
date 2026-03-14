package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("exam_analyses")
public class ExamAnalysis {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long examSessionId;
    private BigDecimal avgScore;
    private BigDecimal passRate;
    private BigDecimal excellentRate;
    private Integer warningCount;
    private String recommendation;
    private LocalDateTime generatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExamSessionId() { return examSessionId; }
    public void setExamSessionId(Long examSessionId) { this.examSessionId = examSessionId; }
    public BigDecimal getAvgScore() { return avgScore; }
    public void setAvgScore(BigDecimal avgScore) { this.avgScore = avgScore; }
    public BigDecimal getPassRate() { return passRate; }
    public void setPassRate(BigDecimal passRate) { this.passRate = passRate; }
    public BigDecimal getExcellentRate() { return excellentRate; }
    public void setExcellentRate(BigDecimal excellentRate) { this.excellentRate = excellentRate; }
    public Integer getWarningCount() { return warningCount; }
    public void setWarningCount(Integer warningCount) { this.warningCount = warningCount; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
