package com.citms.modules.sprintproject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 */

@ApiModel("sprint表")
@Data
@TableName("tbl_sprint")
public class SprintEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long id;//

	private Integer state;//状态：1: active  2: INACTIVE
	
	private String name;//迭代名称
	
	private java.util.Date startDate;//开始日期
	
	private java.util.Date endDate;//结束日期
	
	private Long originBoardId;//
	
	private String goal;//

}