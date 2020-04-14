package com.citms.modules.jira.enums;

public enum DictionaryEnum {

    ISSUE_STATUS("issue_status", "10"),//问题状态
    ISSUE_TYPE("issue_type", "20"),//问题类型
    USER_ACTIVE("user_active", "30"),//用户激活
    ISSUE_RESOLUTION("issue_resolution", "40"),//解决结果
    ISSUE_PRIORITY("issue_priority", "50"),//问题优先级
    SPRINT_STATE("sprint_state", "60"),//迭代状态
    ISSUE_DEFECT("issue_defect", "80"),//缺陷类型
    ISSUE_SOURCE("issue_source", "90"),//缺陷来源
    USER_TYPE("user_type", "100"),//用户类型
    LOG_CHANGE_STATUS("log_change_status", "110");//变更值状态

    private String name ;
    private String index ;

    DictionaryEnum(String name, String index) {
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
