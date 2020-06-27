package com.mr.cwh.system.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.RolePowerCondition;
import com.mr.cwh.system.mapper.PowerMapper;
import com.mr.cwh.system.mapper.RoleMapper;
import com.mr.cwh.system.mapper.RolePowerMapper;
import com.mr.cwh.system.service.PowerService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @program: power
 * @description: 权限业务
 * @author: cuiweihua
 * @create: 2020-06-19 15:50
 */
@Service
public class PowerServiceImpl implements PowerService {

    @Resource
    private PowerMapper mapper;

    @Resource
    private RolePowerMapper rolePowerMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),10000,0.01);

    @PostConstruct
    /**
    *@Description: 初始化布隆过滤器
    *@Param: []
    *@return: void
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public void init(){
        mapper.select(new PowerCondition()).forEach(i->{
            bloomFilter.put(i.getPid()+i.getName());
            bloomFilter.put(i.getId()+i.getName());
        });
    }

    @Override
    /**
    *@Description: 获取所有权限
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public Response getPowerAll(PowerCondition condition) {
        Response response = new Response();
        List<Object> allPower = redisTemplate.opsForHash().values("allPower");
        if (allPower.isEmpty()){
            List<PowerCondition> list = mapper.select(condition);
            for (PowerCondition powerCondition : list) {
                redisTemplate.opsForHash().put("allPower",String.valueOf(powerCondition.getId()),powerCondition);
            }
            response.setList(list);
            return response;
        }
        response.setList(allPower);
        return response;
    }

    @Override
    /**
    *@Description: 通过角色查询 角色拥有的权限
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public Response getPowerByRole(RolePowerCondition condition) {
        List<RolePowerCondition> list = rolePowerMapper.selectByRole(condition);
        return new Response(list);
    }

    @Override
    public PowerCondition getPowerByIdName(PowerCondition condition) {
        //查询过滤器
        if (bloomFilter.mightContain(condition.getId()+condition.getName())) {
            //查询缓存
            Object o = redisTemplate.opsForValue().get(condition.getId() + condition.getName());
            if (o == null){
                //查询数据库
                PowerCondition powerCondition = mapper.selectByIdAndName(condition);
                //放入缓存
                redisTemplate.opsForValue().set(condition.getId()+condition.getName(),powerCondition);
                //设置过期时间
                redisTemplate.expire(condition.getId()+condition.getName(),5,TimeUnit.MINUTES);
                return powerCondition;
            }
            return (PowerCondition) o;
        }
        return null;
    }

    @Override
    /**
    *@Description: 通过pid和姓名查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.PowerCondition
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public PowerCondition getPowerByPidAndName(PowerCondition condition) {
        //查看是否存在过滤器
        if (bloomFilter.mightContain(condition.getPid()+condition.getName())) {
            //查询缓存
            Object o = redisTemplate.opsForValue().get(condition.getPid() + condition.getName());
            if (o == null){
                //查询数据库
                PowerCondition powerCondition = mapper.selectByPidAndName(condition);
                //存入缓存
                redisTemplate.opsForValue().set(condition.getPid()+condition.getName(),powerCondition);
                //设置缓存过期时间 5分钟
                redisTemplate.expire(condition.getPid()+condition.getName(),5, TimeUnit.MINUTES);

                return powerCondition;
            }
            return (PowerCondition) o;
        }
        return null;
    }

    @Override
    /**
    *@Description: 保存权限
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public Response savePower(PowerCondition condition) {
        Response response = new Response();
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        try {
            if (!StringUtils.isEmpty(condition.getId())){
                //修改权限
                mapper.updateByPrimaryKeySelective(condition);
                stringObjectObjectHashOperations.put("allPower",String.valueOf(condition.getId()),condition);
            }else{
                //新增权限
                int i = mapper.insertSelective(condition);
                //将此权限赋给超级管理
                RolePowerCondition rolePowerCondition = new RolePowerCondition();
                rolePowerCondition.setPower(condition.getId());
                //超级管理id为1 或者查询着急管理
                RoleCondition roleCondition = roleMapper.getRoleByName("超级管理员");
                rolePowerCondition.setRole(roleCondition.getId());
                rolePowerMapper.insertSelective(rolePowerCondition);
                stringObjectObjectHashOperations.put("allPower",String.valueOf(condition.getId()),condition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMsg("发生错误");
        }
        return response;
    }

    @Override
    /**
    *@Description: 删除权限
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public Response del(PowerCondition condition) {
        Response response = new Response();
        try {
            if (condition.getId()!= null){
                if(condition.getId() == 1){return new Response("根节点不能删除",1);}
                StringBuffer buffer = new StringBuffer();
                //查询角色与权限关系(有无角色绑定了该权限)
                List<RolePowerCondition> list  = rolePowerMapper.selectByPower(condition.getId());
                if (list.isEmpty()){
                    //删除数据库
                    redisTemplate.opsForHash().delete("allPower",String.valueOf(condition.getId()));
                    //删除缓存
                    mapper.deleteByPrimaryKey(condition.getId());
                }else{
                    //查询权限名
                    PowerCondition powerCondition = mapper.selectByPrimaryKey(condition.getId());
                    list.forEach(r->{
                        RoleCondition roleCondition = roleMapper.selectByPrimaryKey(r.getRole());
                        buffer.append(powerCondition.getName()+"权限被:"+roleCondition.getName()+"角色绑定了! ");
                    });
                    response.setMsg(buffer.toString());
                    response.setResult(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMsg("error");
            response.setResult(1);
        }

        return response;
    }

    @Override
    /**
    *@Description: 批量删除权限
    *@Param: [ids]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public Response batchDel(String ids) {
        Response response = new Response();
        try {
            if (!StringUtils.isEmpty(ids)){
                //储存绑定权限角色信息
                StringBuffer buffer = new StringBuffer();
                if (ids.contains(",")){
                    for (String s : ids.split(",")) {
                        Integer parseInt = Integer.parseInt(s);
                        if (parseInt == 1){ return  new Response("根节点不能删除",1);}
                    }
                    Stream.of(ids.split(","))
                            .map(Integer::parseInt)
                            .forEach(i->{
                                //查询角色与权限关系
                                List<RolePowerCondition> list  = rolePowerMapper.selectByPower(i);
                                //如无角色绑定
                                if (list.isEmpty()){
                                    //删除数据库
                                    mapper.deleteByPrimaryKey(i);
                                    //删除缓存
                                    redisTemplate.opsForHash().delete("allPower",String.valueOf(i));
                                }else {
                                    //查询权限名
                                    PowerCondition powerCondition = mapper.selectByPrimaryKey(i);
                                    //遍历绑定权限的信息
                                    list.forEach(r->{
                                        RoleCondition roleCondition = roleMapper.selectByPrimaryKey(r.getRole());
                                        buffer.append(powerCondition.getName()+"权限被:"+roleCondition.getName()+"角色绑定了! ");
                                    });
                                    response.setResult(1);
                                    response.setMsg(buffer.toString());
                                }

                            });

                }else{
                    if (Integer.parseInt(ids) == 1){return new Response("根节点不能删除",1);}
                    //查询角色与权限关系(有无角色绑定了该权限)
                    List<RolePowerCondition> list  = rolePowerMapper.selectByPower(Integer.getInteger(ids));
                    //如没有
                    if (list.isEmpty()){
                        //删除数据库
                        mapper.deleteByPrimaryKey(Integer.parseInt(ids));
                        //删除缓存
                        redisTemplate.opsForHash().delete("allPower",String.valueOf(ids));
                    }else {
                        //查询权限名
                        PowerCondition powerCondition = mapper.selectByPrimaryKey(Integer.parseInt(ids));
                        list.forEach(r->{
                            RoleCondition roleCondition = roleMapper.selectByPrimaryKey(r.getRole());
                            buffer.append(powerCondition.getName()+"权限被:"+roleCondition.getName()+"角色绑定了! ");
                        });
                        response.setResult(1);
                        response.setMsg(buffer.toString());
                    }

                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setMsg("发生错误");
            response.setResult(1);
        }
        return response;
    }

    @Override
    /**
    *@Description: 保存角色拥有权限
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/19
    */
    public Response saveRolePower(RolePowerCondition condition) {
        Response response = new Response();
        rolePowerMapper.deleteByRole(condition.getRole());
        try {
            if (!StringUtils.isEmpty(condition.getPowers())){
                if (condition.getPowers().contains(",")){
                    Arrays.stream(condition.getPowers().split(","))
                            .map(Integer::parseInt)
                            .forEach(i->{
                                RolePowerCondition rolePowerCondition = new RolePowerCondition();
                                rolePowerCondition.setPower(i);
                                rolePowerCondition.setRole(condition.getRole());
                                rolePowerMapper.insertSelective(rolePowerCondition);
                            });
                }else{
                    RolePowerCondition rolePowerCondition = new RolePowerCondition();
                    rolePowerCondition.setPower(Integer.parseInt(condition.getPowers()));
                    rolePowerCondition.setRole(condition.getRole());
                    rolePowerMapper.insertSelective(rolePowerCondition);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMsg("error");

        }
        return response;
    }
}
