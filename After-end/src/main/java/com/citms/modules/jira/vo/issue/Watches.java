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
public class Watches {

    private String  self;
    private int     watchCount;
    private boolean isWatching;

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setIsWatching(boolean isWatching) {
        this.isWatching = isWatching;
    }

    public boolean getIsWatching() {
        return isWatching;
    }

}