package com.citms.modules.tester.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.tester.entity.TesterHistoryEntity;

/**
 *测试统计历史记录信息
 */
public interface TesterHistoryService extends IService<TesterHistoryEntity> {

    /**
     * 保存测试统计历史记录信息
     * @param sprintId
     */
    void saveTesterHistory(Long sprintId);

    /**
     * 更新下个迭代信息
     * @param sprintId
     */
    void updateNextSprintId(Long sprintId);
}