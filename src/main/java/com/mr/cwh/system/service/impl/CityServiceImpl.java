package com.mr.cwh.system.service.impl;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.CityCondition;
import com.mr.cwh.system.mapper.CityMapper;
import com.mr.cwh.system.service.CityService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: power
 * @description: 业务层接口实现
 * @author: cuiweihua
 * @create: 2020-06-18 09:49
 */
@Service
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper mapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    /**
    *@Description:  通过id查询
    *@Param: [cityCondition]
    *@return: com.mr.cwh.system.condition.CityCondition
    *@Author: cuiweihua
    *@date: 2020/6/18
    */
    public CityCondition getCityById(CityCondition cityCondition) {
        //查询缓存
        CityCondition city = (CityCondition) redisTemplate.opsForValue().get(String.valueOf(cityCondition.getId()+"id"));
        if (city == null){
            //查询数据库
            city = mapper.selectByPrimaryKey(cityCondition.getId());
            //加入缓存
            redisTemplate.opsForValue().set(String.valueOf(city.getId()+"id"),city);
            //设置过期时间
            redisTemplate.expire(String.valueOf(city.getId()+"id"),1, TimeUnit.MINUTES);
        }
        return city;
    }

    @Override
    /**
    *@Description: 通过pid 查询
    *@Param: [cityCondition]
    *@return: com.mr.cwh.system.condition.CityCondition
    *@Author: cuiweihua
    *@date: 2020/6/18
    */
    public Response getCityByPid(CityCondition cityCondition) {
        Response response = new Response();
        //查询缓存
        List<Object> allcitypid = redisTemplate.opsForHash().values(String.valueOf(cityCondition.getParentId()+"pid"));
        if (allcitypid.isEmpty()){
            //查询数据库
            List<CityCondition> list = mapper.selectByPid(cityCondition.getParentId());
            for (CityCondition condition : list) {
                //加入缓存
                redisTemplate.opsForHash().put(String.valueOf(cityCondition.getParentId()+"pid"),String.valueOf(condition.getId()),condition);
                //设置过期时间s
                redisTemplate.expire(String.valueOf(cityCondition.getParentId()+"pid"),10, TimeUnit.MINUTES);
            }

            response.setList(list);
            return response;
        }
        response.setList(allcitypid);
        return response;
    }


}
