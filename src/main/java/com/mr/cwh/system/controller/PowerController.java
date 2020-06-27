package com.mr.cwh.system.controller;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RolePowerCondition;
import com.mr.cwh.system.service.PowerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: power
 * @description: 权限
 * @author: cuiweihua
 * @create: 2020-06-19 15:51
 */
@RestController
@RequestMapping("power")
public class PowerController {

    @Autowired
    private PowerService service;

    @GetMapping
    public Response getPowerAll(PowerCondition condition){
        return service.getPowerAll(condition);
    }

    @GetMapping("/byPidName")
    @RequiresPermissions("byPidName")
    public PowerCondition getPowerByPidAndName(PowerCondition condition){
        return service.getPowerByPidAndName(condition);
    }
    @GetMapping("byRole")
    @RequiresPermissions("byRole")
    public Response getPowerByRole(RolePowerCondition condition){
        return service.getPowerByRole(condition);
    }

    @GetMapping("byIdName")
    @RequiresPermissions("byIdName")
    public PowerCondition getPowerByIdName(PowerCondition condition){
        return service.getPowerByIdName(condition);
    }

    @PostMapping
    @RequiresPermissions("savePower")
    public Response savePower(@RequestBody  PowerCondition condition){
        return service.savePower(condition);
    }

    @PutMapping
    @RequiresPermissions("editPower")
    public Response editPower(@RequestBody PowerCondition condition){
        return service.savePower(condition);
    }

    @DeleteMapping
    @RequiresPermissions("delPower")
    public Response delPower(PowerCondition condition){
        return service.del(condition);
    }

    @DeleteMapping("batch")
    @RequiresPermissions("delPower")
    public Response batchDel(String ids){
        return service.batchDel(ids);
    }

    @PostMapping("saveRolePower")
    @RequiresPermissions("saveRolePower")
    public Response savePowerRole(@RequestBody  RolePowerCondition condition){
        return service.saveRolePower(condition);
    }
}
