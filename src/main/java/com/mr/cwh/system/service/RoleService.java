package com.mr.cwh.system.service;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserRoleCondition;

/**
 * @program: power
 * @description: 业务层接口
 * @author: cuiweihua
 * @create: 2020-06-17 19:44
 */
public interface RoleService {
    Response getRole(RoleCondition condition);

    RoleCondition getRoleById(RoleCondition condition);

    Response saveRole(RoleCondition condition);

    Response delRole(String ids);

    Response getRoleByUser(UserRoleCondition condition);
}
