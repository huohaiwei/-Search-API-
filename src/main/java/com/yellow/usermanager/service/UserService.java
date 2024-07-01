package com.yellow.usermanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yellow.usermanager.model.dto.user.UserQueryRequest;
import com.yellow.yellowapicommon.model.entity.User;
import com.yellow.usermanager.model.vo.LoginUserVO;
import com.yellow.usermanager.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     * @return 新用户Id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request 会话请求
     * @return  脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response);

    /**
     * 退出登录
     *
     * @param request 注销用户的请求
     *
     * @return 注销成功与否
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户信息(未脱敏)
     * @param request 获取登录用户的请求
     *
     * @return 当前用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户(允许未登录)(未脱敏)
     *
     * @param request 获取当前登录用户的请求
     * @return 当前登录用户
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 未脱敏用户信息
     * @return 脱敏的已登录用户视图
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户视图
     *
     * @param user 未脱敏用户信息
     * @return 脱敏的用户视图
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户视图列表
     *
     * @param userList 未脱敏用户信息列表
     * @return 脱敏的用户视图列表
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 是否为管理员
     *
     * @param request  请求
     * @return 校验结果
     */
     boolean isAdmin(HttpServletRequest request);

    /**
     * 重载是否为管理员
     *
     * @param loginUser 登录的用户
     * @return 校验结果
     */
     boolean isAdmin(User loginUser);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 查询条件
     * @return 查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 搜索获取用户分页列表
     * @param userQueryRequest 查询条件
     * @return 用户分页列表
     */
    Page<UserVO> searchUserVOByPage(UserQueryRequest userQueryRequest);
}
