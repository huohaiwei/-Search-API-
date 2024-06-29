package com.yellow.usermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yellow.yellowapicommon.model.entity.UserInterfaceInfo;

import java.util.List;


/**
* @author 陈翰垒
* @description 针对表【user_interface_info(用户-接口关系表)】的数据库操作Mapper
* @createDate 2024-01-22 21:16:23
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 获取调用次数前top名的接口
     * @param limit 前x名
     * @return
     */
    List<UserInterfaceInfo> getTopInvokeInterfaceInfo(int limit);
}




