package com.mr.cwh.system.condition;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * sys_dept
 * @author 
 */
@Data
public class DeptCondition implements Serializable {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "yyyy-MM-dd",pattern = "yyyy-MM-dd")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    private String msg;

    public DeptCondition() {
    }

    public DeptCondition(String msg) {
        this.msg = msg;
    }
}