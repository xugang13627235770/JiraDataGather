package com.citms.modules.sprintproject.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.sprintproject.entity.SprintEntity;
import com.citms.modules.sprintproject.vo.SprintInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
@Mapper
public interface SprintMapper extends BaseMapper<SprintEntity> {

    /**
     * 根据ID获取迭代信息
     * @param id
     * @return
     */
    SprintEntity querySprintById(Long id);

    /**
     * 根据项目ID获取迭代信息
     * @param projectId
     * @return
     */
    List<SprintEntity> getSprintListByProjectId(Long projectId);

    /**
     * 查询未关联迭代信息
     * @return
     */
    List<SprintEntity> getUnrelatedSprintList();

    /**
     *查询已关联迭代信息
     * @return
     */
    List<SprintInfoVO> getSprintListByProjectIds(@Param("projectIdList")List<Long> projectIdList);
}