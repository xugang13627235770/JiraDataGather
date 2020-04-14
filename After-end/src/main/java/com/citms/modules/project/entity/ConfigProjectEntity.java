package com.citms.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目统计规则配置实体类
 */
@Data
@TableName("tbl_config_project")
@ApiModel("项目统计规则配置实体类")
public class ConfigProjectEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId
    @ApiModelProperty("主键ID")
    private Long pkid;

    /**
     * 新增故事关闭权重
     */
    @ApiModelProperty("新增故事关闭权重")
    private BigDecimal newStoryCloseWeight;

    /**
     * 历史遗留故事关闭权重
     */
    @ApiModelProperty("历史遗留故事关闭权重")
    private BigDecimal historyStoryCloseWeight;

    /**
     * 迭代目标完成率基础分
     */
    @ApiModelProperty("迭代目标完成率基础分")
    private BigDecimal sprintCompletionRateBaseScore;

    /**
     * 迭代目标完成率基线值
     */
    @ApiModelProperty("迭代目标完成率基线值")
    private BigDecimal sprintCompletionRateBaseLine;

    /**
     * 人均关闭故事点数基础分
     */
    @ApiModelProperty("人均关闭故事点数基础分")
    private BigDecimal personClosePointBaseScore;

    /**
     * 人均关闭故事点数基线值
     */
    @ApiModelProperty("人均关闭故事点数基线值")
    private BigDecimal personClosePointBaseLine;

    /**
     * 缺陷关闭率基础分
     */
    @ApiModelProperty("缺陷关闭率基础分")
    private BigDecimal defectCloseRateBaseScore;

    /**
     * 迭代缺陷关闭率基线值
     */
    @ApiModelProperty("迭代缺陷关闭率基线值")
    private BigDecimal defectCloseRateBaseLine;

    /**
     * 缺陷密度基础分
     */
    @ApiModelProperty("缺陷密度基础分")
    private BigDecimal defectDensityBaseScore;

    /**
     * 缺陷密度基线值
     */
    @ApiModelProperty("缺陷密度基线值")
    private BigDecimal defectDensityBaseLine;

    /**
     * 迭代新增故事扣分基数
     */
    @ApiModelProperty("迭代新增故事扣分基数")
    private BigDecimal sprintAddStoryDeductionBase;
}
