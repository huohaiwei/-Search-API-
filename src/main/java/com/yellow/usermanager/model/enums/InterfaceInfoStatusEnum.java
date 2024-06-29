package com.yellow.usermanager.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口状态枚举
 * @author 陈翰垒
 */
public enum InterfaceInfoStatusEnum {

    OFFLINE("关闭", 0),
    ONLINE("上线", 1);

    private final String text;

    private final int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值的列表
     *
     * @return 值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举的 value
     * @return 枚举
     */
    public static InterfaceInfoStatusEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(InterfaceInfoStatusEnum.values())) {
            return null;
        }
        for (InterfaceInfoStatusEnum userRole : InterfaceInfoStatusEnum.values()) {
            if(userRole.value==value){
                return userRole;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }
    public int getValue(){
        return value;
    }

}
