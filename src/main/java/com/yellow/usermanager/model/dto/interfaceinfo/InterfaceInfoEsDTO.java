package com.yellow.usermanager.model.dto.interfaceinfo;

import com.google.gson.Gson;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 陈翰垒
 */
//开启ES实体
@Document(indexName = "interface_info")
@Data
public class InterfaceInfoEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 创建用户
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param
     * @return
     */
    public static InterfaceInfoEsDTO objToDto(InterfaceInfo interfaceInfo){
        if (interfaceInfo==null){
            return null;
        }
        InterfaceInfoEsDTO interfaceInfoEsDTO = new InterfaceInfoEsDTO();
        BeanUtils.copyProperties(interfaceInfo,interfaceInfoEsDTO);
        return interfaceInfoEsDTO;
    }


}
