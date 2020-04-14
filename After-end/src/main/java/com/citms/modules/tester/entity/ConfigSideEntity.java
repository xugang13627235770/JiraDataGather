package com.citms.modules.tester.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 缺陷区间表
 */

@ApiModel("缺陷区间表")
@Data
@TableName("tbl_config_side")
public class ConfigSideEntity implements Serializable {

	@TableId
	@ApiModelProperty("主键ID")
	private Long pkid;

	@ApiModelProperty("区间编码")
	private String code;//区间编码

	@ApiModelProperty("左区间")
	private BigDecimal leftSide;//左区间

	@ApiModelProperty("右区间")
	private BigDecimal rightSide;//右区间

	@ApiModelProperty("加分值")
	private BigDecimal score;//加分值

	@ApiModelProperty("是否删除 0：未删除   1：已删除")
	private Integer isDeleted;//是否删除 0：未删除   1：已删除
}