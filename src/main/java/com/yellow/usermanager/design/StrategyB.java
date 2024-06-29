package com.yellow.usermanager.design;

import com.yellow.yellowapiclientsdk.client.YellowApiClient;
import com.yellow.yellowapiclientsdk.model.User;

/**
 * @author 陈翰垒
 */
public class StrategyB implements InvokeStrategy {

    @Override
    public String invokeInterfaceInfo(String accessKey, String secretKey, String userRequestParams) {
        YellowApiClient tempClient = new YellowApiClient(accessKey, secretKey);
        User user = tempClient.getUser();
        return "名字是: "+user.getName()+" 年龄是: "+user.getAge();
    }
}
