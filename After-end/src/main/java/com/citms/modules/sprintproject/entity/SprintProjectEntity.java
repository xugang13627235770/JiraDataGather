package com.citms.modules.sprintproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 迭代项目关联表
 */

@ApiModel("迭代项目关联表")
@Data
@TableName("tbl_sprint_project")
public class SprintProjectEntity implements Serializable {

	private Long sprintId;//迭代ID
	
	private Long projectId;//项目ID

	private Integer isDeleted;//是否删除 0：未删除   1：已删除
	
}