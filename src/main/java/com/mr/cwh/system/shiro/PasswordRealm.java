package com.mr.cwh.system.shiro;

import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.mapper.PowerMapper;
import com.mr.cwh.system.mapper.RoleMapper;
import com.mr.cwh.system.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @program: Shiro
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-22 11:44
 */
public class PasswordRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PowerMapper powerMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录名
        String account = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //查询缓存
        Object o = redisTemplate.opsForValue().get(String.valueOf(account));
        UserCondition userCondition = null ;
        if (o != null){
            userCondition = (UserCondition) o;
        }
        if (userCondition == null){
            //通过账户查询账户信息
            userCondition = userMapper.selectByAccount(account);
            //存入缓存
            redisTemplate.opsForValue().set(String.valueOf(account),userCondition);
            //设置缓存过期
            redisTemplate.expire(String.valueOf(account),5, TimeUnit.MINUTES);
        }
        if (userCondition != null){
            //查询拥有的角色
            List<RoleCondition> roleConditions = roleMapper.selectByUser(userCondition.getId());
            if (!roleConditions.isEmpty()){
                for (RoleCondition i : roleConditions) {
                    //将角色名添加到shiro
                    info.addRole(i.getName());
                    //通过角色查询拥有的权限
                    List<PowerCondition> powerConditions = powerMapper.selectByRole(i.getId());
                    if (!powerConditions.isEmpty()) {
                        //将权限添加到shiro
                        powerConditions.stream()
                                .filter(p -> !StringUtils.isEmpty(p.getAuth()))
                                .map(PowerCondition::getAuth)
                                .forEach(info::addStringPermission);
                    }
                }
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取当前登录名
        Object account = authenticationToken.getPrincipal();
        //查询缓存
        UserCondition userCondition = (UserCondition) redisTemplate.opsForValue().get(String.valueOf(account));
        if (userCondition == null){
            //查询数据库中信息
            userCondition = userMapper.selectByAccount((String) account);
            //设置缓存过期
            redisTemplate.expire(String.valueOf(account),5, TimeUnit.MINUTES);
        }

        //将数据存入shiro
        AuthenticationInfo info = null;
        if (userCondition != null){
            info = new SimpleAuthenticationInfo(userCondition.getAccount(),userCondition.getPassword(),userCondition.getPhone());
        }
        return info;
    }
}
