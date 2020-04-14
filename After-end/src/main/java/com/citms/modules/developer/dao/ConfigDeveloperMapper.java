package com.citms.modules.developer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.developer.entity.ConfigDeveloperEntity;
import com.citms.modules.developer.vo.DeveloperRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @category 开发统计配置表
 */
@Mapper
public interface ConfigDeveloperMapper extends BaseMapper<ConfigDeveloperEntity> {

    /**
     * 统计开发迭代内的数据,分页
     * @param ids
     * @return
     */
    List<DeveloperRecordVO> getDeveloperRecordPage(@Param("ids") List<String> ids);

    /**
     *统计开发迭代内的数据,不分页
     * @param ids
     * @return
     */
    List<DeveloperRecordVO> getDeveloperRecordList(@Param("ids") List<String> ids);

    /**
     *统计开发迭代内的数据
     * @return
     */
    Integer getDeveloperRecordCount(@Param("ids") List<String> ids);

}