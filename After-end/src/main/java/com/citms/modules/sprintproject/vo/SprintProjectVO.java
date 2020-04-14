package com.citms.modules.sprintproject.vo;

import lombok.Data;

import java.util.List;

/**
 * 类SprintVo的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/26 10:49
 */
@Data
public class SprintProjectVO {

    //项目ID
    private Long               projectId;
    //项目名称
    private String             projectName;
    //迭代信息
    private List<SprintInfoVO> sprintList;
}
