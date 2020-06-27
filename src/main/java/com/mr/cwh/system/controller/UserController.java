package com.mr.cwh.system.controller;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.condition.UserRoleCondition;
import com.mr.cwh.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.PUT;

/**
 * @author CWH
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService service;

    @RequestMapping("log")
    public Response log(){
        return new Response("没有权限",1);
    }

    @PostMapping("userAndRole")
    @RequiresPermissions("saveUserRole")
    /**
    *@Description: 保存用户角色关系
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/18
    */
    public Response saveUserRole(@RequestBody UserRoleCondition condition){
        return service.saveUserRole(condition);
    }

    @GetMapping("byId")
    @RequiresPermissions("getUserById")
    /**
    *@Description: 通过id查询
    *@Param: [condition]
    *@return: condition
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public UserCondition getUserById(UserCondition condition){
        return service.getUserById(condition);
    }

    @GetMapping("byIdAccount")
    @RequiresPermissions("byIdAccount")
    /**
    *@Description: 通过用户名和id查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public UserCondition getUserByIdAccount(UserCondition condition){
        return service.getUserByIdAccount(condition);
    }

    @GetMapping("byAccount")
    /**
     *@Description: 通过账户名查询
     *@Param: [condition]
     *@return: condition
     *@Author: cuiweihua
     *@date: 2020/6/17
     */
    public UserCondition getUserByAccount(UserCondition condition){
        return service.getUserByAccount(condition);
    }


    @GetMapping("byEmail")
    /**
    *@Description: 通过邮箱查询
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.UserCondition
    *@Author: cuiweihua
    *@date: 2020/6/24
    */
    public UserCondition getUserByEmail(UserCondition condition){
        return service.getUserByEmail(condition);
    }

    @GetMapping("byIdEmail")
    /**
     *@Description: 通过邮箱查询
     *@Param: [condition]
     *@return: com.mr.cwh.system.condition.UserCondition
     *@Author: cuiweihua
     *@date: 2020/6/24
     */
    public UserCondition getUserByIdEmail(UserCondition condition){
        return service.getUserByIdEmail(condition);
    }

    /**
    *@Description: 获取全部用户信息,可以排序和按条件查询
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    @GetMapping
    public Response getUser(UserCondition condition){
        return service.getUser(condition);
    }

    @PostMapping
    @RequiresPermissions("saveUser")
    /**
    *@Description: 新增
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response saveUser(@RequestBody UserCondition condition){
        return service.saveUser(condition);
    }

    @PutMapping
    @RequiresPermissions("editUser")
    /**
    *@Description: 修改
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response editUser(@RequestBody UserCondition condition){
        return service.saveUser(condition);
    }

    @DeleteMapping
    @RequiresPermissions("delUser")
    /**
    *@Description: 删除
    *@Param: [ids]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/16
    */
    public Response delUser(String ids){
        return service.delUser(ids);
    }


    @PostMapping("login")
    /**
    *@Description: 登录
    *@Param: [condition, request]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/20
    */
    public Response login(@RequestBody UserCondition condition, HttpServletRequest request){
        return service.login(condition,request);
    }

    @PostMapping("loginEmail")
    public Response loginEmail(@RequestBody UserCondition condition,HttpServletRequest request){
        return service.loginEmail(condition.getEmail(),condition.getAuthcode(),request);
    }

    @PostMapping("register")
    /**
    *@Description: 用户注册
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/21
    */
    public Response register(@RequestBody UserCondition condition){
        return service.register(condition);
    }

    @GetMapping("loginout")
    /**
    *@Description: shiro注销登录
    *@Param: []
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/22
    */
    public Response loginOut(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //清除session
        request.getSession().invalidate();
        return new Response();
    }

    @GetMapping(value="updataSessionImg")
    /**
    *@Description: 修改头像后调用此方法修改session中的url
    *@Param: [request, url]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/22
    */
    public Response updataSessionImg(HttpServletRequest request,String url){
        HttpSession session = request.getSession();
        UserCondition userCondition = (UserCondition) session.getAttribute("user");
        userCondition.setUrl(url);
        session.setAttribute("user", userCondition);
        return new Response();
    }

    @PutMapping("editHead")
    public Response editHeah(@RequestBody UserCondition condition){
        return service.saveUser(condition);
    }
}
