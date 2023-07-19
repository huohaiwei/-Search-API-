package com.yellow.usermanager.service;

import com.yellow.usermanager.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈翰垒
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-07-14 00:14:18
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 验证密码
     * @param userCode 用户编号
     * @return 新用户Id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String userCode);

    /**
     * 用户登录
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request 请求域
     * @return  脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户信息
     * @return  脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 退出登录
     *
     * @param request 注销用户的请求
     *
     * @return 随便返回一个数
     */
    int userLogout(HttpServletRequest request);
}
