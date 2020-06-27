package com.mr.cwh.system.service;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.CityCondition;

/**
 * @program: power
 * @description: city业务层接口
 * @author: cuiweihua
 * @create: 2020-06-18 09:49
 */
public interface CityService {
    CityCondition getCityById(CityCondition cityCondition);

    Response getCityByPid(CityCondition cityCondition);
}
