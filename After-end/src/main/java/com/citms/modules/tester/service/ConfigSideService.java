package com.citms.modules.tester.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.tester.entity.ConfigSideEntity;

import java.util.List;

/**
 * 区间配置接口
 */
public interface ConfigSideService extends IService<ConfigSideEntity> {

    /**
     * 根据code列表查询区间配置
     * @param codeList
     * @return
     */
    List<ConfigSideEntity> selectSideConfigByCodes(List<String> codeList);

    /**
     * 根据code列表删除区间配置
     * @param codeList
     */
    void deleteByCodes(List<String> codeList);

    /**
     * 新增区间配置
     * @param sideEntityList
     */
    void save(List<ConfigSideEntity> sideEntityList);
}
