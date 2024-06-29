package com.yellow.usermanager.design;

import java.io.UnsupportedEncodingException;

/**
 * 调用策略
 * @author 陈翰垒
 */
public interface InvokeStrategy {
    /**
     * 调用接口
     * @param accessKey
     * @param secretKey
     * @param userRequestParams
     */
    String invokeInterfaceInfo(String accessKey,String secretKey,String userRequestParams) throws UnsupportedEncodingException;

}
