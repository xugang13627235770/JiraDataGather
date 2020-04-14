package com.citms.modules.home.vo;

import lombok.Data;

/**
 * 问题类型统计
 * @author DELL 2019/11/28 9:10
 */
@Data
public class IssueTypeVO {

    private Integer issueType;//问题类型

    private String issueTypeStr;//问题类型值

    private Integer closeIssueNum;//关闭问题数

    private Integer noCloseIssueNum;//未关闭问题数
}
