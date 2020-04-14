package com.citms.modules.home.vo;

import lombok.Data;

/**
 * 类IssueDateVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/28 10:35
 */
@Data
public class IssueDateVO {

    private String issueDate;//问题日期

    private Integer issueStatus;//问题状态

    private String issueStatusStr;//问题状态值

    private Integer issueNum;//问题数量
}
