package com.yellow.usermanager.design;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author 陈翰垒
 */
public class StrategyContext {
    private InvokeStrategy invokeStrategy;

    public void setInvokeStrategy(InvokeStrategy invokeStrategy) {
        this.invokeStrategy = invokeStrategy;
    }

    public String handleStrategy(String accessKey,String secretKey,String userRequestParams) throws UnsupportedEncodingException {
        String res = invokeStrategy.invokeInterfaceInfo(accessKey, secretKey, userRequestParams);
        return res;
    }
}
