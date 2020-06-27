package com.mr.cwh.system.service.impl;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.DeptCondition;
import com.mr.cwh.system.mapper.DeptMapper;
import com.mr.cwh.system.service.DeptService;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: power
 * @description: 业务层
 * @author: cuiweihua
 * @create: 2020-06-18 11:56
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper mapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Response getDeptAll(DeptCondition condition) {

        Response response = new Response();
        List<Object> range = redisTemplate.opsForHash().values("alldept");

        if (range.isEmpty()){
            List<DeptCondition> list  = mapper.getDeptAll(condition);
            for (DeptCondition deptCondition : list) {
                redisTemplate.opsForHash().put("alldept",String.valueOf(deptCondition.getId()),deptCondition);
            }
            response.setList(list);
            return response;
        }
        response.setList(range);
        return response;
    }

    @Override
    public DeptCondition getDeptById(DeptCondition condition) {
        DeptCondition dept = (DeptCondition) redisTemplate.opsForHash().get("alldept", String.valueOf(condition.getId()));
        if (dept == null){
            dept = mapper.selectByPrimaryKey(condition.getId());
            if (dept == null){
                return new DeptCondition("无信息");
            }
            redisTemplate.opsForHash().put("alldept",String.valueOf(dept.getId()),dept);
        }
        return dept;
    }
}
