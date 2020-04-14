package com.citms.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.project.entity.ConfigProjectEntity;
import com.citms.modules.project.vo.ConfigProjectVO;

/**
 * 项目统计动态规则配置接口
 */
public interface ConfigProjectService extends IService<ConfigProjectEntity> {

    /**
     * 获取项目统计动态规则配置
     * @return
     */
    ConfigProjectVO getConfigProjectRuleInfo();

    /**
     * 保存项目统计动态规则配置
     * @param vo
     */
    void saveConfigProjectRule(ConfigProjectVO vo);
}
