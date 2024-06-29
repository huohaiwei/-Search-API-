package com.yellow.usermanager.exception;

import com.yellow.usermanager.model.enums.ErrorCode;

/**
 * 抛出异常的工具类
 *
 * @author 陈翰垒
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param runtimeException 顶级异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException ) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode ) {
            throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常 ，并指定自定义错误信息
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message ) {
        throwIf(condition, new BusinessException(errorCode,message));
    }


}
