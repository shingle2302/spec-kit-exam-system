package com.spec.kit.exam.system.config;

import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.CommonErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that formats all errors consistently
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return Result.error(CommonErrorCodeEnum.GENERAL_SERVER_ERROR, "Internal server error occurred");
    }

    /**
     * Handle runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime error occurred: ", ex);
        return Result.error(CommonErrorCodeEnum.RUNTIME_EXCEPTION, ex.getMessage());
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid argument provided: ", ex);
        return Result.validationError(ex.getMessage());
    }

    /**
     * Handle security-related exceptions
     */
    @ExceptionHandler(SecurityException.class)
    public Result<Object> handleSecurityException(SecurityException ex) {
        logger.warn("Security violation occurred: ", ex);
        return Result.unauthorized("Access denied");
    }
}