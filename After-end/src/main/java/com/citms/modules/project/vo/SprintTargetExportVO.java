package com.citms.modules.project.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 类SprintTargeVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/29 9:49
 * 迭代指标
 */
@Data
public class SprintTargetExportVO extends BaseRowModel {

    @ExcelProperty(value = "指标名称", index = 0)
    private String name;//指标名称

    @ExcelProperty(value = "指标值", index = 1)
    private String targetValue;//指标数据

    @ExcelProperty(value = "得分值", index = 2)
    private String targetScore;//得分值
}
