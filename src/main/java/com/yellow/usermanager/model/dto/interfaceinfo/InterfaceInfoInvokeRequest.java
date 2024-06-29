package com.yellow.usermanager.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用接口请求
 *
 * @author 陈翰垒
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户传递请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}