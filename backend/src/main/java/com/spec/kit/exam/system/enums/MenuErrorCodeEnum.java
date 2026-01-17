package com.spec.kit.exam.system.enums;

/**
 * 菜单模块错误码枚举
 * 错误码范围：300X
 */
public enum MenuErrorCodeEnum implements ErrorCode {
    MENU_NOT_FOUND("3000", "菜单不存在"),
    FAILED_TO_CREATE_MENU("3001", "创建菜单失败"),
    FAILED_TO_UPDATE_MENU("3002", "更新菜单失败"),
    FAILED_TO_DELETE_MENU("3003", "删除菜单失败"),
    MENU_ALREADY_EXISTS("3004", "菜单已存在");

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