package com.yellow.usermanager.exception;

import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ErrorCode;
import com.yellow.usermanager.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException"+e.getMessage(),e);
        //e.getDescription()是BusinessException的自定义描述
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler (RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
