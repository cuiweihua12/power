package com.mr.cwh.system.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @program: power
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-24 13:50
 */
public class CustomizedModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        //判断realms是否为空
        assertRealmsConfigured();
        //获取登录类型
        CustomizedToken token = (CustomizedToken) authenticationToken;
        String type = token.getLoginType();
        //获取realms
        Collection<Realm> realms = getRealms();
        Collection<Realm> typeRealms = new ArrayList<>();
        realms.forEach(i->{
            if (i.getName().contains(type)){
                typeRealms.add(i);
            }
        });
        //判断单个还是多个
        if (typeRealms.size() == 1){
            return doSingleRealmAuthentication(typeRealms.iterator().next(),token);
        }else {
            return doMultiRealmAuthentication(realms,token);
        }
    }
}
