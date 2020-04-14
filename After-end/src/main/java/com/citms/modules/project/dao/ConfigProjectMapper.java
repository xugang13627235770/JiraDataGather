package com.citms.modules.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.project.entity.ConfigProjectEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目统计动态规则配置Dao层
 */
@Mapper
public interface ConfigProjectMapper extends BaseMapper<ConfigProjectEntity> {

}
