package com.citms.modules.jira.enums;

/**
 * 类WebhookEventEnum的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/11 15:30
 */
public enum WebhookEventEnum {


    ISSUE("jira:issue", "10"),
    PROJECT("project", "20"),
    SPRINT("sprint", "30"),
    ISSUE_CREATED("jira:issue_created", "40"),
    ISSUE_UPDATED("jira:issue_updated", "50"),
    ISSUE_DELETED("jira:issue_deleted", "55"),
    PROJECT_CREATED("project_created", "60"),
    PROJECT_UPDATED("project_updated", "70"),
    SPRINT_LABEL("sprint", "80"),
    SPRINT_DELETED("sprint_deleted", "90");


    private String name ;
    private String index ;

    WebhookEventEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
