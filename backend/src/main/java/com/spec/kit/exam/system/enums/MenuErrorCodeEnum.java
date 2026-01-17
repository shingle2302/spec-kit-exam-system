package com.spec.kit.exam.system.enums;

/**
 * 菜单模块错误码枚举
 * 错误码范围：300X
 */
public enum MenuErrorCodeEnum implements ErrorCode {
    FAILED_TO_UPDATE_MENU("3001", "更新菜单失败"),
    FAILED_TO_DELETE_MENU("3002", "删除菜单失败");

    private final String code;
    private final String message;

    MenuErrorCodeEnum(String code, String message) {
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