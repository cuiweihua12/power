package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.RoleCondition;
import com.mr.cwh.system.condition.UserRoleCondition;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleCondition record);

    int insertSelective(RoleCondition record);

    RoleCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleCondition record);

    int updateByPrimaryKey(RoleCondition record);

    List<RoleCondition> getRole(RoleCondition condition);

    List<RoleCondition> selectByUser(Integer id);

    RoleCondition getRoleByName(String name);
}