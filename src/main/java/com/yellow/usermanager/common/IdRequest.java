package com.yellow.usermanager.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口id请求
 * @author 陈翰垒
 */
@Data
public class IdRequest implements Serializable {

    /**
     * 接口的id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
