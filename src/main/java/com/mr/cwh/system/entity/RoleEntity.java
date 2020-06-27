package com.mr.cwh.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_role
 * @author 
 */
@Data
public class RoleEntity implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 数据生成时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}