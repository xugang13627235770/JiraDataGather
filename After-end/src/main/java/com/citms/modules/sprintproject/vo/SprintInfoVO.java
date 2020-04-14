package com.citms.modules.sprintproject.vo;

import com.citms.modules.sprintproject.entity.SprintEntity;
import lombok.Data;

/**
 * 类SprintInfoVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/26 15:34
 */
@Data
public class SprintInfoVO extends SprintEntity {

    //项目ID
    private Long projectId;
    //项目名称
    private String projectName;

    private String sprintIdStr;//迭代ID，以逗号隔开
}
