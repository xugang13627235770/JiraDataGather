package com.citms.modules.developer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @since 2018/03
 * @version R1.0
 * @category 开发统计配置表
 */

@ApiModel("开发统计配置表")
@Data
@TableName("tbl_config_developer")
public class ConfigDeveloperEntity implements Serializable {

	@TableId
	private Long pkid;

	private BigDecimal totalBaseScore;//总基础分
	
	private BigDecimal storyBaseScore;//故事基础分
	
	private BigDecimal closeStory;//基线关闭故事点数
	
	private BigDecimal testDelayRatio;//提测延期系数
	
	private BigDecimal testBackRatio;//提测打回系数
	
	private BigDecimal issueReturnRatio;//缺陷回归系数
	
	private BigDecimal noredIssueRatio;//非红色缺陷系数
	
	private BigDecimal redIssueRatio;//红色缺陷系数
	
	private BigDecimal validIssueRatio;//有效缺陷系数
	
	private BigDecimal issueCloseValue;//缺陷关闭率阈值
	
	private BigDecimal nocloseIssueRatio;//未关闭缺陷系数
	
	private BigDecimal onlineIssueRatio;//线上缺陷系数

	private BigDecimal maxScore;// 关闭故事最大加分值

	private BigDecimal baseTargetRate;//baseline基线迭代目标完成率

	private BigDecimal baseDefectCloseRate;//基线缺陷关闭率

	private BigDecimal densityJava;//工种缺陷密度-Java/C#工程师
	
	private BigDecimal densityFront;//工种缺陷密度-前端工程师
	
	private BigDecimal densityBigdata;//工种缺陷密度-大数据工程师
	
	private BigDecimal densityDefault;//工种缺陷密度-默认

	

}