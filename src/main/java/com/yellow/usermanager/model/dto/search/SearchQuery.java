package com.yellow.usermanager.model.dto.search;

import com.yellow.usermanager.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 陈翰垒
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchQuery extends PageRequest implements Serializable {
    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}
