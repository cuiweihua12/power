package com.mr.cwh.system.condition;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_role_power
 * @author 
 */
@Data
public class RolePowerCondition implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色id
     */
    private Integer role;

    /**
     * 权限id
     */
    private Integer power;

    private String powers;

    private static final long serialVersionUID = 1L;
}