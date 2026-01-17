package com.spec.kit.exam.system.enums;

/**
 * 错误码接口，定义错误码的基本属性
 */
public interface ErrorCode {
    /**
     * 获取错误码
     * @return 错误码字符串
     */
    String getCode();

    /**
     * 获取错误消息
     * @return 错误消息
     */
    String getMessage();
}