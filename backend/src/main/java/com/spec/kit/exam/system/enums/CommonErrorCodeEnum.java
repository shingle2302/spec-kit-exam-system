package com.spec.kit.exam.system.enums;

/**
 * 通用错误码枚举
 * 错误码范围：500X
 */
public enum CommonErrorCodeEnum implements ErrorCode {
    SYSTEM_ERROR("5000", "系统内部错误"),
    VALIDATION_ERROR("5001", "参数验证错误"),
    AUTHORIZATION_ERROR("5002", "授权错误"),
    INVALID_PERMISSION_CONFIG("5003", "无效的权限配置"),
    INVALID_PERMISSION_CONFIG2("5004", "无效的权限配置"),
    GENERAL_SERVER_ERROR("5005", "一般服务器错误"),
    RUNTIME_EXCEPTION("5006", "运行时异常");

    private final String code;
    private final String message;

    CommonErrorCodeEnum(String code, String message) {
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