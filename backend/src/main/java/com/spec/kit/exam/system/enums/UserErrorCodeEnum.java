package com.spec.kit.exam.system.enums;

/**
 * 用户模块错误码枚举
 * 错误码范围：100X
 */
public enum UserErrorCodeEnum implements ErrorCode {
    USER_NOT_FOUND("1001", "用户未找到"),
    FAILED_TO_UNLOCK_USER("1002", "解锁用户失败"),
    REGISTRATION_ERROR("1003", "注册错误"),
    AUTHENTICATION_FAILED("1004", "认证失败"),
    LOGOUT_FAILED("1005", "登出失败"),
    ACCOUNT_LOCKED("1006", "账户已被锁定");

    private final String code;
    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}