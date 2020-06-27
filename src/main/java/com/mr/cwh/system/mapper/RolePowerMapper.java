package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.RolePowerCondition;

import java.util.List;

public interface RolePowerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePowerCondition record);

    int insertSelective(RolePowerCondition record);

    RolePowerCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePowerCondition record);

    int updateByPrimaryKey(RolePowerCondition record);

    void deleteByRole(Integer role);

    List<RolePowerCondition> selectByRole(RolePowerCondition condition);

    List<RolePowerCondition> selectByPower(Integer i);
}