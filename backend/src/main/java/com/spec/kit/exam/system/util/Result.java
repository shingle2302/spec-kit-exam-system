package com.spec.kit.exam.system.util;

import com.spec.kit.exam.system.enums.ErrorCode;

public final class Result<T> {
    private final T data;
    private final String code;
    private final String msg;

    private Result(T data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>(null, "0000", "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, "0000", "操作成功");
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(data, "0000", msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        return new Result<>(null, code, msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(null, errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> error(ErrorCode errorCode, String customMsg) {
        return new Result<>(null, errorCode.getCode(), customMsg);
    }

    public T getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return "0000".equals(code);
    }
}