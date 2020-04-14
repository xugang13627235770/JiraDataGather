/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.home.contoller;

import com.citms.common.utils.R;
import com.citms.modules.home.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@Api(description ="首页问题统计接口")
public class HomeController {

	@Autowired
	private HomeService issueService;

	@ApiOperation("根据项目ID统计出问题")
	@GetMapping("/getIssueDataByProjectId")
	public R getIssueDataByProjectId(Long projectId){
		return R.ok().put("list", issueService.getHomeIssue(projectId));
	}

}
