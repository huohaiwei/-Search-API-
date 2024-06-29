package com.yellow.usermanager.exception;

import com.yellow.usermanager.model.enums.ErrorCode;

/**
 * 自定义异常类
 *
 * @author 陈翰垒
 */
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 仅有错误码的异常
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMassage());
        this.code = errorCode.getCode();
    }

    /**
     * 错误码和自定义错误信息
     *
     * @param errorCode 错误码
     * @param message   自定义错误信息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();

    }

    public int getCode() {
        return code;
    }

}
