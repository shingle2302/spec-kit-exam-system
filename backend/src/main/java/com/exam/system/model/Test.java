package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Document(indexName = "tests")
@TableName("tests")
public class Test extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @Field(type = FieldType.Text, name = "title")
    @TableField("title")
    private String title;
    
    @Field(type = FieldType.Text, name = "description")
    @TableField("description")
    private String description;
    
    @Field(type = FieldType.Keyword, name = "grade_id")
    @TableField("grade_id")
    private Long gradeId;
    
    @Field(type = FieldType.Keyword, name = "subject_id")
    @TableField("subject_id")
    private Long subjectId;
    
    @Field(type = FieldType.Integer, name = "time_limit_minutes")
    @TableField("time_limit_minutes")
    private Integer timeLimitMinutes;
    
    @Field(type = FieldType.Boolean, name = "is_active")
    @TableField("is_active")
    private Boolean isActive = true;
    
    @Field(type = FieldType.Boolean, name = "is_published")
    @TableField("is_published")
    private Boolean isPublished = false;
    
    @Field(type = FieldType.Date, name = "publish_date")
    @TableField("publish_date")
    private LocalDateTime publishDate;

    public Test() {
        // Default constructor
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }
}