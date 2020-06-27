package com.mr.cwh.system.condition;

import java.io.Serializable;
import lombok.Data;

/**
 * t_city
 * @author 
 */
@Data
public class CityCondition implements Serializable {
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

    private String msg;

    public CityCondition() {
    }

    public CityCondition(String msg) {
        this.msg = msg;
    }

    public CityCondition(Object o) {
    }
}