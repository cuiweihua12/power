package com.mr.cwh.system.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @program: power
 * @description:
 * @author: cuiweihua
 * @create: 2020-06-24 13:49
 */
public class CustomizedToken extends UsernamePasswordToken {

    //登录类型
    private String loginType;

    public CustomizedToken(final String username, final String password,String loginType) {
        super(username,password);
        this.loginType = loginType;
    }


    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
