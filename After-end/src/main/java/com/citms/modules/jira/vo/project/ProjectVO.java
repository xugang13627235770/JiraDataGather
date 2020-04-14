package com.citms.modules.jira.vo.project;

import lombok.Data;

/**
 * 类ProjectVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/18 15:22
 */
@Data
public class ProjectVO {
    private long   timestamp;
    private String webhookEvent;
    private Project project;
}