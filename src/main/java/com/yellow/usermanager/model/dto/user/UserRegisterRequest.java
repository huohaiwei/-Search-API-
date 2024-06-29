package com.yellow.usermanager.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author 陈翰垒
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -6681944583923648394L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 再次输入密码
     */
    private String checkPassword;
}
