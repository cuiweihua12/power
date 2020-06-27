package com.mr.cwh.system.base;

import lombok.Data;

/**
 * @author CWH
 */
@Data
public class BaseCondition {

    private String msg;

    private Integer offset;

    private Integer limit;

    /**
     * 排序名
     */
    private String sort;

    /**
     * 排序方式
     */
    private String order;


}
