package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spec.kit.exam.system.entity.OperationLogEntity;
import com.spec.kit.exam.system.mapper.OperationLogMapper;
import com.spec.kit.exam.system.service.OperationLogService;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogEntity> 
    implements OperationLogService {
    
    @Override
    public void save(OperationLogEntity operationLog) {
        this.save(operationLog);
    }
}