package com.exam.system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Start inserting fill...");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "creator", String.class, getCurrentUser());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updater", String.class, getCurrentUser());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Start updating fill...");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updater", String.class, getCurrentUser());
    }

    private String getCurrentUser() {
        // In a real application, this would get the current user from the security context
        // For now, we'll return 'admin' as default when no context exists
        return "admin";
    }
}