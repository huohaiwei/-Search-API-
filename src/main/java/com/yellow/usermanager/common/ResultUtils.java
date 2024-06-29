package com.yellow.usermanager.common;

import com.yellow.usermanager.model.enums.ErrorCode;

/**
 * 返回工具类
 * @author 陈翰垒
 */
public class ResultUtils {

    /**
     * 返回成功
     *
     * @param data 返回的数据
     * @param <T> 泛型
     * @return 返回给前端的响应
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 返回失败
     *
     * @param errorCode 错误码
     * @return 返回给前端的响应
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * runtimeException错误返回
     *
     * @param errorCode 错误码
     * @param message 错误的详细描述
     * @return 返回给前端的响应
     */
    public static BaseResponse error(ErrorCode errorCode,String message){
        return new BaseResponse(errorCode.getCode(),null,message);
    }


    /**
     * businessException错误返回
     *
     * @param Code  自定义状态码
     * @param message  自定义返回信息
     * @return 返回给前端的响应
     */
    public static BaseResponse error(int Code,String message){
        return new BaseResponse(Code,null,message);
    }

}
