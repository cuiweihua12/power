package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.PowerCondition;

import java.util.List;

public interface PowerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PowerCondition record);

    int insertSelective(PowerCondition record);

    PowerCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PowerCondition record);

    int updateByPrimaryKey(PowerCondition record);

    List<PowerCondition> select(PowerCondition condition);

    List<PowerCondition> selectByRole(Integer id);

    PowerCondition selectByPidAndName(PowerCondition condition);

    PowerCondition selectByIdAndName(PowerCondition condition);
}