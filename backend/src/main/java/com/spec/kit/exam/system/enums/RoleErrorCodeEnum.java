package com.spec.kit.exam.system.enums;

/**
 * 角色模块错误码枚举
 * 错误码范围：200X
 */
public enum RoleErrorCodeEnum implements ErrorCode {
    ROLE_NOT_FOUND("2001", "角色未找到");

    private final String code;
    private final String message;

    RoleErrorCodeEnum(String code, String message) {
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