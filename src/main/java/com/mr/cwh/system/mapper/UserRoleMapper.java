package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserRoleCondition;

import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleCondition record);

    int insertSelective(UserRoleCondition record);

    UserRoleCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRoleCondition record);

    int updateByPrimaryKey(UserRoleCondition record);

    void delAllByUser(Integer user);

    List<UserRoleCondition> getRoleByUser(UserRoleCondition condition);

    List<UserRoleCondition> selectByRole(Integer i);

}