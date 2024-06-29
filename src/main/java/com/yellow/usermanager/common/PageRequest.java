package com.yellow.usermanager.common;

import com.yellow.usermanager.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求类
 *
 * @author 陈翰垒
 */
@Data
public class PageRequest {

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式(默认升序)
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
