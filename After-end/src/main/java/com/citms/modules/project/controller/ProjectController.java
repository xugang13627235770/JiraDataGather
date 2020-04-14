/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.project.controller;

import com.citms.common.utils.R;
import com.citms.common.utils.EasyExcelUtils;
import com.citms.modules.project.service.ProjectService;
import com.citms.modules.jira.service.StoryPointRecordService;
import com.citms.modules.project.vo.SprintTargetExportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/project")
@Api(description ="项目信息接口")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private StoryPointRecordService storyPointRecordService;

	@ApiOperation("根据项目名称模糊查询项目信息")
	@GetMapping("/getProjectListByName")
	public R getProjectListByName(String projectName){
		return R.ok().put("projectList", projectService.getProjectListByName(projectName));
	}

//	@ApiOperation("根据迭代ID统计项目信息")
//	@GetMapping("/getProjectInfoBySprintId")
//	public R getProjectInfoBySprintId(Long sprintId){
//		return R.ok().put("defectRateList", projectService.getDefectRate(sprintId))//迭代总缺陷数量人员占比
//				.put("storyPointRateList", projectService.getStoryPointRate(sprintId))//迭代故事点数人员占比
////				.put("sprintTargetList", projectService.getSprintTargetExportList(sprintId))//迭代内各项指标
//				.put("storyPointChart", storyPointRecordService.getStoryPointRecordBySprintId(sprintId))//迭代故事点数燃尽图
//				.put("addIssueTypeList", projectService.getAddIssueTypeList(sprintId));//迭代新增问题类型统计
//	}

	@ApiOperation("迭代总缺陷数量人员占比")
	@GetMapping("/getDefectRate")
	public R getDefectRate(Long sprintId){
		return R.ok().put("defectRateList", projectService.getDefectRate(sprintId));
	}

	@ApiOperation("迭代故事点数人员占比")
	@GetMapping("/getStoryPointRate")
	public R getStoryPointRate(Long sprintId){
		return R.ok().put("storyPointRateList", projectService.getStoryPointRate(sprintId));
	}

//	@ApiOperation("迭代内各项指标")
//	@GetMapping("/getSprintTargetList")
//	public R getSprintTargetList(Long sprintId){
//		return R.ok().put("sprintTargetList", projectService.getSprintTargetExportList(sprintId));
//	}

//	@ApiOperation("迭代故事点数燃尽图")
//	@GetMapping("/getStoryPointRecordBySprintId")
//	public R getStoryPointRecordBySprintId(Long sprintId){
//		return R.ok().put("storyPointChart", storyPointRecordService.getStoryPointRecordBySprintId(sprintId));
//	}

	@ApiOperation("迭代新增问题类型统计")
	@GetMapping("/getAddIssueTypeList")
	public R getAddIssueTypeList(Long sprintId){
		return R.ok().put("addIssueTypeList", projectService.getAddIssueTypeList(sprintId));
	}


    @ApiOperation("导出迭代内各项指标数据")
    @PostMapping("/exportExcel")
    public R exportExcel(Long sprintId) {
        List<SprintTargetExportVO> sprintTargetExportVOList = projectService.getSprintTargetExportList(sprintId);
        if(CollectionUtils.isEmpty(sprintTargetExportVOList)){
            return R.error();
        }

        FileOutputStream fos = null;
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = dfs.format(new Date());
        String name = "迭代内各项指标数据记录_" + date + ".xlsx";
        String path = EasyExcelUtils.userAttachPath() + "\\" + name;
        try {
            fos = new FileOutputStream(path);
            EasyExcelUtils.writeEasyExcel(fos, SprintTargetExportVO.class, sprintTargetExportVOList, "迭代内各项指标数据记录");
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

//	@ApiOperation("迭代目标完成率=新增故事关闭率*0.6+历史遗留故事关闭率*0.4")
//	@GetMapping("/getSprintTargetRate")
//	public R getSprintTargetRate(Long sprintId){
//		return R.ok().put("sprintTargetRate", projectService.getSprintTargetRate(sprintId));
//	}
//
//	@ApiOperation("人均关闭故事点数")
//	@GetMapping("/getAvgStoryPoint")
//	public R getAvgStoryPoint(Long sprintId){
//		return R.ok().put("avgStoryPoint", projectService.getAvgStoryPoint(sprintId));
//	}
//
//	@ApiOperation("平均故事关闭时长")
//	@GetMapping("/getAvgStoryTime")
//	public R getAvgStoryTime(Long sprintId){
//		return R.ok().put("avgStoryTime", projectService.getAvgStoryTime(sprintId));
//	}
//
//	@ApiOperation("平均故事延期时长")
//	@GetMapping("/getAvgStoryDelay")
//	public R getAvgStoryDelay(Long sprintId){
//		return R.ok().put("avgStoryDelay", projectService.getAvgStoryDelay(sprintId));
//	}
//
//	@ApiOperation("重新打开故事次数")
//	@GetMapping("/getReopenStory")
//	public R getReopenStory(Long sprintId){
//		return R.ok().put("reopenStory", projectService.getReopenStory(sprintId));
//	}
//
//	@ApiOperation("迭代遗留故事占比")
//	@GetMapping("/getRemainStory")
//	public R getRemainStory(Long sprintId){
//		return R.ok().put("remainStory", projectService.getRemainStory(sprintId));
//	}
//
//	@ApiOperation("逾期完成故事点数")
//	@GetMapping("/getOverdueStoryPoint")
//	public R getOverdueStoryPoint(Long sprintId){
//		return R.ok().put("overdueStoryPoint", projectService.getOverdueStoryPoint(sprintId));
//	}
//
//	@ApiOperation("缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数")
//	@GetMapping("/getDefectCloseRate")
//	public R getDefectCloseRate(Long sprintId){
//		return R.ok().put("defectCloseRate", projectService.getDefectCloseRate(sprintId));
//	}
//
//	@ApiOperation("缺陷密度=新增缺陷数/关闭故事点数")
//	@GetMapping("/getDefectDensity")
//	public R getDefectDensity(Long sprintId){
//		return R.ok().put("defectDensity", projectService.getDefectDensity(sprintId));
//	}
//
//	@ApiOperation("遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷")
//	@GetMapping("/getRemainDefectRate")
//	public R getRemainDefectRate(Long sprintId){
//		return R.ok().put("remainDefectRate", projectService.getRemainDefectRate(sprintId));
//	}
//
//	@ApiOperation("本迭代内线上反馈的缺陷")
//	@GetMapping("/getOnlineDefect")
//	public R getOnlineDefect(Long sprintId){
//		return R.ok().put("onlineDefect", projectService.getOnlineDefect(sprintId));
//	}
//
//	@ApiOperation("平均线上缺陷修复时长")
//	@GetMapping("/getOnlineRepairTime")
//	public R getOnlineRepairTime(Long sprintId){
//		return R.ok().put("onlineRepairTime", projectService.getOnlineRepairTime(sprintId));
//	}

	@ApiOperation("迭代内各项指标")
	@GetMapping("/getSprintStatisticsEntityBySprintId")
	public R getSprintStatisticsEntityBySprintId(Long sprintId){
		return R.ok().put("sprintStatistics", projectService.getSprintStatisticsEntityBySprintId(sprintId));
	}
}
