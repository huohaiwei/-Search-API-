package com.yellow.usermanager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;

import java.util.Date;
import java.util.List;


/**
* @author 陈翰垒
* @description 针对表【interface_info(接口信息表)】的数据库操作Mapper
* @createDate 2024-01-03 22:18:58
* @Entity generator.domain.InterfaceInfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    /**
     * 查询接口列表（包括已被删除的数据）
     * @param minUpdateTime 5分钟之前的时间
     * @return
     */
    List<InterfaceInfo> listInterfaceInfoWithDelete(Date minUpdateTime);
}




