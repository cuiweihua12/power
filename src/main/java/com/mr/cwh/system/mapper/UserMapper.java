package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.UserCondition;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCondition record);

    int insertSelective(UserCondition record);

    UserCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCondition record);

    int updateByPrimaryKey(UserCondition record);

    List<UserCondition> getUser(UserCondition condition);

    Integer getUserCount(UserCondition condition);

    UserCondition selectByAccount(String account);

    UserCondition selectByIdAccount(UserCondition condition);

    UserCondition selectByEmail(String email);

    UserCondition selectByIdEmail(UserCondition condition);
}
