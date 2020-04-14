/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.developer.controller;

import com.citms.common.constant.JiraConstants;
import com.citms.common.utils.EasyExcelUtils;
import com.citms.common.utils.Query;
import com.citms.common.utils.R;
import com.citms.modules.developer.entity.ConfigDeveloperEntity;
import com.citms.modules.developer.service.DeveloperService;
import com.citms.modules.developer.vo.DeveloperRecordVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//http://localhost:2233/swagger-ui.html#/
@RestController
@RequestMapping("/developer")
@Api(description ="开发者统计接口")
public class DeveloperController {

	@Autowired
	private DeveloperService developerService;

	@ApiOperation("统计开发迭代内的数据")
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
        List<String> sprintIdList = Lists.newArrayList();
        if(StringUtils.isNotEmpty((String) params.get("sprints"))){
            sprintIdList = Arrays.asList(((String) params.get("sprints")).split(","));
        }
		return R.ok().put("page", developerService.getDeveloperRecordPage(new Query<DeveloperRecordVO>().getPage(params), sprintIdList));
	}

	@ApiOperation("获取开发者配置信息")
	@GetMapping("/getConfigDeveloper")
	public R getConfigDeveloper(){
		return R.ok().put("configDeveloper", developerService.getById(JiraConstants.DEVELOPER_PKID));
	}

	@ApiOperation("保存开发者配置信息")
	@PostMapping("/saveConfigDeveloper")
	public R saveConfigDeveloper(ConfigDeveloperEntity configDeveloper){
		try {
			developerService.saveConfigDeveloper(configDeveloper);
			return R.ok();
		}catch (Exception e){
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}

	@ApiOperation("导出开发迭代内的数据")
	@PostMapping("/exportExcel")
	public R exportExcel(String sprints) {
		List<String> sprintIdList = Lists.newArrayList();
		if(StringUtils.isNotEmpty(sprints)){
			sprintIdList = Arrays.asList(sprints.split(","));
		}
		List<DeveloperRecordVO> developerRecordList = developerService.getDeveloperRecordList(sprintIdList);
		if(CollectionUtils.isEmpty(developerRecordList)){
            return R.error();
        }

        FileOutputStream fos = null;
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = dfs.format(new Date());
        String name = "开发统计记录_" + date + ".xlsx";
        String path = EasyExcelUtils.userAttachPath() + "\\" + name;
        try {
            fos = new FileOutputStream(path);
            EasyExcelUtils.writeEasyExcel(fos, DeveloperRecordVO.class, developerRecordList, "开发统计记录");
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

    @GetMapping("/downloadExcel/{name}")
    @ApiOperation(value = "导出", notes = "传入name", position = 5)
    public void downloadExcel(@ApiParam(value = "Excel文件名称name", required = true) @PathVariable String name, HttpServletResponse response) throws IOException {
        String path = EasyExcelUtils.userAttachPath() + "\\" + name;
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码
        String filename = URLEncoder.encode(name, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }
}
