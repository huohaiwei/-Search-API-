package com.yellow.usermanager.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.mapper.UserMapper;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.yellowapicommon.model.entity.User;
import com.yellow.yellowapicommon.services.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 陈翰垒
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokedUser(String accessKey) {

        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey), ErrorCode.PARAMS_ERROR);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }

}
