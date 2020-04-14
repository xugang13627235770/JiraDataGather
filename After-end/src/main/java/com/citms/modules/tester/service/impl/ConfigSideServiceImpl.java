package com.citms.modules.tester.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.modules.tester.dao.ConfigSideMapper;
import com.citms.modules.tester.entity.ConfigSideEntity;
import com.citms.modules.tester.service.ConfigSideService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 区间配置接口实现类
 */
@Service
public class ConfigSideServiceImpl extends ServiceImpl<ConfigSideMapper, ConfigSideEntity> implements ConfigSideService {

    @Override
    public List<ConfigSideEntity> selectSideConfigByCodes(List<String> codeList) {

        if (!CollectionUtils.isEmpty(codeList)) {

            return this.baseMapper.selectSideConfigByCodes(codeList);
        } else {

            return Lists.newArrayList();
        }
    }

    @Override
    public void deleteByCodes(List<String> codeList) {

        if (!CollectionUtils.isEmpty(codeList)) {

            this.baseMapper.deleteByCodes(codeList);
        }
    }

    @Override
    public void save(List<ConfigSideEntity> sideEntityList) {

        if (!CollectionUtils.isEmpty(sideEntityList)) {

            this.saveBatch(sideEntityList, 20);
        }
    }
}
