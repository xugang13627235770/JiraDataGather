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
 * @category 测试统计记录
 */

@ApiModel("测试统计记录")
@Data
@TableName("tbl_tester_history")
public class TesterHistoryEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long projectId;//项目ID
	
	private Long sprintId;//
	
	private Long userId;//用户ID
	
	private String userName;//姓名
	
	private BigDecimal issueValidNum;//缺陷有效数量
	
	private BigDecimal issueValidRate;//缺陷有效率
	
	private BigDecimal violateOperateTimes;//违规操作次数
	
	private BigDecimal issueAddNum;//有效缺陷新增数量
	
	private BigDecimal taskTimes;//缺陷平均耗时
	
	private BigDecimal historyCloseNum;//历史缺陷关闭数量
	
	private BigDecimal onlineIssueNum;//线上反馈缺陷数量
	
	private BigDecimal totalScore;//总积分
	
	private Long nextSprintId;//下个迭代ID
	
}