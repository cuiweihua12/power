package com.mr.cwh.system.service;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.UserCondition;
import com.mr.cwh.system.condition.UserRoleCondition;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Response getUser(UserCondition condition);

    Response saveUser(UserCondition condition);

    Response delUser(String ids);

    UserCondition getUserById(UserCondition condition);

    Response saveUserRole(UserRoleCondition condition);

    UserCondition getUserByAccount(UserCondition condition);

    UserCondition getUserByIdAccount(UserCondition condition);

    Response login(UserCondition condition, HttpServletRequest request);

    Response register(UserCondition condition);

    UserCondition getUserByEmail(UserCondition condition);

    Response loginEmail(String email,String authCode, HttpServletRequest request);

    UserCondition getUserByIdEmail(UserCondition condition);
}
