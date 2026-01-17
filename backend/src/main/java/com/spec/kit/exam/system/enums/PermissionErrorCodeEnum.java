package com.spec.kit.exam.system.enums;

/**
 * 权限模块错误码枚举
 * 错误码范围：400X
 */
public enum PermissionErrorCodeEnum implements ErrorCode {
    FAILED_TO_ASSIGN_PERMISSIONS("4001", "分配权限失败"),
    FAILED_TO_REMOVE_PERMISSIONS("4002", "移除权限失败"),
    FAILED_TO_UPDATE_PERMISSION("4003", "更新权限失败"),
    FAILED_TO_DELETE_PERMISSION("4004", "删除权限失败"),
    PERMISSION_NOT_FOUND("4005", "权限未找到");

    private final String code;
    private final String message;

    PermissionErrorCodeEnum(String code, String message) {
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