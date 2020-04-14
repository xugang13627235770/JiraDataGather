package com.citms.modules.developer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.developer.entity.DeveloperHistoryEntity;

/**
 * 开发人员统计
 */
public interface DeveloperHistoryService extends IService<DeveloperHistoryEntity> {

    /**
     * 保存开发统计历史记录信息
     * @param sprintId
     */
    void saveDeveloperHistory(Long sprintId);

    /**
     * 更新下个迭代信息
     * @param sprintId
     */
    void updateNextSprintId(Long sprintId);

}