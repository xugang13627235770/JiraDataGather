package com.citms.modules.tester.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.tester.entity.ConfigSideEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 缺陷区间
 */
@Mapper
public interface ConfigSideMapper extends BaseMapper<ConfigSideEntity> {

    /**
     * 根据code获取区间配置信息
     * @param code
     * @return
     */
    List<ConfigSideEntity> getConfigSideByCode(String code);

    /**
     * 根据code列表查询区间配置
     * @param list
     * @return
     */
    List<ConfigSideEntity> selectSideConfigByCodes(List<String> list);

    /**
     * 根据code列表删除区间配置
     * @param list
     */
    void deleteByCodes(List<String> list);
}