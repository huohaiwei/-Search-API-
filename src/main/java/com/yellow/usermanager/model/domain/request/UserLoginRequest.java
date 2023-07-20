package com.yellow.usermanager.model.domain.request;

import lombok.Data;


import java.io.Serializable;


/**
 * 用户登录请求体
 *
 * @author 陈翰垒
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -4996142733808620786L;

    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;


}
