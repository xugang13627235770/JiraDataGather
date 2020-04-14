/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.sprintproject.controller;

import com.alibaba.fastjson.JSON;
import com.citms.common.utils.R;
import com.citms.modules.sprintproject.service.SprintService;
import com.citms.modules.sprintproject.vo.SprintInfoVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sprint")
@Api(description ="迭代信息接口")
public class SprintController {

	@Autowired
	private SprintService sprintService;

	@ApiOperation("根据项目ID查询迭代信息")
	@GetMapping("/getSprintListByProjectId")
	public R getSprintListByProjectId(Long projectId){
		return R.ok().put("sprintList", sprintService.getSprintListByProjectId(projectId));
	}

	@ApiOperation("查询未关联迭代")
	@GetMapping("/getUnrelatedSprintList")
	public R getUnrelatedSprintList(){
		return R.ok().put("unrelatedSprintList", sprintService.getUnrelatedSprintList());
	}

	@ApiOperation("查询迭代关联关系-根据项目id")
	@GetMapping("/getRelatedSprintList")
	public R getRelatedSprintList(String projects){//项目信息以逗号隔开
		List<Long> projectIdList = Lists.newArrayList();
		if(StringUtils.isNotEmpty(projects)){
			projectIdList = Arrays.asList(projects.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
		}
		return R.ok().put("relatedSprintList", sprintService.getRelatedSprintList(projectIdList));
	}

	@ApiOperation("保存迭代关联关系")
	@PostMapping("/saveSprintRelatedInfo")
	public R saveSprintRelatedInfo(String sprintProjectStr){
		try {
			List<SprintInfoVO> sprintProjectVOList =  JSON.parseArray(sprintProjectStr, SprintInfoVO.class);
			sprintService.saveSprintRelatedInfo(sprintProjectVOList);
			return R.ok();
		}catch (Exception e){
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}
}
