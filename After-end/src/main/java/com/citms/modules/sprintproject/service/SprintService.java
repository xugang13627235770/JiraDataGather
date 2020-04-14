package com.citms.modules.sprintproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.sprintproject.entity.SprintEntity;
import com.citms.modules.sprintproject.vo.SprintInfoVO;
import com.citms.modules.sprintproject.vo.SprintProjectVO;

import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 */
public interface SprintService extends IService<SprintEntity> {

    /**
     * 根据项目ID获取迭代信息
     * @return
     */
    List<SprintEntity> getSprintListByProjectId(Long projectId);

    /**
     * 查询未关联迭代信息
     * @return
     */
    List<SprintEntity> getUnrelatedSprintList();

    /**
     * 获取所有的项目和关联的迭代信息
     * @return
     */
    List<SprintProjectVO> getRelatedSprintList(List<Long> projectIdList);

    /**
     * 保存迭代关联信息
     */
    void saveSprintRelatedInfo(List<SprintInfoVO> sprintProjectVOList);
}