package com.citms.modules.home.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类IssueScale的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/28 10:03
 */
@Data
public class IssueScaleVO {

    private String displayName;//人员名称

    private BigDecimal issueRate;//问题占比

}
