package com.citms.modules.developer.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 开发人员统计记录
 * @author DELL 2019/11/19 14:10
 */
@Data
public class DeveloperRecordVO extends BaseRowModel {

    private Long userId;//用户ID

    private Integer userType;//用户类型

    @ExcelProperty(value = "人员名称", index = 0)
    private String developerName;//人员名称

    @ExcelProperty(value = "关闭故事点数", index = 1)
    private BigDecimal closeStoryNum;//关闭故事点数

    @ExcelProperty(value = "提测延期天数", index = 2)
    private Long testDelayDays;//提测延期天数

    @ExcelProperty(value = "提测打回次数", index = 3)
    private Long testBackTimes;//提测打回次数

    @ExcelProperty(value = "缺陷回归次数", index = 4)
    private Long issueReturnTimes;//缺陷回归次数

    @ExcelProperty(value = "红色缺陷遗留", index = 5)
    private Long redIssue;//红色缺陷遗留

    @ExcelProperty(value = "非红色缺陷遗留", index = 6)
    private Long noredIssue;//非红色缺陷遗留

    @ExcelProperty(value = "有效缺陷数量", index = 7)
    private Long validIssueNum;//有效缺陷数量

    private BigDecimal issueCloseRate;//缺陷关闭率

    @ExcelProperty(value = "线上反馈缺陷", index = 9)
    private Long onlineIssue;//线上反馈缺陷

    @ExcelProperty(value = "总积分", index = 10)
    private BigDecimal totalScore;//总积分

    @ExcelProperty(value = "缺陷关闭率", index = 8)
    private String issueCloseRateStr;//缺陷关闭率

    private Integer order;//1:上涨   0：不变   -1：下跌

}
