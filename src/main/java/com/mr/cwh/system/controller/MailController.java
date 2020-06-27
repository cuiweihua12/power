package com.mr.cwh.system.controller;

import com.mr.cwh.system.condition.UserCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: power
 * @description: 发送邮件
 * @author: cuiweihua
 * @create: 2020-06-24 10:47
 */
@RestController
@RequestMapping("mail")
public class MailController {
    @Autowired
    private JavaMailSender javaMailSender;
    @Resource
    private HttpServletRequest request;

    /**
     * 发送邮件账户
     */
    private final String emailFrom = "1119033704@qq.com";


    @RequestMapping("send")
    public Object senMsg(UserCondition condition){
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人的邮箱地址
        message.setFrom(emailFrom);
        //收件人的邮箱地址
        message.setTo(condition.getEmail());
        //邮件主题
        message.setSubject("登录验证");
        //验证码
        int authCode = (int) ((Math.random() * 9 + 1) * 100000);
        //存入session
        request.getSession().setAttribute("auth",String.valueOf(authCode));
        //邮件内容
        message.setText("验证码："+authCode);
        //发送邮件
        javaMailSender.send(message);
        System.out.println("发送成功");
        return "success";
    }


}
