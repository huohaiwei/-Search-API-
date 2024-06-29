package com.yellow.usermanager.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author 陈翰垒
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1645642329825067845L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;
}
