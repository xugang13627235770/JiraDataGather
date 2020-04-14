/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.tester.contoller;

import com.alibaba.fastjson.JSON;
import com.citms.common.utils.Query;
import com.citms.common.utils.R;
import com.citms.common.utils.EasyExcelUtils;
import com.citms.modules.tester.service.TesterService;
import com.citms.modules.tester.vo.ConfigTesterVO;
import com.citms.modules.tester.vo.TesterRecordVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(description ="测试者统计接口")
@RestController
@RequestMapping("/tester")
public class TesterController {

	@Autowired
	private TesterService testerService;

	@ApiOperation("统计测试迭代内的数据")
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		List<String> sprintIdList = Lists.newArrayList();
		if(StringUtils.isNotEmpty((String) params.get("sprints"))){
			sprintIdList = Arrays.asList(((String) params.get("sprints")).split(","));
		}
		return R.ok().put("page", testerService.getTesterRecordPage(new Query<TesterRecordVO>().getPage(params), sprintIdList));
	}

	@ApiOperation("获取测试者配置信息")
	@GetMapping("/getConfigTester")
	public R getConfigTester(){
		return R.ok().put("configTesterVO", testerService.getConfigTesterVO());
	}

	@ApiOperation("保存测试者配置信息")
	@PostMapping("/saveConfigTesterVO")
	public R saveConfigTesterVO(String configTesterVOStr){
		try {
			ConfigTesterVO configTesterVO =  JSON.parseObject(configTesterVOStr, ConfigTesterVO.class);
			testerService.saveConfigTester(configTesterVO);
			return R.ok();
		}catch (Exception e){
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}

	@ApiOperation("导出测试统计的数据")
	@PostMapping("/exportExcel")
	public R exportExcel(String sprints) {
		if(StringUtils.isEmpty(sprints)){
			return R.error("迭代信息不能为空");
		}

		List<String> sprintIdList = Arrays.asList(sprints.split(","));
		List<TesterRecordVO> testerRecordList = testerService.getTesterRecordList(sprintIdList);
		if(CollectionUtils.isEmpty(testerRecordList)){
			return R.error();
		}

		FileOutputStream fos = null;
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String date = dfs.format(new Date());
		String name = "测试统计记录_" + date + ".xlsx";
		String path = EasyExcelUtils.userAttachPath() + "\\" + name;
		try {
			fos = new FileOutputStream(path);
			EasyExcelUtils.writeEasyExcel(fos, TesterRecordVO.class, testerRecordList, "测试统计记录");
			return R.ok().put("data", name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return R.error();
	}

}
