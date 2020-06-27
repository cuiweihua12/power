package com.mr.cwh.system.base;

import com.mr.cwh.system.condition.UserCondition;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CWH
 */
@Data
@Component
@Scope("prototype")
public class Response {

    private List<?> list;

    private UserCondition condition;

    private Integer total;

    private String msg;

    private Integer result = 0;

    public Response() {
    }

    public Response(String msg, Integer result) {
        this.msg = msg;
        this.result = result;
    }

    public Response(List<?> list) {
        this.list = list;
    }
}
