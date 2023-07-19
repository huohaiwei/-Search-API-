package com.yellow.usermanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ErrorCode;
import com.yellow.usermanager.constant.UserConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.model.domain.User;
import com.yellow.usermanager.model.domain.request.UserLoginRequest;
import com.yellow.usermanager.model.domain.request.UserRegisterRequest;
import com.yellow.usermanager.service.UserService;
import com.yellow.usermanager.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author 陈翰垒
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未接收到请求！");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userCode = userRegisterRequest.getUserCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, userCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"表单有空项");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, userCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userRegister(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未接收到登录请求！");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"表单有空项！");
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户的信息
     *
     * @param request
     * @return 用户的信息
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userinfo = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userinfo;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"用户未登录！");
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUserList(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user ->
                userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员");
        }

        if (id <= 0) {
            return null;
        }
        boolean result= userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 退出登录
     *
     * @param request 用户注销的请求
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未收到请求");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userinfo = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userinfo;
        if (user == null || user.getUserRole() != UserConstant.ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
