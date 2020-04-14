package com.citms.modules.sprintproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;

import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 */
public interface SprintProjectService extends IService<SprintProjectEntity> {

    /**
     * 获取所有迭代项目关联信息
     * @return
     */
    List<SprintProjectEntity> getAllSprintProjectList();

    /**
     * 根据迭代ID获取迭代项目关联信息
     * @param sprintId
     * @return
     */
    SprintProjectEntity getSprintProjectEntityBySprintId(Long sprintId);

    /**
     * 根据迭代ID获取下个迭代信息
     * @param sprintId
     * @return
     */
    SprintProjectEntity getNextSprintProjectBySprintId(Long sprintId);

    /**
     * 根据迭代ID获取上个迭代信息
     * @param sprintId
     * @return
     */
    SprintProjectEntity getLastSprintProjectBySprintId(Long sprintId);

}