package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.DeptCondition;

import java.util.List;

public interface DeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeptCondition record);

    int insertSelective(DeptCondition record);

    DeptCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeptCondition record);

    int updateByPrimaryKey(DeptCondition record);

    List<DeptCondition> getDeptAll(DeptCondition condition);
}