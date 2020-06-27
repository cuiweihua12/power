package com.mr.cwh.system.controller;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.CityCondition;
import com.mr.cwh.system.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: power
 * @description: 城市控制层
 * @author: cuiweihua
 * @create: 2020-06-18 09:48
 */
@RestController
@RequestMapping("city")
public class CityController {

    @Autowired
    private CityService service;

    @GetMapping
    public CityCondition getCityById(CityCondition cityCondition){
        return service.getCityById(cityCondition);
    }

    @GetMapping("byPid")
    public Response getCityByPid(CityCondition cityCondition){
        return service.getCityByPid(cityCondition);
    }

}
