package com.citms.modules.jira.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 */

@ApiModel("问题表")
@Data
@TableName("tbl_issue")
public class IssueEntity implements Serializable {
	@TableId
	private Long pkid;//主键ID

	private Long id;//问题ID
	
	private String keyValue;//问题KEY
	
	private Integer issuetype;//问题类型
	
	private String components;//问题概述

	private String timespent;//
	
	private String timeoriginalestimate;//
	
	private String description;//问题描述

	private String sprintId;//迭代ID
	
	private Long projectId;//所在项目ID
	
	private String aggregatetimespent;//
	
	private Integer resolution;//解决状态
	
	private String timetracking;//
	
	private String customfield10104;//

	private Integer customfield10203;//缺陷来源
	
	private String customfield10105;//
	
	private Date customfield10204;//预计提测日期
	
	private String customfield10106;// 故事点数

	private String customfield10205;// 阻塞标记
	
	private String attachment;//
	
	private String aggregatetimeestimate;//
	
	private java.util.Date resolutiondate;//
	
	private Integer workratio;//
	
	private String summary;//
	
	private java.util.Date lastViewed;//
	
	private String watches;//
	
	private Long creatorId;//创建者ID
	
	private java.util.Date created;//
	
	private Long reporterId;//
	
	private String customfield10000;//
	
	private Integer aggregateprogress;//
	
	private Integer priority;//优先级
	
	private String customfield10100;//

	private Integer customfield10201;//缺陷类型
	
	private String labels;//
	
	private String timeestimate;//
	
	private String aggregatetimeoriginalestimate;//
	
	private java.util.Date duedate;//到期日
	
	private String progress;//
	
	private String comment;//
	
	private String issuelinks;//
	
	private Integer votes;//
	
	private String worklog;//
	
	private Long assigneeId;//经办人
	
	private java.util.Date updated;//最后更新时间
	
	private Integer status;//问题状态

	private Date testedDate;//实际提测日期

	private Long closeUserId;//关闭人

	private Date closeDate;//关闭时间

	private Date flagDate;//标记时间

	private Integer isAdd;//是否迭代新增  0：是   1：否

}