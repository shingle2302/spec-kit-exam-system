package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.OperationLogEntity;

public interface OperationLogService {
    boolean save(OperationLogEntity operationLog);
}