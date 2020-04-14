package com.citms.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 */

@ApiModel("用户表")
@Data
@TableName("tbl_user")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;//
	
	private String name;//英文名
	
	private String displayName;//中文名
	
	private String keyValue;//
	
	private String emailAddress;//email
	
	private Integer active;//是否激活 0：未激活  1：已激活

	//用户类型
	private Integer userType;

	//最后操作时间
	private Date updateTime;

}