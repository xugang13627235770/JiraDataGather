package com.citms.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 指定项目迭代内的迭代统计表
 */

@ApiModel("指定项目迭代内的迭代统计表")
@Data
@TableName("tbl_sprint_statistics")
public class SprintStatisticsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Long pkid;

	private Long sprintId;//迭代ID

	private String sprintTargetRate;//迭代目标完成率

	private String sprintTargetRateScore;//迭代目标完成率得分

	private String avgStoryPoint;//人均关闭故事点数

	private String avgStoryPointScore;//人均关闭故事点数得分

	private String avgStoryTime;//平均故事关闭时长

	private String avgStoryTimeScore;//平均故事关闭时长得分

	private String avgStoryDelay;//平均故事延期时长

	private String avgStoryDelayScore;//平均故事延期时长得分

	private String reopenStory;//重新打开故事次数

	private String reopenStoryScore;//重新打开故事次数得分

	private String remainStory;//迭代遗留故事占比

	private String remainStoryScore;//迭代遗留故事占比得分

	private String overdueStoryPoint;//逾期完成故事点数

	private String overdueStoryPointScore;//逾期完成故事点数得分

	private String defectCloseRate;//缺陷关闭率

	private String defectCloseRateScore;//缺陷关闭率得分

	private String defectDensity;//缺陷密度

	private String defectDensityScore;//缺陷密度得分

	private String remainDefectRate;//遗留缺陷比例

	private String remainDefectRateScore;//遗留缺陷比例得分

	private String onlineDefect;//本迭代内线上反馈的缺陷

	private String onlineDefectScore;//线上缺陷得分

	private String onlineRepairTime;//平均线上缺陷修复时长

	private String onlineRepairTimeScore;//线上缺陷修复时长得分

	private String addStory;//迭代期间新增故事数

	private String addStoryScore;//迭代期间新增故事数扣分

	private Date createdTime;//创建时间

	private String sprintNewStory;//迭代新任务(便与调试)

	private String sprintNewStoryFinish;//迭代已关闭的新任务(便与调试)

	private String sprintOldStory;//迭代遗留任务(便与调试)

	private String sprintOldStoryFinish;//迭代已关闭的遗留任务(便与调试)
}