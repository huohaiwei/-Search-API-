package com.yellow.usermanager.model.vo;

import com.yellow.usermanager.model.entity.Picture;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索VO
 * @author 陈翰垒
 */
@Data
public class SearchVO implements Serializable {

    /**
     * 用户VO列表
     */
    private List<UserVO> userVOList;

    /**
     * 接口VO列表
     */
    private List<InterfaceInfo> interfaceInfoList;

    /**
     * 图片列表
     */
    private List<Picture> pictureList;

    private static final long serialVersionUID = 1L;
}
