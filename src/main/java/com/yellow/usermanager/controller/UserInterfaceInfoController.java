package com.yellow.usermanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yellow.usermanager.annotation.AuthChecker;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.constant.UserConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;

import com.yellow.yellowapicommon.model.entity.User;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.service.UserInterfaceInfoService;
import com.yellow.usermanager.service.UserService;
import com.yellow.yellowapiclientsdk.client.YellowApiClient;
import com.yellow.yellowapicommon.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.yellow.yellowapicommon.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yellow.yellowapicommon.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.yellow.yellowapicommon.model.entity.UserInterfaceInfo;
import com.yellow.yellowapicommon.services.InnerUserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口controller
 *
 * @author 陈翰垒
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@CrossOrigin(origins = {"http://localhost:8000","https://poorly-desired-macaw.ngrok-free.app"}, allowCredentials = "true")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private YellowApiClient yellowApiClient;

//    /**
//     * 创建
//     *
//     * @param userInterfaceInfoAddRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/add")
//    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Long> addInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
//        if (userInterfaceInfoAddRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
//        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
//        //校验参数
//        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
//        User loginUser = userService.getLoginUser(request);
//        userInterfaceInfo.setUserId(loginUser.getId());
//        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
//        // 返回新加入的id
//        long newInterfaceInfoId = userInterfaceInfo.getId();
//        return ResultUtils.success(newInterfaceInfoId);
//    }

//    /**
//     * 删除
//     *
//     * @param deleteRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/delete")
//    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
//        if (deleteRequest == null || deleteRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
//        // 判断是否存在
//        UserInterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可删除
//        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean res = interfaceInfoService.removeById(id);
//        return ResultUtils.success(res);
//    }

    /**
     * 更新（仅管理员）
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
//    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        //         限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserInterfaceInfo> interfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

//    /**
//     * 分页获取列表（封装类）
//     *
//     * @param interfaceInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody UserInterfaceInfoQueryRequest interfaceInfoQueryRequest,
//            HttpServletRequest request) {
//        long current = interfaceInfoQueryRequest.getCurrent();
//        long size = interfaceInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<userInterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
//                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
//        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
//    }

//    /**
//     * 分页获取当前用户创建的资源列表
//     *
//     * @param interfaceInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody UserInterfaceInfoQueryRequest interfaceInfoQueryRequest,
//            HttpServletRequest request) {
//        if (interfaceInfoQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        interfaceInfoQueryRequest.setUserId(loginUser.getId());
//        long current = interfaceInfoQueryRequest.getCurrent();
//        long size = interfaceInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<userInterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
//                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
//        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
//    }

    // endregion

//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param interfaceInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(@RequestBody UserInterfaceInfoQueryRequest interfaceInfoQueryRequest,
//            HttpServletRequest request) {
//        long size = interfaceInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<userInterfaceInfo> interfaceInfoPage = interfaceInfoService.searchFromEs(interfaceInfoQueryRequest);
//        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
//    }

//    /**
//     * 编辑（用户）
//     *
//     * @param interfaceInfoEditRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceInfoEditRequest, HttpServletRequest request) {
//        if (interfaceInfoEditRequest == null || interfaceInfoEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        userInterfaceInfo userInterfaceInfo = new userInterfaceInfo();
//        BeanUtils.copyProperties(interfaceInfoEditRequest, userInterfaceInfo);
//        List<String> tags = interfaceInfoEditRequest.getTags();
//        if (tags != null) {
//            userInterfaceInfo.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 参数校验
//        interfaceInfoService.validInterfaceInfo(userInterfaceInfo, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = interfaceInfoEditRequest.getId();
//        // 判断是否存在
//        userInterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = interfaceInfoService.updateById(userInterfaceInfo);
//        return ResultUtils.success(result);
//    }


}
