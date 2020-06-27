package com.mr.cwh.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_user
 * @author 
 */
@Data
public class UserEntity implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 所在城市
     */
    private Integer city;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据产生时间
     */
    private Date createTime;

    private Integer remove;

    private String url;

    private Integer dept;

    private static final long serialVersionUID = 1L;
}