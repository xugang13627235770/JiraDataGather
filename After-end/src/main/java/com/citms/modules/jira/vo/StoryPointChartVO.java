package com.citms.modules.jira.vo;

import lombok.Data;

import java.util.List;

/**
 * 类IssueDateVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/28 10:35
 */
@Data
public class StoryPointChartVO {

    private List<String> timeArr;//问题日期

    private List<StoryPointObj> list;//

}
