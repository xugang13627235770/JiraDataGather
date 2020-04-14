/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.issue;

/**
 * Auto-generated: 2019-11-11 10:53:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Issuetype {

    private String  self;
    private String  id;
    private String  description;
    private String  iconUrl;
    private String  name;
    private boolean subtask;
    private int     avatarId;

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

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }

    public boolean getSubtask() {
        return subtask;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getAvatarId() {
        return avatarId;
    }

}