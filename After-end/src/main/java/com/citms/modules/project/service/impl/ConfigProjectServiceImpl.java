package com.citms.modules.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.project.entity.ConfigProjectEntity;
import com.citms.modules.project.vo.ConfigProjectVO;
import com.citms.modules.project.dao.ConfigProjectMapper;
import com.citms.modules.project.service.ConfigProjectService;
import com.citms.modules.tester.entity.ConfigSideEntity;
import com.citms.modules.tester.service.ConfigSideService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目统计动态规则配置接口实现类
 */
@Service
@Transactional
public class ConfigProjectServiceImpl extends ServiceImpl<ConfigProjectMapper, ConfigProjectEntity> implements ConfigProjectService {

    @Autowired
    private ConfigSideService configSideService;

    /**
     * 获取所有项目区间配置code
     * @return
     */
    private List<String> getProjectConfigCodes() {

        List<String> codeList = Lists.newArrayList(
                JiraConstants.ProjectConfigRangeCode.AVG_STORY_CLOSE_TIME,
                JiraConstants.ProjectConfigRangeCode.AVG_STORY_DELAY_TIME,
                JiraConstants.ProjectConfigRangeCode.LEFT_OVER_DEFECT_RATE,
                JiraConstants.ProjectConfigRangeCode.LEFT_OVER_STORY_RATE,
                JiraConstants.ProjectConfigRangeCode.ONLINE_DEFECT_AVG_REPAIR_TIME,
                JiraConstants.ProjectConfigRangeCode.ONLINE_DEFECT_NUM,
                JiraConstants.ProjectConfigRangeCode.OVERDUE_COMPLETE_POINT,
                JiraConstants.ProjectConfigRangeCode.STORY_REOPEN_NUM
        );

        return codeList;
    }

    @Override
    public ConfigProjectVO getConfigProjectRuleInfo() {

        ConfigProjectVO vo = new ConfigProjectVO();

        // 查询固定配置
        List<ConfigProjectEntity> list = this.baseMapper.selectList(new QueryWrapper<ConfigProjectEntity>());
        ConfigProjectEntity entity = CollectionUtils.isEmpty(list) ? new ConfigProjectEntity() : list.get(0);
        BeanUtils.copyProperties(entity,vo);

        // 查询区间配置
        List<ConfigSideEntity> sideEntityList = configSideService.selectSideConfigByCodes(getProjectConfigCodes());

        // 区间配置分组
        Map<String, List<ConfigSideEntity>> sideMap = sideEntityList.stream().collect(Collectors.groupingBy(ConfigSideEntity::getCode));
        sideMap.keySet().forEach(code -> {

            List<ConfigSideEntity> value = sideMap.get(code);
            Collections.sort(value, (v1, v2) -> v1.getLeftSide().compareTo(v2.getLeftSide()));


            switch (code) {
                case JiraConstants.ProjectConfigRangeCode.AVG_STORY_CLOSE_TIME:

                    vo.setAvgStoryCloseTimeRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.AVG_STORY_DELAY_TIME:

                    vo.setAvgStoryDelayTimeRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.LEFT_OVER_DEFECT_RATE:

                    vo.setLeftOverDefectRateRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.LEFT_OVER_STORY_RATE:

                    vo.setLeftOverStoryRateRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.ONLINE_DEFECT_AVG_REPAIR_TIME:

                    vo.setOnlineDefectAvgRepairTimeRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.ONLINE_DEFECT_NUM:

                    vo.setOnlineDefectNumRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.OVERDUE_COMPLETE_POINT:

                    vo.setOverdueCompletePointRange(value);
                    break;
                case JiraConstants.ProjectConfigRangeCode.STORY_REOPEN_NUM:

                    vo.setStoryReopenNumRange(value);
                    break;
                default:

                    break;
            }
        });

        return vo;
    }

    /**
     * 保存时处理区间配置
     * @return
     */
    private List<ConfigSideEntity> handleEmptySide(List<ConfigSideEntity> targetList) {

        if (CollectionUtils.isEmpty(targetList)) {

            return Lists.newArrayList();
        } else {

            List<ConfigSideEntity> filterList = targetList.stream().filter(entity -> {

                return entity.getLeftSide() != null && entity.getScore() != null && StringUtils.isNotBlank(entity.getCode());
            }).collect(Collectors.toList());

            Collections.sort(filterList, (v1, v2) -> v1.getLeftSide().compareTo(v2.getLeftSide()));
            return targetList;
        }
    }

    @Override
    public void saveConfigProjectRule(ConfigProjectVO vo) {

        // 保存固定配置
        ConfigProjectEntity entity = new ConfigProjectEntity();
        BeanUtils.copyProperties(vo,entity);

        if (entity.getPkid() != null) {

            this.baseMapper.updateById(entity);
        } else {

            this.baseMapper.insert(entity);
        }

        // 保存区间配置
        configSideService.deleteByCodes(getProjectConfigCodes());

        List<List<ConfigSideEntity>> sideListList = Lists.newArrayList(
                handleEmptySide(vo.getAvgStoryCloseTimeRange()),
                handleEmptySide(vo.getAvgStoryDelayTimeRange()),
                handleEmptySide(vo.getLeftOverDefectRateRange()),
                handleEmptySide(vo.getLeftOverStoryRateRange()),
                handleEmptySide(vo.getOnlineDefectAvgRepairTimeRange()),
                handleEmptySide(vo.getOnlineDefectNumRange()),
                handleEmptySide(vo.getOverdueCompletePointRange()),
                handleEmptySide(vo.getStoryReopenNumRange())
        );

        List<ConfigSideEntity> sideEntityList = sideListList.stream().flatMap(e -> e.stream()).collect(Collectors.toList());

        configSideService.save(sideEntityList);
    }
}
