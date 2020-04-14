package com.citms.modules.project.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类StoryPointScaleVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/12/3 16:03
 */
@Data
public class StoryPointScaleVO {

    private String displayName;//人员名称

    private BigDecimal storyPointRate;//故事点数占比

    private BigDecimal totalStoryPoint;//总故事点数
}
