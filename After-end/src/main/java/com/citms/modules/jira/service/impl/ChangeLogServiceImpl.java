package com.citms.modules.jira.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.modules.jira.dao.ChangeLogMapper;
import com.citms.modules.jira.entity.ChangeLogEntity;
import com.citms.modules.jira.service.ChangeLogService;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ChangeLogServiceImpl extends ServiceImpl<ChangeLogMapper,ChangeLogEntity>
        implements ChangeLogService {
}