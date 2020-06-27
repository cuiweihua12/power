package com.mr.cwh.system.shiro;

import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.mapper.PowerMapper;
import com.mr.cwh.system.mapper.RoleMapper;
import com.mr.cwh.system.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: power
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-24 11:57
 */
public class MailRealm extends AuthorizingRealm {
    @Resource
    private UserMapper mapper;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PowerMapper powerMapper;
    /**
     * 为当前登录用户赋角色和权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录名
        String account = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //查询缓存
        Object o = redisTemplate.opsForValue().get(String.valueOf(account));
        UserCondition userCondition = null;
        if (o !=null){
            userCondition = (UserCondition) o;
        }

        if (userCondition == null){
            //通过账户查询账户信息
            userCondition = mapper.selectByEmail(account);
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

    /**
     * 验证当前登录用户
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomizedToken token = (CustomizedToken) authenticationToken;
        //获取登录邮箱
        Object email = token.getPrincipal();
        //根据邮箱查询数据库用户信息
        UserCondition userCondition = mapper.selectByEmail((String) email);
        AuthenticationInfo authenticationInfo = null;
        if (userCondition != null){
            //获取输入验证码
            /*String code = new String(token.getPassword());*/
            //获取缓存中验证码
            Object auth = request.getSession().getAttribute("auth");
            /*if (!code.equals(auth)){
                //不相同则抛出验证码不一致异常
                throw new IncorrectCredentialsException();
            }*/
            authenticationInfo = new SimpleAuthenticationInfo(userCondition.getAccount(),auth,userCondition.getName());
        }
        return authenticationInfo;
    }
}
