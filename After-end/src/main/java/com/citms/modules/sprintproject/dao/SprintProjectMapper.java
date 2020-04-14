package com.citms.modules.sprintproject.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 迭代项目关联
 */
@Mapper
public interface SprintProjectMapper extends BaseMapper<SprintProjectEntity> {

    SprintProjectEntity querySprintProjectById(@Param("projectId") Long projectId, @Param("sprintId")Long sprintId);

    /**
     * 批量保存迭代项目关联关系
     * @param sprintProjectList
     * @return
     */
    boolean batchSaveSprintProject(@Param("sprintProjectList") List<SprintProjectEntity> sprintProjectList);

    /**
     * 根据项目ID批量删除
     * @param projectIdList
     * @return
     */
    Integer batchDeleteByProjectIds(@Param("projectIdList") List<Long> projectIdList);

    /**
     * 获取所有迭代项目关联信息
     * @return
     */
    List<SprintProjectEntity> getAllSprintProjectList();

    /**
     * 根据迭代ID获取跟当前迭代一个项目的所有迭代信息
     * @param sprintId
     * @return
     */
    List<SprintProjectEntity> getSprintProjectBySprintId(Long sprintId);
}