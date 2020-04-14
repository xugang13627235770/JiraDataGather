package com.citms.modules.jira.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类IssueDateObj的实现描述：TODO 类实现描述 
 * @author DELL 2019/12/3 17:24
 */
@Data
public class StoryPointObj {
    private Integer          issueStatus;//问题状态
    private String           type;//问题状态值
    private List<BigDecimal> data;//问题数量
}
