package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseModel {
    
    @TableId(type = IdType.ASSIGN_ID) // Using ASSIGN_ID to generate Long IDs
    protected Long id;
    
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore // Don't serialize the deleted flag to avoid confusion
    private Integer deleted = 0;
}