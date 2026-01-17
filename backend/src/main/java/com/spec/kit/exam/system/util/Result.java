package com.spec.kit.exam.system.util;

/**
 * Utility class for unified API responses in {data, code, msg} format
 */
public class Result<T> {
    private T data;
    private String code;
    private String msg;

    // Private constructor to enforce use of static methods
    private Result(T data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    // Static factory methods for common responses
    public static <T> Result<T> success(T data) {
        return new Result<>(data, "0000", "success");
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(data, "0000", msg);
    }

    public static <T> Result<T> success() {
        return new Result<>(null, "0000", "success");
    }

    public static <T> Result<T> error(String code, String msg) {
        return new Result<>(null, code, msg);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(null, "500", msg); // Default server error code
    }

    public static <T> Result<T> error(String code, String msg, T data) {
        return new Result<>(data, code, msg);
    }

    // Validation error
    public static <T> Result<T> validationError(String msg) {
        return new Result<>(null, "1001", msg); // Validation error code
    }

    // Authorization error
    public static <T> Result<T> unauthorized(String msg) {
        return new Result<>(null, "2001", msg); // Authorization error code
    }

    // Getters and setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}