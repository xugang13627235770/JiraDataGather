package com.citms.modules.project.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类StoryPointScaleVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/12/3 16:03
 */
@Data
public class DefectScaleVO {

    private String displayName;//人员名称

    private BigDecimal defectRate;//缺陷占比

    private BigDecimal totalDefect;//总缺陷数量
}
