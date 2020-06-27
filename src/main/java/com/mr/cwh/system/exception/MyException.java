package com.mr.cwh.system.exception;

import com.mr.cwh.system.base.Response;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.CryptoException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: power
 * @description: 没有权限异常处理
 * @author: cuiweihua
 * @create: 2020-06-22 15:31
 */
@ControllerAdvice
public class MyException {
    /**
     * 对应处理Shiro没有权限处理
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public String defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e){
        System.out.println("没有权限");
        return "redirect:/user/log";

    }

    /**
     * 处理没有登录
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = CryptoException.class)
    public String defaultNoLogin(HttpServletRequest request,HttpServletResponse response,Exception e){
        System.out.println("没有登录");
        return "login";
    }
}
