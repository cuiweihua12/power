package com.mr.cwh.system.condition;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_user_role
 * @author 
 */
@Data
public class UserRoleCondition implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer user;

    /**
     * 角色id
     */
    private Integer role;

    private String roles;

    private static final long serialVersionUID = 1L;
}