package com.citms.modules.project.vo;

import com.citms.modules.project.entity.ConfigProjectEntity;
import com.citms.modules.tester.entity.ConfigSideEntity;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 项目统计动态规则配置扩展类
 */
@Data
@ApiModel("项目统计动态规则配置扩展类")
public class ConfigProjectVO extends ConfigProjectEntity {

    /**
     * 平均故事关闭时长得分区间
     */
    @ApiModelProperty("平均故事关闭时长得分区间")
    private List<ConfigSideEntity> avgStoryCloseTimeRange = Lists.newArrayList();

    /**
     * 平均故事延期时长得分区间
     */
    @ApiModelProperty("平均故事延期时长得分区间")
    private List<ConfigSideEntity> avgStoryDelayTimeRange = Lists.newArrayList();

    /**
     * 故事重新打开次数得分区间
     */
    @ApiModelProperty("故事重新打开次数得分区间")
    private List<ConfigSideEntity> storyReopenNumRange = Lists.newArrayList();

    /**
     * 迭代遗留故事占比得分区间
     */
    @ApiModelProperty("迭代遗留故事占比得分区间")
    private List<ConfigSideEntity> leftOverStoryRateRange = Lists.newArrayList();

    /**
     * 逾期完成故事点数得分区间
     */
    @ApiModelProperty("逾期完成故事点数得分区间")
    private List<ConfigSideEntity> overdueCompletePointRange = Lists.newArrayList();

    /**
     * 遗留缺陷比例得分区间
     */
    @ApiModelProperty("遗留缺陷比例得分区间")
    private List<ConfigSideEntity> leftOverDefectRateRange = Lists.newArrayList();

    /**
     * 线上缺陷数量得分区间
     */
    @ApiModelProperty("线上缺陷数量得分区间")
    private List<ConfigSideEntity> onlineDefectNumRange = Lists.newArrayList();

    /**
     * 线上缺陷平均修复时长得分区间
     */
    @ApiModelProperty("线上缺陷平均修复时长得分区间")
    private List<ConfigSideEntity> onlineDefectAvgRepairTimeRange = Lists.newArrayList();
}
