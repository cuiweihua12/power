package com.mr.cwh.system.controller;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserRoleCondition;
import com.mr.cwh.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: power
 * @description: 角色控制
 * @author: cuiweihua
 * @create: 2020-06-17 19:43
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService service;

    @GetMapping
    /**
    *@Description: 获取角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response getRole(RoleCondition condition){
        return service.getRole(condition);
    }

    @GetMapping("/byId")
    @RequiresPermissions("byId")
    /**
    *@Description: 通过id获取角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.condition.RoleCondition
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public RoleCondition getRoleById(RoleCondition condition){
        return service.getRoleById(condition);
    }

    @GetMapping("/byUser")
    @RequiresPermissions("byUser")
    /**
    *@Description: 通过user获取角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/18
    */
    public Response getRoleByUser(UserRoleCondition condition){
        return service.getRoleByUser(condition);
    }
    
    @PostMapping
    @RequiresPermissions("saveRole")
    /**
    *@Description: 保存角色
    *@Param: [condition]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response saveRole(@RequestBody RoleCondition condition){
        return service.saveRole(condition);
    }

    @PutMapping
    @RequiresPermissions("editRole")
    public Response editRole(@RequestBody RoleCondition condition){
        return service.saveRole(condition);
    }

    @DeleteMapping
    @RequiresPermissions("delRole")
    /**
    *@Description: 删除角色
    *@Param: [ids]
    *@return: com.mr.cwh.system.base.Response
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public Response delRole(String ids){
        return service.delRole(ids);
    }

}
