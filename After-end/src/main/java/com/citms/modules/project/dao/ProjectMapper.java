package com.citms.modules.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.project.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;

/**

 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    ProjectEntity queryProjectEntityById(Long id);

}