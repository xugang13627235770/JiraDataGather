package com.citms.modules.project.controller;

import com.citms.common.utils.R;
import com.citms.modules.project.service.ConfigProjectService;
import com.citms.modules.project.vo.ConfigProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目统计规则
 */
@RestController
@RequestMapping("/configProject")
@Api("项目统计规则")
public class ConfigProjectController {

    @Autowired
    private ConfigProjectService configProjectService;

    /**
     * 获取项目统计动态规则配置
     * @return
     */
    @ApiOperation("获取项目统计动态规则配置")
    @GetMapping("/getConfigProjectRuleInfo")
    public R getConfigProjectRuleInfo(){

        return R.ok().put("projectRuleInfo", configProjectService.getConfigProjectRuleInfo());
    }

    /**
     * 保存项目统计动态规则配置
     * @param vo
     * @return
     */
    @ApiOperation("保存项目统计动态规则配置")
    @PostMapping("/saveConfigProjectRule")
    public R saveConfigProjectRule(@RequestBody ConfigProjectVO vo) {

        configProjectService.saveConfigProjectRule(vo);
        return R.ok();
    }
}
