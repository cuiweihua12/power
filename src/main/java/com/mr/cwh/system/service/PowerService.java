package com.mr.cwh.system.service;

import com.mr.cwh.system.base.Response;
import com.mr.cwh.system.condition.PowerCondition;
import com.mr.cwh.system.condition.RolePowerCondition;

public interface PowerService {
    Response getPowerAll(PowerCondition condition);

    Response savePower(PowerCondition condition);

    Response del(PowerCondition condition);

    Response saveRolePower(RolePowerCondition condition);

    Response getPowerByRole(RolePowerCondition condition);

    PowerCondition getPowerByPidAndName(PowerCondition condition);

    Response batchDel(String ids);

    PowerCondition getPowerByIdName(PowerCondition condition);
}
