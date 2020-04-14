package com.citms.modules.jira.vo.issue;

import java.util.List;

/**
 * 类ChangeLog的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/11 11:24
 */
public class Changelog {
    private String              id;
    private List<ChangeLogItem> items;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setItems(List<ChangeLogItem> items) {
        this.items = items;
    }

    public List<ChangeLogItem> getItems() {
        return this.items;
    }
}
