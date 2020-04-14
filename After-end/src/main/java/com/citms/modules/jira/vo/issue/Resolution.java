package com.citms.modules.jira.vo.issue;

/**
 * 类Resolution的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/12 14:19
 */

/**
 * Copyright 2019 bejson.com
 */
public class Resolution {

    private String self;
    private String id;
    private String description;
    private String name;

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
