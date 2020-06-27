package com.mr.cwh.system.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @program: power
 * @description: 比较验证码\
 * @author: cuiweihua
 * @create: 2020-06-24 17:33
 */
public class ShrioMail implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        CustomizedToken token = (CustomizedToken) authenticationToken;
        //获取登录验证码
        String auth = new String(token.getPassword());
        //获取缓存验证码
        String authCache = (String) authenticationInfo.getCredentials();
        if (auth.equals(authCache)){
            return true;
        }
        return false;
    }
}
