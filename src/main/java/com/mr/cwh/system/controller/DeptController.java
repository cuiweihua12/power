package com.mr.cwh.system.controller;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.DeptCondition;
import com.mr.cwh.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: power
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-18 11:57
 */
@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private DeptService service;

    @GetMapping
    public Response getDeptAll(DeptCondition condition){
        return service.getDeptAll(condition);
    }

    @GetMapping("/byId")
    public DeptCondition getDeptById(DeptCondition condition){
        return service.getDeptById(condition);
    }
}
