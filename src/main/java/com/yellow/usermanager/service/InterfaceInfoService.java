package com.yellow.usermanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yellow.usermanager.model.dto.search.InterfaceInfoQuery;
import com.yellow.usermanager.model.dto.search.SearchQuery;
import com.yellow.usermanager.model.dto.user.UserQueryRequest;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import com.yellow.yellowapicommon.model.entity.User;
import com.yellow.usermanager.model.vo.InterfaceInfoVO;
import com.yellow.usermanager.model.vo.UserVO;


/**
* @author 陈翰垒
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-01-03 22:18:58
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 获取查询条件
     * @param interfaceInfoQueryRequest 查询条件请求
     * @return 查询条件
     */
     QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取接口信息视图
     *
     * @param interfaceInfo 接口信息
     * @return 处理过的接口信息视图
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    /**
     * 新增接口
     *
     * @param interfaceInfo 接口信息
     * @param addUser 添加用户
     * @return 处理过的接口信息视图
     */
    boolean addInterfaceInfo(InterfaceInfo interfaceInfo,User addUser);

    /**
     * 搜索获取接口分页列表
     * @param interfaceInfoQueryRequest 查询条件
     * @return 接口分页列表
     */
    Page<InterfaceInfoVO> searchInterfaceInfoVOByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     *
     */
    Page<InterfaceInfo> searchFromEs(InterfaceInfoQuery searchQuery);
}
