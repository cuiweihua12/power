package com.mr.cwh.system.condition;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mr.cwh.system.base.BaseCondition;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 * sys_user
 * @author
 */
@Data
@Component
@Scope("prototype")
public class UserCondition extends BaseCondition implements Serializable {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    private Integer remove;

    private String url;

    private Integer dept;

    private List<String> powers;

    private String roleName;

    private String authcode;

    private static final long serialVersionUID = 1L;


}
