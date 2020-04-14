package com.citms.modules.developer.entity;

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
 * @category 开发统计记录
 */

@ApiModel("开发统计记录")
@Data
@TableName("tbl_developer_history")
public class DeveloperHistoryEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long projectId;//项目ID
	
	private Long sprintId;//
	
	private Long userId;//用户ID
	
	private String developerName;//人员名称
	
	private BigDecimal closeStoryNum;//关闭故事点数
	
	private Long testDelayDays;//提测延期天数
	
	private Long testBackTimes;//提测打回次数
	
	private Long issueReturnTimes;//缺陷回归次数
	
	private Long redIssue;//红色缺陷遗留
	
	private Long noredIssue;//非红色缺陷遗留
	
	private Long validIssueNum;//有效缺陷数量
	
	private BigDecimal issueCloseRate;//缺陷关闭率
	
	private Long onlineIssue;//线上反馈缺陷
	
	private BigDecimal totalScore;//总积分
	
	private Long nextSprintId;//下个迭代ID

}