package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("administrators")
public class Administrator extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("admin_level")
    private AdminLevel adminLevel;
    
    @TableField(exist = false) // Not stored in the database, used for runtime reference
    private User user;
    
    public enum AdminLevel {
        SYSTEM, SCHOOL, DEPARTMENT
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