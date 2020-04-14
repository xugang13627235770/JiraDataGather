/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.user.controller;

import com.citms.common.utils.Query;
import com.citms.common.utils.R;
import com.citms.modules.user.entity.UserEntity;
import com.citms.modules.jira.service.DictionaryService;
import com.citms.modules.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//http://localhost:2233/swagger-ui.html#/
@RestController
@RequestMapping("/user")
@Api(description ="人员分类接口")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private DictionaryService dictionaryService;

	@ApiOperation("用户列表")
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		String key = null;
		String userType = null;
        if(StringUtils.isNotEmpty((String) params.get("key"))){
			key = (String) params.get("key");
        }
		if(StringUtils.isNotEmpty((String) params.get("userType"))){
			userType = (String) params.get("userType");
		}
		return R.ok().put("page", userService.getUserList(new Query<UserEntity>().getPage(params), key, userType));
	}

	@ApiOperation("获取人员类型")
	@GetMapping("/getUserTypeList")
	public R getUserTypeList(){
		return R.ok().put("userTypeList", dictionaryService.getUserTypeList());
	}

	@ApiOperation("设置人员类型")
	@PostMapping("/updateUserTypeByIds")
	public R updateUserTypeByIds(String idStr, String userType){
		if(StringUtils.isEmpty(idStr) || StringUtils.isEmpty(userType)){
			return R.error();
		}

		List<String> ids = Arrays.asList(idStr.split(","));
		userService.updateUserTypeByIds(ids, userType);
		return R.ok();
	}


}
