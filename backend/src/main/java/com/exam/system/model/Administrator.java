package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("administrators")
public class Administrator {
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @TableField("admin_level")
    private AdminLevel adminLevel;
    
    @TableField(exist = false) // Not stored in the database, used for runtime reference
    private User user;
    
    public enum AdminLevel {
        SYSTEM, SCHOOL, DEPARTMENT
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdminLevel getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(AdminLevel adminLevel) {
        this.adminLevel = adminLevel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}