package com.spec.kit.exam.system.aspect;

import com.spec.kit.exam.system.annotation.OperationLog;
import com.spec.kit.exam.system.entity.OperationLogEntity;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.OperationLogService;
import com.spec.kit.exam.system.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class OperationLogAspect {
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private UserService userService;
    
    @Around("@annotation(operationLog)")
    public Object doAround(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = point.proceed();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            saveOperationLog(point, operationLog, true, duration, null);
            
            return result;
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            saveOperationLog(point, operationLog, false, duration, e.getMessage());
            
            throw e;
        }
    }
    
    private void saveOperationLog(ProceedingJoinPoint point, OperationLog operationLog,
                               boolean success, long duration, String errorMsg) {
        try {
            User currentUser = userService.getCurrentUser();
            HttpServletRequest request = getCurrentRequest();
            
            OperationLogEntity log = new OperationLogEntity();
            
            if (currentUser != null) {
                try {
                    log.setUserId(Long.parseLong(currentUser.getId()));
                    log.setUsername(currentUser.getUsername());
                } catch (NumberFormatException e) {
                    log.setUsername(currentUser.getUsername());
                }
            }
            
            log.setModule(operationLog.module());
            log.setOperation(operationLog.operation());
            log.setDescription(operationLog.description());
            log.setStatus(success ? "SUCCESS" : "FAILURE");
            log.setDuration(duration);
            log.setErrorMsg(errorMsg);
            
            if (request != null) {
                log.setRequestIp(getClientIp(request));
                log.setRequestUrl(request.getRequestURI());
                log.setRequestMethod(request.getMethod());
            }
            
            operationLogService.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        return ip;
    }
}