/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.issue;

import java.util.List;

/**
 * Auto-generated: 2019-11-11 10:53:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Worklog {

    private int          startAt;
    private int          maxResults;
    private int          total;
    private List<String> worklogs;

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setWorklogs(List<String> worklogs) {
        this.worklogs = worklogs;
    }

    public List<String> getWorklogs() {
        return worklogs;
    }

}