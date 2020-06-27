package com.mr.cwh.system.service.impl;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.condition.UserRoleCondition;
import com.mr.cwh.system.mapper.RoleMapper;
import com.mr.cwh.system.mapper.RolePowerMapper;
import com.mr.cwh.system.mapper.UserMapper;
import com.mr.cwh.system.mapper.UserRoleMapper;
import com.mr.cwh.system.service.RoleService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: power
 * @description: 业务层接口实现
 * @author: cuiweihua
 * @create: 2020-06-17 19:45
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper mapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RolePowerMapper rolePowerMapper;


    private BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(),10000,0.01);;

    @Override
    /**
    *@Description: 获取角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response getRole(RoleCondition condition) {
        Response response = this.getResponse();
        //查询缓存
        List<Object> allrole = redisTemplate.opsForHash().values("allrole").stream().collect(Collectors.toList());
        if (allrole.isEmpty()){
            //查询数据库
            List<RoleCondition> list = mapper.getRole(condition);
            //写入缓存
            list.forEach(i->{
                redisTemplate.opsForHash().put("allrole",String.valueOf(i.getId()),i);
            });
            response.setList(list);
            response.setTotal(list.size());

        }
        //查询缓存总条数
        int total = redisTemplate.opsForHash().size("allrole").intValue();
        response.setList(allrole);
        response.setTotal(total);
        return response;
    }

    @Override
    /**
    *@Description: 通过id获取角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.RoleCondition
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public RoleCondition getRoleById(RoleCondition condition) {
        //查询过滤器
        if (!bloomFilter.mightContain(condition.getId())){
            return new RoleCondition("无信息");
        }
        //查询缓存
        RoleCondition roleCondition = (RoleCondition) redisTemplate.opsForHash().get("allrole", String.valueOf(condition.getId()));
        if (roleCondition == null){
            //查询数据库
            roleCondition = mapper.selectByPrimaryKey(condition.getId());
            //写入缓存
            redisTemplate.opsForHash().put("allrole",String.valueOf(condition.getId()),roleCondition);
        }
        return roleCondition;
    }

    @Override
    /**
    *@Description: 保存角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response saveRole(RoleCondition condition) {
        Response response = this.getResponse();
        try {
            if (condition.getId() == null){
                condition.setCreateTime(new Date());
                //保存数据库
                mapper.insertSelective(condition);
                //保存缓存
                redisTemplate.opsForHash().put("allrole",String.valueOf(condition.getId()),condition);
                //加入过滤器
                bloomFilter.put(condition.getId());
            }else{
                mapper.updateByPrimaryKeySelective(condition);
                redisTemplate.opsForHash().put("allrole",String.valueOf(condition.getId()),condition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMsg("发生错误");
            response.setResult(1);
        }
        return response;
    }

    @Override
    public Response getRoleByUser(UserRoleCondition condition) {
        Response response = this.getResponse();
        List<UserRoleCondition> list = userRoleMapper.getRoleByUser(condition);
        response.setList(list);
        return response;
    }

    @Override
    /**
    *@Description: 删除角色
    *@Param: [ids]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response delRole(String ids) {
        Response response = this.getResponse();
        try {
            if (!StringUtils.isEmpty(ids)){
                //储存绑定角色的用户名
                StringBuffer buffer = new StringBuffer();
                if (ids.contains(",")){
                    for (String s : ids.split(",")) {
                        Integer parseInt = Integer.parseInt(s);
                        return new Response("超级管理不能删除!", 1);
                    }
                    Stream.of(ids.split(","))
                            .map(Integer::parseInt)
                            .forEach(i->{
                                //查询角色是否被用户绑定(查询绑定此角色的用户)
                                List<UserRoleCondition> list = userRoleMapper.selectByRole(i);
                                //未被绑定执行删除
                                if (list.isEmpty()){
                                    //删除数据库
                                    mapper.deleteByPrimaryKey(i);
                                    //删除中间表数据
                                    rolePowerMapper.deleteByRole(i);
                                    //删除缓存
                                    redisTemplate.opsForHash().delete("allrole",String.valueOf(i));
                                }else{
                                    //查询角色名
                                    RoleCondition roleCondition = mapper.selectByPrimaryKey(i);
                                    //查询绑定角色的用户
                                    list.forEach(r->{
                                        UserCondition userCondition = userMapper.selectByPrimaryKey(r.getUser());
                                        if (userCondition != null){
                                            buffer.append(roleCondition.getName()+"角色被"+userCondition.getName()+"绑定了!");
                                        }
                                    });
                                    response.setMsg(buffer.toString());
                                    response.setResult(1);
                                }

                            });
                }else{
                    if (Integer.parseInt(ids) == 1){
                        return new Response("超级管理不能删除!", 1);
                    }
                    //查询角色是否被用户绑定(查询绑定此角色的用户)
                    List<UserRoleCondition> list = userRoleMapper.selectByRole(Integer.parseInt(ids));
                    if (list.isEmpty()){
                        //删除数据库
                        mapper.deleteByPrimaryKey(Integer.parseInt(ids));
                        //删除缓存
                        redisTemplate.opsForHash().delete("allrole",String.valueOf(ids));
                    }else{
                        //查询角色名
                        RoleCondition roleCondition = mapper.selectByPrimaryKey(Integer.parseInt(ids));
                        //查询绑定角色的用户
                        list.forEach(r->{
                            UserCondition userCondition = userMapper.selectByPrimaryKey(r.getUser());
                            buffer.append(roleCondition.getName()+"角色被"+userCondition.getName()+"用户绑定了!");
                        });
                        response.setMsg(buffer.toString());
                        response.setResult(1);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMsg("发生错误");
        }
        return response;
    }

    /**
    *@Description: 获取 response
    *@Param: 
    *@return: 
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    private Response getResponse(){
        return new Response();
    }

    @PostConstruct
    private void init(){
        List<RoleCondition> role = mapper.getRole(new RoleCondition());
        if (!role.isEmpty()){
            //初始化布隆过滤器
            for (RoleCondition condition : role) {
                bloomFilter.put(condition.getId());
            }
        }
    }
}
