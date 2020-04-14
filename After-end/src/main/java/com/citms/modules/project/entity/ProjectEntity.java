package com.citms.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 */

@ApiModel("项目表")
@Data
@TableName("tbl_project")
public class ProjectEntity implements Serializable {

	@TableId
	private Long pkid;

	private Long id;//
	
	private String keyValue;//
	
	private String name;//项目名称

	private Long projectPersonId;//项目负责人ID
	
	private String projectPerson;//项目负责人
	
	private String projectTypeKey;//

}