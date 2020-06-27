package com.mr.cwh.system.config.dataInit;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.config.RedisBloomFilter;
import com.mr.cwh.system.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @program: power
 * @description: 布隆过滤器初始化
 * @author: cuiweihua
 * @create: 2020-06-18 08:06
 */
public class RedisDataInit {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private RedisBloomFilter bloomFilter;

    @PostConstruct
    public void init(){
        Response user = userService.getUser(new UserCondition());
        List<UserCondition> list = (List<UserCondition>) user.getList();
        for (UserCondition condition : list) {
            bloomFilter.put(String.valueOf(condition.getId()));
        }
    }

}
