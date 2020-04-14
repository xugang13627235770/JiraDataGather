package com.citms.modules.jira.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.jira.entity.ChangeLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
@Mapper
public interface ChangeLogMapper extends BaseMapper<ChangeLogEntity> {

    List<ChangeLogEntity> queryChangeLogEntityById(Long id);

}