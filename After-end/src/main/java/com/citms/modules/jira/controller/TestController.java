/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.jira.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.citms.common.utils.PageUtils;
import com.citms.common.utils.Query;
import com.citms.common.utils.R;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class TestController {

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		IPage<SysUserEntity> page = new Query<SysUserEntity>().getPage(params);
		page.setSize(1);
		page.setTotal(1);
		page.setCurrent(1);
		List<SysUserEntity> list = new ArrayList<>();
		SysUserEntity e = new SysUserEntity();
		e.setUsername("xu");
		e.setCreateTime(new Date());
		e.setMobile("1365874586");
		e.setEmail("1524667887@qq.com");
		e.setStatus(1);
		list.add(e);
		page.setRecords(list);

		return R.ok().put("page", new PageUtils(page));
	}

	@Data
	@TableName("sys_user")
	class SysUserEntity implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * 用户ID
		 */
		@TableId
		private Long userId;

		/**
		 * 用户名
		 */
		private String username;

		/**
		 * 密码
		 */
		private String password;

		/**
		 * 盐
		 */
		private String salt;

		/**
		 * 邮箱
		 */
		private String email;

		/**
		 * 手机号
		 */
		private String mobile;

		/**
		 * 状态  0：禁用   1：正常
		 */
		private Integer status;

		/**
		 * 角色ID列表
		 */
		@TableField(exist=false)
		private List<Long> roleIdList;

		/**
		 * 创建者ID
		 */
		private Long createUserId;

		/**
		 * 创建时间
		 */
		private Date createTime;

	}

}
