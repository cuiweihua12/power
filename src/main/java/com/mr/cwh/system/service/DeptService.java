package com.mr.cwh.system.service;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.DeptCondition;

public interface DeptService {
    Response getDeptAll(DeptCondition condition);

    DeptCondition getDeptById(DeptCondition condition);
}
