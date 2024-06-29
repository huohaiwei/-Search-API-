package com.yellow.usermanager.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Sql工具类
 *
 * @author 陈翰垒
 */
public class SqlUtils {
    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return 合法返回 true，否则 false
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=","(",")"," ");
    }
}
