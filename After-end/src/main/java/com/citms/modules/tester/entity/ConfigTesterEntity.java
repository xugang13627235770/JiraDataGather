package com.citms.modules.tester.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 测试参数配置表
 */

@ApiModel("测试参数配置表")
@Data
@TableName("tbl_config_tester")
public class ConfigTesterEntity implements Serializable {

	@TableId
	private Long pkid;

	private BigDecimal totalBaseScore;//总基础分
	
	private BigDecimal issueValidBase;//缺陷有效率统计基数
	
	private BigDecimal violateMaxScore;//违规操作最大扣分值
	
	private BigDecimal onlineIssueRatio;//线上缺陷系数

}