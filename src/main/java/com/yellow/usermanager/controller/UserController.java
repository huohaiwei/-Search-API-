package com.yellow.usermanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yellow.usermanager.annotation.AuthChecker;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.DeleteRequest;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.constant.UserConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.model.dto.user.*;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.model.vo.LoginUserVO;
import com.yellow.usermanager.model.vo.UserVO;
import com.yellow.usermanager.service.UserService;
import com.yellow.yellowapicommon.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户接口
 *
 * @author 陈翰垒
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:8000"}, allowCredentials = "true")
@Slf4j
public class UserController {

    /**
     * 盐值
     */
    private static final String SALT = "yellow";

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册信息
     * @return 注册成功后的用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未接收到请求！");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "表单有空项");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录用户请求
     * @param request          请求
     * @return 登录成功后的用户信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 更新个人信息
     *
     * @param updateMyRequest 更新个人信息请求
     * @param request         请求
     * @return 更新成功提示
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest updateMyRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(updateMyRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        //代理对象
        User user = new User();
        BeanUtils.copyProperties(updateMyRequest, user);
        user.setId(loginUser.getId());
        boolean res = userService.updateById(user);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id 用户id
     * @param request 请求
     * @return 脱敏用户信息
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        UserVO userVO = userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }

    /**
     * 退出登录
     *
     * @param request 用户注销的请求
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到请求");
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 当前登录用户视图
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 更新用户信息（管理员）
     *
     * @param userUpdateRequest 要更新的用户
     * @param request           更新请求
     * @return 更新成功提示
     */
    @PostMapping("/update")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        //1.校验参数是否为空
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据Id获取用户信息
     *
     * @param id      用户id
     * @param request 请求
     * @return 用户信息
     */
    @GetMapping("/get")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 删除用户
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return 删除成功提示
     */
    @PostMapping("/delete")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean res = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(res);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest 添加请求，不包含密码
     * @return 添加的用户的id
     */
    @PostMapping("/add")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        //默认密码12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean res = userService.save(user);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 分页获取用户封装列表（管理员）
     *
     * @param userQueryRequest 查询请求
     * @return 用户列表
     */
    @PostMapping("/list/page")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取脱敏用户列表
     *
     * @param userQueryRequest 查询请求
     * @return 用户列表
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        //限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        //脱敏用户列表视图
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        //符合条件的分页对象
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

//    /**
//     * 根据搜索词获取用户VO列表
//     */
//    @PostMapping("/search/list/page/vo")
//    public BaseResponse<Page<UserVO>> searchListByPage(@RequestBody UserQueryRequest userQueryRequest) {
//        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
//        long size = userQueryRequest.getPageSize();
//        //限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        //脱敏用户列表视图
//        Page<UserVO> userVO = userService.searchUserVOByPage(userQueryRequest);
//        //符合条件的分页对象
//        return ResultUtils.success(userVOPage);
//    }

}
