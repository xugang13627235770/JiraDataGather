package com.citms.modules.jira.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 */
@ApiModel("字典表")
@Data
@TableName("tbl_dictionary")
public class DictionaryEntity implements Serializable {

	@TableId
	private Long id;//标识
	
	private Long parentId;//上级字典
	
	private String code;//字典编码
	
	private String dictKey;//字典值
	
	private String dictValue;//字典中文名称
	
	private String remark;//说明
	
	private Integer isDeleted;//是否已删除
	
}