package com.mr.cwh.system.condition;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_power
 * @author 
 */
@Data
public class PowerCondition implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 父级权限id
     */
    private Integer pid;

    /**
     * URL
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * shiro权限
     */
    private String auth;

    /**
     * 数据产生时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}