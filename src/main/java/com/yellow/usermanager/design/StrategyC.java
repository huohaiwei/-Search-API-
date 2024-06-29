package com.yellow.usermanager.design;

import com.google.gson.Gson;
import com.yellow.yellowapiclientsdk.client.YellowApiClient;
import com.yellow.yellowapiclientsdk.model.User;

import java.io.UnsupportedEncodingException;

/**
 * @author 陈翰垒
 */
public class StrategyC implements InvokeStrategy {

    @Override
    public String invokeInterfaceInfo(String accessKey, String secretKey, String userRequestParams) throws UnsupportedEncodingException {
        YellowApiClient tempClient = new YellowApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        User requestUser = gson.fromJson(userRequestParams, User.class);
        return tempClient.saveUser(requestUser);
    }
}
