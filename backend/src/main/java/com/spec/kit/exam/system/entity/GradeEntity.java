package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 年级实体类
 */
@Data
@TableName("grades")
public class GradeEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long educationalLevelId;

    private String status;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}