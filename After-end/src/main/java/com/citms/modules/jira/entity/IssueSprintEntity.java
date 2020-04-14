package com.citms.modules.jira.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 */

@ApiModel("tbl_issue_sprint表")
@Data
@TableName("tbl_issue_sprint")
public class IssueSprintEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long issueId;//问题ID

	private Long sprintId;//迭代ID

	private Integer isSprintAdd;//是否迭代新增  0：是   1：否
	
}