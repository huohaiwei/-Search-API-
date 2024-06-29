package com.yellow.usermanager.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yellow.usermanager.constant.CommonConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.mapper.UserInterfaceInfoMapper;

import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.service.UserInterfaceInfoService;
import com.yellow.usermanager.utils.SqlUtils;
import com.yellow.yellowapicommon.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yellow.yellowapicommon.model.entity.UserInterfaceInfo;
import com.yellow.yellowapicommon.services.InnerUserInterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 陈翰垒
 * @description 针对表【user_interface_info(用户-接口关系表)】的数据库操作Service实现
 * @createDate 2024-01-22 21:16:23
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements InnerUserInterfaceInfoService {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

}




