package com.citms.modules.jira.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 */

@ApiModel("变更日志表")
@Data
@TableName("tbl_changelog")
public class ChangeLogEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long id;//
	
	private String changeType;//变更类型
	
	private String originalValue;//原始值
	
	private String changeValue;//变更值
	
	private Long operatorId;//操作人ID
	
	private java.util.Date operatorDate;//操作时间

	private Long issueId;//问题ID

	private Integer changeStatus;//变更值状态

}