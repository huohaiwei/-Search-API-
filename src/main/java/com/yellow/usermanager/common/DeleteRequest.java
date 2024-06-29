package com.yellow.usermanager.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的删除请求
 * @author 陈翰垒
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * 要删除的数据的id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
