package com.citms.modules.jira.entity;

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
 * @category 故事点数记录表
 */

@ApiModel("故事点数记录表")
@Data
@TableName("tbl_story_point_record")
public class StoryPointRecord implements Serializable {

	@TableId
	private Long pkid;

	private Long sprintId;//迭代id
	
	private String sprintDate;//迭代日期
	
	private BigDecimal storyPoint;//故事点数
	
	

}