package com.mr.cwh.system.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.condition.UserRoleCondition;
import com.mr.cwh.system.mapper.PowerMapper;
import com.mr.cwh.system.mapper.RoleMapper;
import com.mr.cwh.system.mapper.UserMapper;
import com.mr.cwh.system.mapper.UserRoleMapper;
import com.mr.cwh.system.service.UserService;
import com.mr.cwh.system.shiro.CustomizedToken;
import com.mr.cwh.system.utils.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author CWH
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper mapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedisTemplate<String,Object> redisCount;


    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PowerMapper powerMapper;



    private BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(),10000,0.01);

    private BloomFilter<String> stringBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),10000,0.01);

    private BloomFilter<String> accountIdBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),10000,0.01);

    private RedisTemplate getRedisCount(){
        redisCount.setStringSerializer(new StringRedisSerializer());
        return redisCount;
    }

    @Override
    /**
    *@Description: 通过用户id和邮箱查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/26
    */
    public UserCondition getUserByIdEmail(UserCondition condition) {
        if (stringBloomFilter.mightContain(condition.getId()+condition.getEmail())){
            //查缓存
            Object user = redisTemplate.opsForValue().get(String.valueOf(condition.getId()+condition.getEmail()));
            if (user == null){
                //查数据库
                UserCondition userCondition = mapper.selectByIdEmail(condition);
                //存入缓存
                redisTemplate.opsForValue().set(String.valueOf(condition.getId()+condition.getEmail()),userCondition);
                //设置5分钟后缓存过期
                redisTemplate.expire(String.valueOf(condition.getId()+condition.getEmail()),5,TimeUnit.MINUTES);
                return userCondition;
            }
            return (UserCondition) user;
        }
        return null;
    }

    @Override
    /**
    *@Description: 通过邮箱查询用户信息
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/24
    */
    public UserCondition getUserByEmail(UserCondition condition) {
        //过滤
        if (stringBloomFilter.mightContain(condition.getEmail())) {
            //查缓存
            Object user = redisTemplate.opsForValue().get(String.valueOf(condition.getEmail()));
            if (user == null){
                //查数据库
                UserCondition userCondition = mapper.selectByEmail(condition.getEmail());
                if (userCondition != null){
                    //查询拥有角色
                    List<RoleCondition> roleConditions = roleMapper.selectByUser(userCondition.getId());
                    //存储权限
                    List<String> url = new ArrayList<>();
                    //储存角色名
                    StringBuffer buffer = new StringBuffer();
                    //查询拥有权限 遍历角色 查询角色拥有权限
                    for (RoleCondition i : roleConditions) {
                        List<PowerCondition> powerConditions = powerMapper.selectByRole(i.getId());
                        buffer.append(i.getName()+",");
                        for (PowerCondition p : powerConditions) {
                            url.add(p.getUrl());
                        }
                    }
                    //存入权限
                    userCondition.setPowers(url);
                    if(buffer.length() != 0){
                        userCondition.setRoleName(buffer.substring(0,buffer.lastIndexOf(",")));
                    }
                }
                //存入缓存
                redisTemplate.opsForValue().set(String.valueOf(condition.getEmail()),userCondition);
                //设置5分钟后缓存过期
                redisTemplate.expire(String.valueOf(condition.getEmail()),5,TimeUnit.MINUTES);
                return userCondition;
            }
            return (UserCondition) user;
        }
        return null;
    }

    @Override
    /**
    *@Description: 通过用户名查询数据
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public UserCondition getUserByAccount(UserCondition condition) {
        //查询过滤器
         if (stringBloomFilter.mightContain(condition.getAccount())) {
             //查询缓存
            Object userall = redisTemplate.opsForValue().get(String.valueOf(condition.getAccount()));
            if (userall == null){
                //查询数据库
                UserCondition userCondition = mapper.selectByAccount(condition.getAccount());
                if (userCondition != null){
                    //查询拥有角色
                    List<RoleCondition> roleConditions = roleMapper.selectByUser(userCondition.getId());
                    //存储权限
                    List<String> url = new ArrayList<>();
                    //储存角色名
                    StringBuffer buffer = new StringBuffer();
                    //查询拥有权限 遍历角色 查询角色拥有权限
                    for (RoleCondition i : roleConditions) {
                        List<PowerCondition> powerConditions = powerMapper.selectByRole(i.getId());
                        buffer.append(i.getName()+",");
                        for (PowerCondition p : powerConditions) {
                            url.add(p.getUrl());
                        }
                    }
                    //存入权限
                    userCondition.setPowers(url);
                    if(buffer.length() != 0){
                        userCondition.setRoleName(buffer.substring(0,buffer.lastIndexOf(",")));
                    }
                }
                //存入缓存
                redisTemplate.opsForValue().set(String.valueOf(condition.getAccount()),userCondition);
                //设置5分钟后缓存过期
                redisTemplate.expire(String.valueOf(condition.getAccount()),5,TimeUnit.MINUTES);
                return userCondition;
            }
            return (UserCondition) userall;
        }
        return  null;
    }

    @Override
    /**
    *@Description: 通过用户名和id查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public UserCondition getUserByIdAccount(UserCondition condition) {
        //查询过滤器
        if (accountIdBloomFilter.mightContain(condition.getId()+condition.getAccount())) {
            //查询缓存
            Object o = redisTemplate.opsForValue().get(String.valueOf(condition.getId() + condition.getAccount()));
            if (o == null){
                //查询数据库
                UserCondition userCondition = mapper.selectByIdAccount(condition);
                if (userCondition == null){
                    //存入缓存
                    redisTemplate.opsForValue().set(String.valueOf(condition.getId()+condition.getAccount()),null);
                }else {
                    //存入缓存
                    redisTemplate.opsForValue().set(String.valueOf(condition.getId()+condition.getAccount()),userCondition);
                }
                //设置缓存过期时间
                redisTemplate.expire(String.valueOf(condition.getId()+condition.getAccount()),5000, TimeUnit.MILLISECONDS);
                return userCondition;
            }
            return (UserCondition) o;
        }
        return null;
    }

    @Override
    /**
    *@Description: 保存用户角色关系
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/18
    */
    public Response saveUserRole(UserRoleCondition condition) {
        Response response = this.getResponse();
        try {
        //删除数据库中与user有关的角色关系
        userRoleMapper.delAllByUser(condition.getUser());
        //判断角色是否为空
            if (!StringUtils.isEmpty(condition.getRoles())){
                if (condition.getRoles().contains(",")){
                    //保存用户角色
                    Stream.of(condition.getRoles().split(","))
                            .map(Integer::parseInt)
                            .forEach(i->{
                                UserRoleCondition roleCondition = new UserRoleCondition();
                                roleCondition.setRole(i);
                                roleCondition.setUser(condition.getUser());
                                userRoleMapper.insertSelective(roleCondition);
                            });
                }else{
                    //保存用户角色
                    UserRoleCondition roleCondition = new UserRoleCondition();
                    roleCondition.setUser(condition.getUser());
                    roleCondition.setRole(Integer.parseInt(condition.getRoles()));
                    userRoleMapper.insertSelective(roleCondition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMsg("发生错误");
            response.setResult(1);
        }
        return response;
    }


    /**
    *@Description: 获取response
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    private Response getResponse(){
        return new Response();
    }

    @Override
    /**
    *@Description: 查询全部
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response getUser(UserCondition condition) {
        Response response = this.getResponse();
        //查询缓存 用户信息
        Set<Object> range;
        //缓存分页
        if (condition.getOffset() == 0){
            range = redisTemplate.opsForZSet().range("getUser", condition.getOffset(), condition.getLimit()-1);
        }else{
            range = redisTemplate.opsForZSet().range("getUser", condition.getOffset(), condition.getLimit()+condition.getOffset()-1);
        }
        List<Object> all = new ArrayList<>();
        for (Object r : range) {
            Object userall = redisTemplate.opsForHash().get("userall", String.valueOf(r));
            all.add(userall);
        }
      /*  //查询缓存
        List<Object> all = redisTemplate.opsForHash().values("userall").stream().collect(Collectors.toList());*/
        if (all.isEmpty()){

            //查询数据库
            List<UserCondition> list = mapper.getUser(condition);
            //条数
            Integer count = mapper.getUserCount(condition);
            //存入缓存
            this.getRedisCount().opsForValue().set("getUserCount",count);
            list.forEach(i->{
                redisTemplate.opsForZSet().add("getUser",i.getId(),i.getId());
                redisTemplate.opsForHash().put("userall",String.valueOf(i.getId()),i);
            });
            /*//设置过期时间
            redisTemplate.expire("all",10,TimeUnit.MINUTES);*/
            response.setList(list);
            response.setTotal(count);
            return response;
        }
        //查询缓存总条数
        Integer count = (Integer) this.getRedisCount().opsForValue().get("getUserCount");
        response.setList(all);
        response.setTotal(count);
        return response;
    }

    @Override
    /**
    *@Description: 保存用户
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response saveUser(UserCondition condition) {
        Response response = this.getResponse();
        try {
            if (!Optional.ofNullable(condition.getId()).isPresent()){

                //判断数据库中是否存
                Optional<UserCondition> optional = Optional.ofNullable(mapper.selectByAccount(condition.getAccount()));
                if (!optional.isPresent()){
                    //加密密码
                    condition.setPassword(PasswordUtil.encode(condition.getPassword()));
                    condition.setCreateTime(new Date());
                    condition.setRemove(1);
                    //插入数据库
                    mapper.insertSelective(condition);
                    //插入过滤器
                    stringBloomFilter.put(condition.getAccount());
                    stringBloomFilter.put(condition.getId()+condition.getEmail());
                    bloomFilter.put(condition.getId());
                    accountIdBloomFilter.put(condition.getId()+condition.getAccount());
                    //插入缓存
                    //缓存数量 +1
                    Integer count = (Integer) this.getRedisCount().opsForValue().get("getUserCount");
                    this.getRedisCount().opsForValue().set("getUserCount",count+1);
                    redisTemplate.opsForZSet().add("getUser",condition.getId(),condition.getId());
                    redisTemplate.opsForHash().put("userall",String.valueOf(condition.getId()),condition);
                }else {
                    return new Response("已存在",1);
                }

            }else {
                //修改数据库值
                mapper.updateByPrimaryKeySelective(condition);
                //加入过滤器
                /*stringBloomFilter.put(condition.getAccount());
                accountIdBloomFilter.put(condition.getId()+condition.getAccount());*/
                //查询数据库值
                UserCondition userCondition = mapper.selectByPrimaryKey(condition.getId());
                //修改缓存值
                redisTemplate.opsForHash().put("userall",String.valueOf(condition.getId()),userCondition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMsg("发生错误");
            response.setResult(1);
        }
        return response;
    }

    @Override
    /**
    *@Description: 批量删除和 单删
    *@Param: [ids]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response delUser(String ids) {
        Response response = this.getResponse();
        try {
            if (Optional.ofNullable(ids).isPresent()){
                if (ids.contains(",")){
                    Stream.of(ids.split(",")).map(Integer::parseInt).forEach(i->{
                        //删除数据库中数据
                        mapper.deleteByPrimaryKey(i);
                        //删除中间表数据
                        userRoleMapper.delAllByUser(i);
                        //缓存数量 -1
                        Integer count = (Integer) this.getRedisCount().opsForValue().get("getUserCount");
                        this.getRedisCount().opsForValue().set("getUserCount",count-1);
                        //删除缓存中数据
                        redisTemplate.opsForZSet().remove("getUser",i);
                        redisTemplate.opsForHash().delete("userall",String.valueOf(i));
                    });
                }else {
                    //删除用户表
                    mapper.deleteByPrimaryKey(Integer.parseInt(ids));
                    //删除中间表
                    userRoleMapper.delAllByUser(Integer.parseInt(ids));
                    //缓存数量 -1
                    Integer count = (Integer) this.getRedisCount().opsForValue().get("getUserCount");
                    this.getRedisCount().opsForValue().set("getUserCount",count-1);
                    redisTemplate.opsForZSet().remove("getUser",Integer.parseInt(ids));
                    redisTemplate.opsForHash().delete("userall",String.valueOf(ids));
                }
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
    *@Description: 通过用户id查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public UserCondition getUserById(UserCondition condition) {
        //查询过滤器
        if (!bloomFilter.mightContain(condition.getId())){
            UserCondition userCondition = new UserCondition();
            userCondition.setMsg("无信息");
            return userCondition;
        }
        Object userall = redisTemplate.opsForHash().get("userall", String.valueOf(condition.getId()));
        if (userall == null){

            //查询缓存
            userall = redisTemplate.opsForHash().get("userall", String.valueOf(condition.getId()));
            if (userall == null){
                //查询数据库
                UserCondition userCondition = mapper.selectByPrimaryKey(condition.getId());
                //存入缓存
                redisTemplate.opsForHash().put("userall",String.valueOf(condition.getId()),userCondition);
                return userCondition;
            }
        }
        return (UserCondition) userall;
    }

    @Override
    public Response loginEmail(String email,String authCode, HttpServletRequest request) {
        //查询邮箱是否存在
        if (stringBloomFilter.mightContain(email)){
            CustomizedToken token = new CustomizedToken(email,authCode,"Mail");
            Subject subject = SecurityUtils.getSubject();
            try {
                if (!token.isRememberMe()){
                    token.setRememberMe(true);
                    subject.login(token);
                    //查询缓存
                    UserCondition user = (UserCondition) redisTemplate.opsForValue().get(String.valueOf(email));
                    request.getSession().setAttribute("user",user);
                }
            } catch (UnknownAccountException e) {
                System.out.println("用户不存在");
                return new Response("用户不存在",1);
            }catch (IncorrectCredentialsException e){
                System.out.println("验证码错误");
                return new Response("验证码错误",1);
            }
            return new Response();
        }
        return new Response("用户不存在",1);
    }

    @Override
    public Response login(UserCondition condition, HttpServletRequest request) {
        //查询账号是否存在
        if (stringBloomFilter.mightContain(condition.getAccount())) {
            Response response = new Response();
            UsernamePasswordToken token = new CustomizedToken(condition.getAccount(),condition.getPassword(),"Password");
            Subject subject = SecurityUtils.getSubject();
            try {
                if (!token.isRememberMe()){
                    token.setRememberMe(true);
                    subject.login(token);
                    //查询缓存
                    UserCondition user = (UserCondition) redisTemplate.opsForValue().get(String.valueOf(condition.getAccount()));
                    //逻辑上到这一步缓存不可能为空,但是为了严谨,加上查询数据库
                    if (user == null){
                        //查询拥有角色
                        List<RoleCondition> roleConditions = roleMapper.selectByUser( condition.getId());
                        //储存角色名
                        StringBuffer buffer = new StringBuffer();
                        //查询拥有权限 遍历角色 查询角色拥有权限
                        for (RoleCondition i : roleConditions) {
                            buffer.append(i.getName());
                        }
                        //存入角色名
                        if(buffer.length() != 0){
                            user.setRoleName(buffer.substring(0,buffer.lastIndexOf(",")));
                        }
                    }
                    request.getSession().setAttribute("user",user);
                }
            } catch (UnknownAccountException e) {
                System.out.println("账户不存在");
                response.setResult(1);
                response.setMsg("账户不存在");
            } catch (IncorrectCredentialsException e){
                System.out.println("密码错误");
                response.setMsg("密码错误");
                response.setResult(1);
            }
            return response;

        }
        return new Response("账号不存在",1);
    }

    /* @Override
    *//**
    *@Description: 登录
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/20
    *//*
    public Response login(UserCondition condition, HttpServletRequest request) {
        //查询账号是否存在  过滤器
        if (stringBloomFilter.mightContain(condition.getAccount())) {
            //查询缓存
            UserCondition user = (UserCondition) redisTemplate.opsForValue().get(String.valueOf(condition.getAccount()));
            if (user == null){
                //查询数据库
                UserCondition account = mapper.selectByAccount(condition.getAccount());
                if (account == null ){
                    return new Response("账号不存在",1);
                }
                //查询拥有角色
                List<RoleCondition> roleConditions = roleMapper.selectByUser(account.getId());
                //存储权限
                List<String> url = new ArrayList<>();
                //储存角色名
                StringBuffer buffer = new StringBuffer();
                //查询拥有权限 遍历角色 查询角色拥有权限
                for (RoleCondition i : roleConditions) {
                    List<PowerCondition> powerConditions = powerMapper.selectByRole(i.getId());
                    buffer.append(i.getName()+",");
                    for (PowerCondition p : powerConditions) {
                        url.add(p.getUrl());
                    }
                }
                //存入角色名
                account.setRoleName(buffer.substring(0,buffer.lastIndexOf(",")));
                //存入权限
                account.setPowers(url);
                //存入缓存
                redisTemplate.opsForValue().set(String.valueOf(condition.getAccount()),account);
                //设置缓存过期
                redisTemplate.expire(String.valueOf(condition.getAccount()),5,TimeUnit.MINUTES);
                //验证密码是否正确
                if (PasswordUtil.comparePassword(condition.getPassword(),account.getPassword())){
                    //将用户存入session
                    request.getSession().setAttribute("user",account);
                    return new Response();
                }
                //密码错误
                return new Response("密码错误",1);
            }else{
                if (PasswordUtil.comparePassword(condition.getPassword(),user.getPassword())){
                    //将用户存入session
                    request.getSession().setAttribute("user",user);
                    //登录成功
                    return new Response();
                }else {
                    return new Response("密码错误",1);
                }
            }
        }
        return new Response("账号不存在",1);
    }*/

    @Override
    /**
    *@Description: 用户注册
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/21
    */
    public Response register(UserCondition condition) {
        //查询账户是否存在 过滤器不存在则一定不存在
        if (stringBloomFilter.mightContain(condition.getAccount())) {
            return new Response("账户已存在",1);
        }
        //注册
        condition.setPassword(PasswordUtil.encode(condition.getPassword()));
        condition.setRemove(1);
        condition.setCreateTime(new Date());
        mapper.insertSelective(condition);
        //加入过滤器
        stringBloomFilter.put(condition.getAccount());
        stringBloomFilter.put(condition.getEmail());
        stringBloomFilter.put(condition.getId()+condition.getEmail());
        bloomFilter.put(condition.getId());
        accountIdBloomFilter.put(condition.getId()+condition.getAccount());
        return new Response("注册成功",0);
    }

    @PostConstruct
   /**
   *@Description: 初始化布隆过滤器
   *@Param: []
   *@return: void
   *@Author: cuiweihua
   *@date: 2020/6/18
   */
    public void init(){
       List<UserCondition> user = mapper.getUser(new UserCondition());
       if (!user.isEmpty()){
           for (UserCondition condition : user) {
               bloomFilter.put(condition.getId());
               stringBloomFilter.put(condition.getAccount());
               stringBloomFilter.put(condition.getEmail());
               stringBloomFilter.put(condition.getId()+condition.getEmail());
               accountIdBloomFilter.put(condition.getId()+condition.getAccount());
           }
       }
   }

}
