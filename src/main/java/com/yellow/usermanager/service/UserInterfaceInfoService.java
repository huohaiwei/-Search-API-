package com.yellow.usermanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yellow.yellowapicommon.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yellow.yellowapicommon.model.entity.UserInterfaceInfo;


/**
 * @author 陈翰垒
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取查询条件
     * @param userInterfaceInfoQueryRequest 查询条件请求
     * @return 查询条件
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 新增默认50次接口调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean addDefaultInvokeCount(long interfaceInfoId, long userId);
}
