package com.mr.cwh.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_dept
 * @author 
 */
@Data
public class DeptEntity implements Serializable {
    /**
     * 部门id
     */
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父级部门id
     */
    private Integer pid;

    /**
     * 描述
     */
    private String description;

    /**
     * 数据创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}