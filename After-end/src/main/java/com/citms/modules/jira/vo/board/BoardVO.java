/**
 * Copyright 2019 bejson.com
 */
package com.citms.modules.jira.vo.board;

import java.util.List;

/**
 * Auto-generated: 2019-11-11 11:5:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BoardVO {

    private int          maxResults;
    private int          startAt;
    private int          total;
    private boolean      isLast;
    private List<Values> values;

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    public List<Values> getValues() {
        return values;
    }

}