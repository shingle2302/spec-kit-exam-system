package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Document(indexName = "users")
@TableName("users")
public class User extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @Field(type = FieldType.Text, name = "username")
    @TableField("username")
    private String username;
    
    @Field(type = FieldType.Text, name = "email")
    @TableField("email")
    private String email;
    
    @Field(type = FieldType.Text, name = "password_hash")
    @TableField("password_hash")
    private String passwordHash;
    
    @Field(type = FieldType.Text, name = "first_name")
    @TableField("first_name")
    private String firstName;
    
    @Field(type = FieldType.Text, name = "last_name")
    @TableField("last_name")
    private String lastName;
    
    @Field(type = FieldType.Keyword, name = "role")
    @TableField("role")
    private Role role;
    
    @Field(type = FieldType.Date, name = "created_at")
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @Field(type = FieldType.Date, name = "updated_at")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    @Field(type = FieldType.Boolean, name = "is_active")
    @TableField("is_active")
    private Boolean isActive;
    
    public enum Role {
        STUDENT, TEACHER, ADMIN
    }

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}