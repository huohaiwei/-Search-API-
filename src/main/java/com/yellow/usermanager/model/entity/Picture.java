package com.yellow.usermanager.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 *
 * @author
 * @from
 */
@Data
public class Picture implements Serializable {

    private String title;

    private String url;

    private static final long serialVersionUID = 1L;

}