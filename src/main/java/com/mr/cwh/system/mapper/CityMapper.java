package com.mr.cwh.system.mapper;

import com.mr.cwh.system.condition.CityCondition;

import java.util.List;

public interface CityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CityCondition record);

    int insertSelective(CityCondition record);

    CityCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CityCondition record);

    int updateByPrimaryKey(CityCondition record);

    List<CityCondition> getAll();

    List<CityCondition> selectByPid(Integer parentId);

    List<CityCondition> selectAll();
}