package com.citms.modules.tester.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类TesterRecord的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/21 13:51
 * 测试统计记录
 */
@Data
public class TesterRecordVO extends BaseRowModel {

    private Long userId;

    @ExcelProperty(value = "姓名", index = 0)
    private String userName;//人员名称

    @ExcelProperty(value = "缺陷有效数量", index = 1)
    private BigDecimal issueValidNum;//缺陷有效数量

    @ExcelProperty(value = "缺陷有效率", index = 2)
    private BigDecimal issueValidRate;//缺陷有效率

    @ExcelProperty(value = "违规操作次数", index = 3)
    private BigDecimal violateOperateTimes;//违规操作次数

    @ExcelProperty(value = "有效缺陷新增数量", index = 4)
    private BigDecimal issueAddNum;//有效缺陷新增数量

    @ExcelProperty(value = "缺陷平均耗时", index = 5)
    private BigDecimal taskTimes;//缺陷平均耗时

    @ExcelProperty(value = "历史缺陷关闭数量", index = 6)
    private BigDecimal historyCloseNum;//历史缺陷关闭数量

    @ExcelProperty(value = "线上反馈缺陷数量", index = 7)
    private BigDecimal onlineIssueNum;//线上反馈缺陷数量

    @ExcelProperty(value = "总积分", index = 8)
    private BigDecimal totalScore;//总积分

    private Integer order;//1:上涨   0：不变   -1：下跌
}
