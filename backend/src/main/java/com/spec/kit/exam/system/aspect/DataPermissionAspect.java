package com.spec.kit.exam.system.aspect;

import com.spec.kit.exam.system.annotation.DataPermission;
import com.spec.kit.exam.system.entity.BaseEntity;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataPermissionAspect {
    
    @Autowired
    private UserService userService;
    
    @Around("@annotation(dataPermission)")
    public Object doAround(ProceedingJoinPoint point, DataPermission dataPermission) throws Throwable {
        User currentUser = userService.getCurrentUser();
        Object[] args = point.getArgs();
        String dataScopeSql = buildDataScopeSql(currentUser, dataPermission);
        
        if (args.length > 0 && args[0] instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) args[0];
            entity.setDataScope(dataScopeSql);
        }
        
        return point.proceed();
    }
    
    private String buildDataScopeSql(User user, DataPermission dataPermission) {
        StringBuilder sql = new StringBuilder();
        
        if (user.isAdmin()) {
            return "";
        }
        
        if (user.isTeacher()) {
            sql.append(" AND ").append(dataPermission.deptAlias())
               .append(".class_id IN (SELECT class_id FROM teacher_class WHERE user_id = ")
               .append(user.getId()).append(")");
        }
        
        if (user.isStudent()) {
            sql.append(" AND ").append(dataPermission.userAlias())
               .append(".id = ").append(user.getId());
        }
        
        return sql.toString();
    }
}