package com.yellow.usermanager.design;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellow.yellowapiclientsdk.client.YellowApiClient;

/**
 * @author 陈翰垒
 */
public class StrategyA implements InvokeStrategy {

    @Override
    public String invokeInterfaceInfo(String accessKey, String secretKey, String userRequestParams) {
        YellowApiClient tempClient = new YellowApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(userRequestParams, JsonObject.class);
        String name = json.get("name").getAsString();
        String res = tempClient.helloWithName(name);
        return res;
    }
}
