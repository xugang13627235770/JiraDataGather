/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.jira.controller;

import com.citms.common.utils.R;
import com.citms.modules.jira.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary")
@Api(description ="数据字典接口")
public class DictionaryController {

	@Autowired
	private DictionaryService dictionaryService;

	@ApiOperation("根据code查找字典")
	@GetMapping("/findDictionaryByCode")
	public R findDictionaryByCode(String code){
		return R.ok().put("dictionaryList", dictionaryService.getDictionaryByCode(code));
	}


}
