package com.yellow.usermanager.utils;

import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ErrorCode;

/**
 * 返回工具类
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
     * @param errorCode 状态码
     * @return 返回给前端的响应
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 自定义描述的返回
     *
     * @param errorCode 状态码
     * @param description 错误的详细描述
     * @return 返回给前端的响应
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMassage(),description);
    }


    /**
     * 自定义状态码和描述、信息的返回
     *
     * @param Code  自定义状态码
     * @param message  自定义返回信息
     * @param description 自定义描述
     * @return 返回给前端的响应
     */
    public static BaseResponse error(int Code,String message,String description){
        return new BaseResponse(Code,null,message,description);
    }


    /**
     * 自定义信息和描述的返回
     *
     * @param errorCode 全局状态码
     * @param message  自定义信息
     * @param description  自定义描述
     * @return 返回给前端的响应
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(),null,message,description);
    }
}
