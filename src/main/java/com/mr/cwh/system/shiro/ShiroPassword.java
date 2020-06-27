package com.mr.cwh.system.shiro;

import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.utils.PasswordUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.security.AuthProvider;
import java.util.List;
import java.util.stream.Stream;

/**
 * @program: power
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-22 11:45
 */
public class ShiroPassword implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //获取当前登录用户
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //登录密码
        String password = new String(token.getPassword());
        //数据库密码
        Object infoCredentials = authenticationInfo.getCredentials();
        //比较密码是否一致
        return PasswordUtil.comparePassword(password, (String) infoCredentials);
    }
}
