package com.yellow.usermanager.model.domain.request;

import lombok.Data;


import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 陈翰垒
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 1597884981705283356L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 二次密码
     */
    private String checkPassword;

    /**
     * 用户编号
     */
    private String userCode;
}
