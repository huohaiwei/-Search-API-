package com.yellow.usermanager.common;

import com.yellow.usermanager.model.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类(返回给前端)
 *
 * @param <T>
 * @author 陈翰垒
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     *返回数据
     */
    private T data;

    /**
     *业务信息
     */
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;

    }

    public BaseResponse(int code, T data) {
        this(code,data,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMassage());
    }
}
