package com.mr.cwh.system.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * t_city
 * @author 
 */
@Data
public class CityEntity implements Serializable {
    /**
     * 表id
     */
    private Integer id;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 地区等级
     */
    private Boolean level;

    private static final long serialVersionUID = 1L;
}