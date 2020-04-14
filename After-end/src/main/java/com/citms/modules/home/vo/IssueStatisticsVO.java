package com.citms.modules.home.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类IssueStatisticsVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/28 10:53
 * 总的问题统计
 */
@Data
public class IssueStatisticsVO {

    private Long projectId;//项目ID

    private String projectName;//项目名称

    private Integer totalStory;//总的故事数

    private BigDecimal totalStoryPoint;//总的故事点数

    private Integer totalIssue;//总的缺陷数

    private Long totalDeveloper;//总的开发人数

    private Long totalTester;//总的测试人数

    private Long totalDesigner;//总的设计人数
}
