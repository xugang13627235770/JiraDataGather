package com.citms.modules.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.project.entity.SprintStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 指定项目迭代内的迭代统计表
 */
@Mapper
public interface SprintStatisticsMapper extends BaseMapper<SprintStatisticsEntity> {

    /**
     * 根据迭代ID查询迭代统计表
     * @param sprintId
     * @return
     */
    SprintStatisticsEntity querySprintStatisticsById(Long sprintId);

}